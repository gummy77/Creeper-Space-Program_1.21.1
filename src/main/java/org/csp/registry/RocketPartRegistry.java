package org.csp.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.component.RocketPart;
import org.csp.component.rocketpartcomponents.EngineComponent;
import org.csp.component.rocketpartcomponents.FuelComponent;
import org.csp.item.RocketPartBlockItem;

public class RocketPartRegistry {

    public static void initializeRegistry() {
        try {
            registerRocketPart("rocket/wooden/nose",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_NOSE_CONE.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.NOSE,
                            6, 2
                    ).build());
            registerRocketPart("rocket/wooden/fuel_segment",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_FUEL_SEGMENT.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.FUEL,
                            6, 3
                    ).addFuelComponent(new FuelComponent(10f, 1f, 1f, 1f, FuelComponent.FuelType.SOLID)).build());
            registerRocketPart("rocket/wooden/motor",
                    new RocketPart.RocketPartBuilder(
                            BlockRegistry.WOODEN_MOTOR.getDefaultState(),
                            RocketPart.PartMaterial.WOOD,
                            RocketPart.PartType.ENGINE,
                            6, 3
                    ).addEngineComponent(new EngineComponent(250f, ParticleTypes.LARGE_SMOKE)).build());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerRocketPart(String path, RocketPart rocketPart) throws CloneNotSupportedException {
        Identifier identifier = Identifier.of(CspMain.MOD_ID, path);

        // create item settings with default values
        Item.Settings settings = new Item.Settings();
        settings.component(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE, rocketPart.clone());

        // register item
        Item item = Registry.register(Registries.ITEM, identifier, new RocketPartBlockItem(rocketPart.getBlock().getBlock(), settings));

        // add to itemgroup (creative tab)
        ItemGroupEvents.modifyEntriesEvent(ItemRegistry.CSP_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(item);
        });
    }
}
