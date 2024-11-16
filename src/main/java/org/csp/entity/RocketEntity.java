package org.csp.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.csp.component.Rocket;
import org.csp.component.RocketPart;
import org.csp.payload.UpdateRocketPayload;
import org.csp.registry.NetworkingConstants;

public class RocketEntity extends Entity {
    private Rocket rocket;

    public static final EntitySettings settings = new EntitySettings(
            "rocket_entity",
            SpawnGroup.MISC,
            0.6f, 2f,
            true
    );

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWorld() instanceof ServerWorld) {
            networkUpdateData();
            this.setBoundingBox(this.calculateBoundingBox());
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        System.out.println(this.getRocket() != null ?  this.rocket.getWidth() : "Rocket Is not Real");

        return super.interact(player, hand);
    }

    public void addForce(float force, Vec3d offset) {

    }

    public void setRocket(Rocket rocket) {
        this.rocket = rocket;
    }

    public Rocket getRocket() {
        return rocket;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }



    @Override
    public Box calculateBoundingBox() {
        if(this.rocket != null) {
            return Box.of(this.getPos().add(0, this.rocket.getHeight()/2, 0), this.rocket.getWidth(), this.rocket.getHeight(), this.rocket.getWidth());
        } // TODO potentially add rotation? or something? so this looks more accurate when flying
        return super.calculateBoundingBox();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    public void networkUpdateData() {
        if(this.rocket != null) {
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), this.getBlockPos())) {
                ServerPlayNetworking.send(player, new UpdateRocketPayload(this.getId(), this.rocket));
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        DataResult<Rocket> dataResult = Rocket.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("rocket_data"));
        if(dataResult.isSuccess()) {
            this.rocket = dataResult.getOrThrow();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.rocket != null) {
            DataResult<NbtElement> dataResult = Rocket.CODEC.encodeStart(NbtOps.INSTANCE, this.rocket);
            if (dataResult.isSuccess()) {
                nbt.put("rocket_data", dataResult.getOrThrow());
            }
        }
    }
}
