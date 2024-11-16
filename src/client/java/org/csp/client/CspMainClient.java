package org.csp.client;

import net.fabricmc.api.ClientModInitializer;
import org.csp.client.registry.EntityRendererRegistry;

public class CspMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.initializeRegistry();
    }
}
