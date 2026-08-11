package io.pastework.test.client.controller.ui;

import io.pastework.core.api.client.event.input.ClientInputEvent;
import io.pastework.core.api.client.service.ui.IGuiContext;
import io.pastework.core.api.client.service.ui.IGuiLayerRegistry;
import io.pastework.core.api.client.service.ui.IGuiLayerRenderable;
import io.pastework.core.api.client.service.ui.VanillaHudLayers;
import io.pastework.core.api.common.event.IEventConnection;
import io.pastework.core.api.common.event.world.LevelTickEvent;
import io.pastework.core.api.common.service.attachment.IAttachableExtension;
import io.pastework.test.client.PasteworkTestKeybinds;
import io.pastework.test.client.controller.ClientSorceryController;
import io.pastework.test.common.PasteworkTest;
import io.pastework.test.common.attachment.ManaAttachment;
import io.pastework.test.common.registry.Attachments;
import io.pastework.test.common.spell.AbstractSpell;
import lombok.Getter;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SorceryHUDController implements IGuiLayerRenderable
{
    public static final Identifier SORCERY_UI_LAYER = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "sorcery_layer"
    );

    private float textPosX = 100;
    private float textPosY = 100;
    private float textVelX = 5.0f;
    private float textVelY = 5.0f;
    private final List<QuickCastKey> quickCastKeybindList = new ArrayList<>();
    private final List<IEventConnection> eventConnectionList = new ArrayList<>();

    @Getter
    private final ClientSorceryController controller;

    @Getter
    private int hoveredSpellIndex;

    @Getter
    private boolean wasSpellWheelDown;

    @Getter
    private boolean isSelectionHudRendered;

    private record QuickCastKey(KeyMapping keyMapping, int spellIndex)
    { }

    public SorceryHUDController(ClientSorceryController controller)
    {
        this.controller = controller;

        quickCastKeybindList.add(new QuickCastKey(PasteworkTestKeybinds.QUICK_CAST_SPELL_0, 0));
        quickCastKeybindList.add(new QuickCastKey(PasteworkTestKeybinds.QUICK_CAST_SPELL_1, 1));
        quickCastKeybindList.add(new QuickCastKey(PasteworkTestKeybinds.QUICK_CAST_SPELL_2, 2));
        quickCastKeybindList.add(new QuickCastKey(PasteworkTestKeybinds.QUICK_CAST_SPELL_3, 3));
    }

    public void initialize()
    {
        IGuiLayerRegistry.getService()
            .registerAfter(
                VanillaHudLayers.CROSSHAIR,
                SORCERY_UI_LAYER,
                this
            );

        eventConnectionList.add(
            LevelTickEvent.PRE.connect(this::onWorldTick)
        );

        eventConnectionList.add(
            ClientInputEvent.MOUSE_SCROLLED.connect(this::onMouseScroll)
        );
    }

    public void close()
    {
        for(var connection : eventConnectionList)
        {
            connection.disconnect();
        }

        eventConnectionList.clear();
    }

    public void openSpellWheelHud()
    {
        Minecraft.getInstance().mouseHandler.releaseMouse();
        isSelectionHudRendered = !isSelectionHudRendered;
    }

    public void closeSpellWheelHud()
    {
        Minecraft.getInstance().mouseHandler.grabMouse();
        isSelectionHudRendered = !isSelectionHudRendered;
    }

    @Override
    public void renderGuiLayer(IGuiContext ctx)
    {
        final var player = ctx.getMinecraft().player;
        if (player == null)
        {
            return;
        }

        if (((IAttachableExtension) player).hasAttachment(Attachments.MANA))
        {
            updateManaIndicatorHud(ctx);
        }

        updateSpellListHud(ctx);

        if(isSelectionHudRendered)
        {
            updateSpellSelectionHud(ctx);
        }
    }

    private @Nullable QuickCastKey getPressedQuickCastKey()
    {
        for (QuickCastKey castKey : quickCastKeybindList)
        {
            if (castKey.keyMapping().consumeClick())
            {
                return castKey;
            }
        }

        return null;
    }

    private void updateManaIndicatorHud(IGuiContext ctx)
    {
        final var player = ctx.getMinecraft().player;
        assert ((IAttachableExtension) player).hasAttachment(Attachments.MANA);

        ManaAttachment manaAttachment = ((IAttachableExtension) player).getOrThrow(Attachments.MANA);
        final var text = String.format(
            "Your mana: %d/%d",
            manaAttachment.amount(),
            manaAttachment.maxAmount()
        );
        final var delta = ctx.getDeltaTracker().getRealtimeDeltaTicks();
        final var drawer = ctx.getGraphics();
        final var font = ctx.getDefaultFont();
        final var screen = ctx.getWindow();
        final int textWidth = font.width(text);
        final int textHeight = font.lineHeight;

        int screenWidth = screen.getGuiScaledWidth();
        int screenHeight = screen.getGuiScaledHeight();

        textPosX += textVelX * delta;
        textPosY += textVelY * delta;

        if (textPosX <= 0 || textPosX + textWidth >= screenWidth)
        {
            textVelX = -textVelX;
            textPosX = Mth.clamp(textPosX, 0, screenWidth - textWidth);
        }

        if (textPosY <= 0 || textPosY + textHeight >= screenHeight)
        {
            textVelY = -textVelY;
            textPosY = Mth.clamp(textPosY, 0, screenHeight - textHeight);
        }

        drawer.drawString(font, text, (int) textPosX, (int) textPosY, ARGB.white(255));
    }

    private void updateSpellSelectionHud(IGuiContext ctx)
    {
        final var drawer = ctx.getGraphics();
        final var font = ctx.getDefaultFont();
        final int guiCenterX = ctx.getGuiScaledWidth() / 2;
        final int guiCenterY = ctx.getGuiScaledHeight() / 2;
        final int screenCenterX = ctx.getScreenWidth() / 2;
        final int screenCenterY = ctx.getScreenHeight() / 2;
        final double mouseX = ctx.getMouseHandler().xpos();
        final double mouseY = ctx.getMouseHandler().ypos();
        final List<AbstractSpell> spells = controller.getSpellList();

        if (spells.isEmpty())
        {
            drawer.drawCenteredString(
                font,
                "No spells available",
                guiCenterX,
                guiCenterY,
                ARGB.white(255)
            );
            return;
        }

        float mouseAngle = (float) Mth.atan2(mouseY - screenCenterY, mouseX - screenCenterX);
        if (mouseAngle < 0)
        {
            mouseAngle += Mth.TWO_PI;
        }

        int spellCount = spells.size();
        float anglePerSpell = Mth.TWO_PI / spellCount;
        float radius = 50.0f;
        int selectedIndex = controller.getSelectedSpellIndex();

        // Calculate hovered index based on mouse angle
        // Add half angle per spell to shift the sector bounds so that the icon is in the center of the sector
        float adjustedAngle = mouseAngle + (anglePerSpell / 2.0f);
        if (adjustedAngle >= Mth.TWO_PI)
        {
            adjustedAngle -= Mth.TWO_PI;
        }

        // Only consider hovered if the mouse is far enough from the center
        double distSq = (mouseX - guiCenterX) * (mouseX - guiCenterX) + (mouseY - guiCenterY) * (mouseY - guiCenterY);
        // arbitrary threshold inner circle radius
        if (distSq > 400)
        {
            hoveredSpellIndex = (int) (adjustedAngle / anglePerSpell);
            if (hoveredSpellIndex >= spellCount)
            {
                hoveredSpellIndex = 0;
            }
        }
        else
        {
            hoveredSpellIndex = -1;
        }

        for (int i = 0; i < spellCount; i++)
        {
            AbstractSpell spell = spells.get(i);
            float angle = i * anglePerSpell;

            int x = (int) (guiCenterX + radius * Mth.cos(angle));
            int y = (int) (guiCenterY + radius * Mth.sin(angle));

            int color = ARGB.white(255);
            if (i == selectedIndex)
            {
                color = ARGB.color(255, 0, 255, 0); // Green for currently selected
            }

            if (i == hoveredSpellIndex)
            {
                color = ARGB.color(255, 255, 255, 0); // Yellow for currently hovered
            }

            // Draw a placeholder circle or icon
            drawer.fill(
                x - 10, y - 10, x + 10,
                y + 10,
                ARGB.color(128, 0, 0, 0)
            );

            // Render spell identifier for now
            String name = spell.getIdentifier().getPath();
            if (name.length() > 6)
            {
                name = name.substring(0, 6);
            }

            drawer.drawCenteredString(font, name, x, y - 4, color);
        }

        if (hoveredSpellIndex != -1)
        {
            AbstractSpell hoveredSpell = spells.get(hoveredSpellIndex);
            drawer.drawCenteredString(
                font,
                hoveredSpell.getIdentifier().getPath(),
                guiCenterX,
                guiCenterY - 80,
                ARGB.white(255)
            );
        }
    }

    private int handleSpellScrollIndexing(double scrollY, List<AbstractSpell> spells)
    {
        int currentIndex = controller.getSelectedSpellIndex();

        if (scrollY > 0)
        {
            currentIndex--;
        }

        else if (scrollY < 0)
        {
            currentIndex++;
        }

        if (currentIndex < 0)
        {
            currentIndex = spells.size() - 1;
        }

        else if (currentIndex >= spells.size())
        {
            currentIndex = 0;
        }

        return currentIndex;
    }

    private void updateSpellListHud(IGuiContext ctx)
    {
        final var drawer = ctx.getGraphics();
        final var font = ctx.getDefaultFont();
        final List<AbstractSpell> spells = controller.getSpellList();

        if (spells.isEmpty())
        {
            return;
        }

        int startX = 10;
        int startY = ctx.getGuiScaledHeight() / 4;
        int selectedIndex = controller.getSelectedSpellIndex();

        drawer.drawString(font, "Spells:", startX, startY, ARGB.white(255));
        startY += font.lineHeight + 4;

        for (int i = 0; i < spells.size(); i++)
        {
            AbstractSpell spell = spells.get(i);
            String name = spell.getIdentifier().getPath();
            int color = ARGB.color(255, 200, 200, 200); // Default grey
            String prefix = "  ";

            if (i == selectedIndex)
            {
                color = ARGB.color(255, 0, 255, 0); // Green for selected
                prefix = "> ";
            }

            drawer.drawString(font, prefix + name, startX, startY, color);
            startY += font.lineHeight + 2;
        }
    }

    private void onMouseScroll(ClientInputEvent.MouseScrollContext ctx)
    {
        if(!isSelectionHudRendered)
        {
            return;
        }

        double scrollY = ctx.getDelta().y;
        if (scrollY != 0)
        {
            List<AbstractSpell> spells = controller.getSpellList();

            if (spells.isEmpty())
            {
                return;
            }

            int currentIndex = handleSpellScrollIndexing(scrollY, spells);

            controller.trySelectSpell(currentIndex);
            ctx.setCancelled(true);
        }
    }

    private void onWorldTick(Level level)
    {
        if (level.isClientSide())
        {
            if(PasteworkTestKeybinds.OPEN_SPELL_WHEEL.isDown())
            {
                if(!wasSpellWheelDown)
                {
                    openSpellWheelHud();
                    wasSpellWheelDown = true;
                }
            }

            final boolean wasReleased = wasSpellWheelDown && !PasteworkTestKeybinds.OPEN_SPELL_WHEEL.isDown();

            if(wasReleased && isSelectionHudRendered)
            {
                closeSpellWheelHud();
                if(hoveredSpellIndex != ClientSorceryController.INVALID_SPELL_INDEX)
                {
                    controller.trySelectSpell(hoveredSpellIndex);
                }

                wasSpellWheelDown = false;
            }

            if (PasteworkTestKeybinds.CAST_SPELL.consumeClick())
            {
                controller.tryCastSpell();
            }

            QuickCastKey quickCastKey = getPressedQuickCastKey();

            if (quickCastKey != null)
            {
                boolean casted = controller.trySelectAndCastSpell(quickCastKey.spellIndex());
            }
        }
    }
}
