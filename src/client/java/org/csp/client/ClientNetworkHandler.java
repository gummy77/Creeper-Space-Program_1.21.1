package org.csp.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import org.csp.entity.RocketEntity;
import org.csp.entity.StageEntity;
import org.csp.payload.UpdateRocketPayload;
import org.csp.payload.UpdateStagePayload;

public class ClientNetworkHandler {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateRocketPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.rocketID());
                if(entity instanceof RocketEntity rocketEntity){
                    rocketEntity.setRocket(payload.rocketData());
                    rocketEntity.setBoundingBox(rocketEntity.calculateBoundingBox());
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(UpdateStagePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Entity entity = context.client().world.getEntityById(payload.stageID());
                if(entity instanceof StageEntity stageEntity){
                    stageEntity.setStage(payload.stageData());
                }
            });
        });
    }
}
