package io.pastework.test.common.registry;

import io.pastework.core.api.common.service.registry.ICommonRegistrar;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.api.common.service.registry.IEntryHolder;
import io.pastework.test.common.PasteworkTest;
import io.pastework.test.common.spell.*;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

@UtilityClass
public final class Spells
{
    private static final ICommonRegistry REGISTRY_SERVICE = ICommonRegistry.getService();
    public static final ResourceKey<Registry<SpellType<?>>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(
        Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, "spells")
    );
    public static final ICommonRegistrar<SpellType<?>> REGISTRAR = ICommonRegistrar.create(
        PasteworkTest.MOD_ID,
        SPELL_REGISTRY_KEY
    );

    public static final IEntryHolder<SpellType<PlasmaburstSpell>> PLASMABURST = register(
        "plasmaburst",
        SpellAttribute.builder()
            .useTickCooldown(10)
            .useCost(25)
            .build(),
        PlasmaburstSpell::new
    );

    public static final IEntryHolder<SpellType<PotatobreathSpell>> POTATOBREATH = register(
        "potatobreath",
        SpellAttribute.builder()
            .useTickCooldown(20)
            .useCost(50)
            .build(),
        PotatobreathSpell::new
    );

    public static final IEntryHolder<SpellType<HealSpell>> HEAL_BUFF = register(
        "heal_buff",
        SpellAttribute.builder()
            .useTickCooldown(40)
            .useCost(25)
            .build(),
        HealSpell::new
    );

    public static final IEntryHolder<SpellType<SpeedBuffSpell>> SPEED_BUFF = register(
        "speed_buff",
        SpellAttribute.builder()
            .useTickCooldown(30)
            .useCost(25)
            .build(),
        SpeedBuffSpell::new
    );

    public static Registry<SpellType<?>> getRegistry()
    {
        return REGISTRY_SERVICE.requestNativeRegistry(SPELL_REGISTRY_KEY)
            .value();
    }

    private static
    <_TSpell extends AbstractSpell>
    IEntryHolder<SpellType<_TSpell>> register(
        String name,
        SpellAttribute defaultAttribute,
        SpellType.Factory<_TSpell> factory
    )
    {
        return REGISTRAR.register(
            name,
            () -> new SpellType<>(
                Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, name),
                defaultAttribute,
                factory
            )
        );
    }

    public static void initialize()
    {
        REGISTRY_SERVICE.enqueueNewRegistry(SPELL_REGISTRY_KEY);
        REGISTRY_SERVICE.enqueueRegistrar(REGISTRAR);
    }
}
