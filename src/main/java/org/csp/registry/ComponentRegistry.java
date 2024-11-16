package org.csp.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.component.RocketPart;

public class ComponentRegistry {

    public static final ComponentType<RocketPart> ROCKET_PART_COMPONENT_TYPE = registerComponent("rocket_part", RocketPart.CODEC);

    public static void initializeRegistry() {
        CspMain.LOGGER.debug("Registering components complete");
    }

    public static <T> ComponentType<T> registerComponent(String path, Codec<T> codec) {
        Identifier componentID = Identifier.of(CspMain.MOD_ID, path);
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                componentID,
                ComponentType.<T>builder().codec(codec).build()
        );
    }
}
