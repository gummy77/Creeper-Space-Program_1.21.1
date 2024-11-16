package org.csp;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.csp.registry.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CspMain implements ModInitializer {
    public static final String MOD_ID = "csp";
    public static final Logger LOGGER = LogManager.getLogger("Creeper Space Program");

    @Override
    public void onInitialize() {
        LOGGER.debug("CSP Setup Starting");

        ComponentRegistry.initializeRegistry();
        ItemRegistry.initializeRegistry();
        BlockRegistry.initializeRegistry();
        EntityRegistry.initializeRegistry();
        NetworkingConstants.registerPayloads();

        RocketPartRegistry.initializeRegistry();

        LOGGER.debug("CSP Setup Complete");
    }
}
