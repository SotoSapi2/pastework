package io.pastework.test.common.registry;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.registry.*;
import io.pastework.test.common.PasteworkTest;
import io.pastework.test.common.entity.BurningPotato;
import io.pastework.test.common.entity.PlasmaCharge;
import lombok.experimental.UtilityClass;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

@UtilityClass
public class ModEntities
{
    private static final ICommonRegistry REGISTRY_SERVICE = ICommonRegistry.getService();
    private static final ICommonRegistrar<EntityType<?>> ENTITIES = REGISTRY_SERVICE.createRegistrar(
        PasteworkTest.MOD_ID,
        Registries.ENTITY_TYPE
    );

    public static final IEntryHolder<EntityType<BurningPotato>> BURNING_POTATO = register(
        "burning_potato",
        EntityType.Builder.of(BurningPotato::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
    );

    public static final IEntryHolder<EntityType<PlasmaCharge>> PLASMA_CHARGE = register(
        "plasma_charge",
        EntityType.Builder.<PlasmaCharge>of(PlasmaCharge::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
    );

    private static
    <_TEntity extends Entity>
    IEntryHolder<EntityType<_TEntity>> register(
        String name,
        EntityType.Builder<_TEntity> builder
    )
    {
        var key = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, name)
        );

        return ENTITIES.register(
            name,
            () -> builder.build(key)
        );
    }

    public static void initialize()
    {
        REGISTRY_SERVICE.enqueueRegistrar(ENTITIES);
    }
}
