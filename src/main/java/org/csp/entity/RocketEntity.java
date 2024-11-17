package org.csp.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
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
import net.minecraft.util.TimeHelper;
import net.minecraft.util.TimeSupplier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.csp.component.Rocket;
import org.csp.component.RocketPart;
import org.csp.component.RocketState;
import org.csp.payload.UpdateRocketPayload;
import org.csp.registry.NetworkingConstants;

import java.util.ArrayList;

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

        if (this.getRocket() == null) return; // probably loading from NBT data or needs to be networked

        if(this.getRocket().getState() == null) {
            this.getRocket().createNewState();
        }

        switch (this.getRocket().getState().getLaunchState()) {
            case IDLE -> {

            }
            case IGNITION -> {
                if(this.getRocket().getState().getLaunchTimer() <= 0) {
                    this.rocket.getState().setLaunchState(RocketState.LaunchState.LAUNCHING);
                } else {
                    this.rocket.getState().setLaunchTimer(this.getRocket().getState().getLaunchTimer() - 0.05f); // 20 ticks/second -> 0.05s per tick
                }
            }
            case LAUNCHING -> {
                this.tickStages();
            }
            case COASTING -> {

            }
        }

        this.addVelocity(new Vec3d(0, -0.05f, 0));
        this.move(MovementType.SELF, this.getVelocity());
    }

    private void tickStages() {
        if (this.getRocket().getCurrentStage() == null) {
            this.getRocket().getState().setLaunchState(RocketState.LaunchState.COASTING);
            return;
        }

        if(this.getRocket().getCurrentStage().getBurnTimeRemaining() <= 0) {
            if(this.getRocket().getState().getCurrentStage() < this.getRocket().getStages().size() - 1)
                this.move(MovementType.SELF, new Vec3d(0, this.getRocket().getCurrentStage().getHeight(), 0)); // TODO when doing rotations fix this
            this.getRocket().getState().stage();
        } else {
            tickEngines();
        }
    }

    private void tickEngines() {
        ArrayList<RocketPart> parts = this.getRocket().getCurrentStage().getParts();

        for (RocketPart part : parts) {
            if(part.getEngineComponent() != null) {
                part.getEngineComponent().tick(getWorld(), this, part);
                this.getRocket().getCurrentStage().setBurnTimeRemaining(this.getRocket().getCurrentStage().getBurnTimeRemaining() - part.getEngineComponent().getFuelConsumption() * 0.05f); // 20 ticks/second -> 0.05s per tick
            }
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        System.out.println((this.getRocket() != null ? "Rocket Data" : "No Rocket Data") + (getWorld().isClient() ? " on Client" : " on Server"));

        if(this.getRocket() == null || this.getRocket().getState() == null) return ActionResult.FAIL;
        if(this.getRocket().getState().getLaunchState() == RocketState.LaunchState.IDLE)
            this.getRocket().getState().setLaunchState(RocketState.LaunchState.IGNITION);

        return super.interact(player, hand);
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        if(attacker instanceof PlayerEntity) {
            this.kill();
            return true;
        }
        return super.handleAttack(attacker);
    }

    public void addForce(float force, Vec3d offset) {
        this.addVelocity(new Vec3d(0, force / 4750, 0));
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
    public Box calculateBoundingBox() {
        if(this.rocket != null) {
            return Box.of(this.getPos().add(0, this.rocket.getHeight()/2, 0), this.rocket.getWidth(), this.rocket.getHeight(), this.rocket.getWidth());
        } // TODO potentially add rotation? or something? so this looks more accurate when flying
        // TODO update to change depending on what stages are remaining
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
