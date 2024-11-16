package org.csp.registry;

import net.fabricmc.fabric.api.block.v1.FabricBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.block.RocketPartBlock;
import org.csp.entity.RocketPartBlockEntity;

public class BlockRegistry {

    public static final RocketPartBlock WOODEN_NOSE_CONE = (RocketPartBlock) register("rocket/wooden/nose",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));
    public static final RocketPartBlock WOODEN_FUEL_SEGMENT = (RocketPartBlock) register("rocket/wooden/fuel_segment",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));
    public static final RocketPartBlock WOODEN_MOTOR = (RocketPartBlock) register("rocket/wooden/motor",
            new RocketPartBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)));

    public static BlockEntityType<RocketPartBlockEntity> ROCKET_PART_BLOCK_ENTITY = registerEntity("rocket_part_block_entity",
            BlockEntityType.Builder.create(RocketPartBlockEntity::new,
                    WOODEN_NOSE_CONE, WOODEN_FUEL_SEGMENT, WOODEN_MOTOR
            ).build());

    public static void initializeRegistry() {

    }

    private static Block register(String path, Block block){
        Identifier blockID = Identifier.of(CspMain.MOD_ID, path);
        return Registry.register(Registries.BLOCK, blockID, block);
    }

    public static <T extends BlockEntityType<?>> T registerEntity (String path, T type) {
        Identifier blockEntityID = Identifier.of(CspMain.MOD_ID, path);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, blockEntityID, type);
    }
}
