package org.csp.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.csp.payload.UpdateRocketPayload;

public class ClientNetworkHandler {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateRocketPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                //update rocket
            });
        });
    }
}
