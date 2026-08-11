package io.pastework.test.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistrar;
import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistry;
import io.pastework.test.common.PasteworkTest;
import lombok.experimental.UtilityClass;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@UtilityClass
public class PasteworkTestKeybinds
{
    private static final IKeyMappingRegistrar REGISTRAR = IKeyMappingRegistrar.create();
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
        Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, "keycategory")
    );

    public static final KeyMapping CAST_SPELL = REGISTRAR.register(
        "cast_spell",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_F,
        CATEGORY
    );

    public static final KeyMapping OPEN_SPELL_WHEEL = REGISTRAR.register(
        "open_spell_wheel",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_LEFT_ALT,
        CATEGORY
    );

    public static final KeyMapping QUICK_CAST_SPELL_0 = REGISTRAR.register(
        "use_spell_0",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_Z,
        CATEGORY
    );

    public static final KeyMapping QUICK_CAST_SPELL_1 = REGISTRAR.register(
        "use_spell_1",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_X,
        CATEGORY
    );

    public static final KeyMapping QUICK_CAST_SPELL_2 = REGISTRAR.register(
        "use_spell_2",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_C,
        CATEGORY
    );

    public static final KeyMapping QUICK_CAST_SPELL_3 = REGISTRAR.register(
        "use_spell_3",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_V,
        CATEGORY
    );

    public static void initialize()
    {
        IKeyMappingRegistry.getService()
            .enqueueRegistrar(REGISTRAR);
    }
}
