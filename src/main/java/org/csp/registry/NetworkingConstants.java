package org.csp.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.payload.*;

public class NetworkingConstants {
    public static final Identifier UPDATE_ROCKET_DATA_PACKET_ID = Identifier.of(CspMain.MOD_ID,"update_rocket_data_packet");
    public static final Identifier UPDATE_STAGE_DATA_PACKET_ID = Identifier.of(CspMain.MOD_ID,"update_stage_data_packet");

    public static void registerPayloads () {
        PayloadTypeRegistry.playS2C().register(UpdateRocketPayload.ID, UpdateRocketPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateStagePayload.ID, UpdateStagePayload.CODEC);
    }
}
