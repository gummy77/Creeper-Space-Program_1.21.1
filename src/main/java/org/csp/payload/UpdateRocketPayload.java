package org.csp.payload;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.csp.component.Rocket;
import org.csp.registry.NetworkingConstants;

public record UpdateRocketPayload(int rocketID, Rocket rocketData) implements CustomPayload {
    public static final CustomPayload.Id<UpdateRocketPayload> ID = new CustomPayload.Id<>(NetworkingConstants.UPDATE_ROCKET_DATA_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateRocketPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateRocketPayload::rocketID,
            PacketCodecs.codec(Rocket.CODEC), UpdateRocketPayload::rocketData,
            UpdateRocketPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
