package org.csp.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.entity.EntitySettings;
import org.csp.entity.RocketEntity;
import org.csp.entity.StageEntity;

public class EntityRegistry {

    public static EntityType<RocketEntity> ROCKET_ENTITY = registerEntity("rocket_entity", RocketEntity::new, RocketEntity.settings);
    public static EntityType<StageEntity> STAGE_ENTITY = registerEntity("stage_entity", StageEntity::new, StageEntity.settings);


    public static void initializeRegistry() {
    }

    protected static <T extends Entity> EntityType<T> registerEntity(String path, EntityType.EntityFactory<T> type, EntitySettings settings) {
        EntityType<T> entityType = Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(CspMain.MOD_ID, path),
                FabricEntityTypeBuilder.create(settings.spawnGroup, type)
                        .dimensions(EntityDimensions.fixed(settings.x, settings.y))
                        .build());

        if (settings.spawnsNaturally) {
            BiomeModifications.addSpawn(
                    (biomeSelectionContext -> biomeSelectionContext.hasTag(settings.selectorTag)),
                    settings.spawnGroup,
                    entityType,
                    settings.spawnWeight,
                    settings.minGroupSize,
                    settings.maxGroupSize);
        }
        return entityType;
    }
}
