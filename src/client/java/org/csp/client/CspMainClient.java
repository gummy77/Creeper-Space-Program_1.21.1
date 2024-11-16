package org.csp.client;

import net.fabricmc.api.ClientModInitializer;
import org.csp.client.registry.EntityRendererRegistry;
import org.csp.registry.NetworkingConstants;

public class CspMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetworkHandler.initialize();
        EntityRendererRegistry.initializeRegistry();
    }
}
