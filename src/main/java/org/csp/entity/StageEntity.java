package org.csp.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.csp.component.RocketStage;
import org.csp.payload.UpdateStagePayload;

public class StageEntity extends Entity {
    private RocketStage stage;

    public static final EntitySettings settings = new EntitySettings(
            "stage_entity",
            SpawnGroup.MISC,
            0.6f, 2f,
            true
    );

    public StageEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();

        if(getWorld() instanceof ServerWorld) {
            networkUpdateData();
        }

        if(verticalCollision) {
            this.kill();
        }

        this.addVelocity(new Vec3d(0, -0.05f, 0));
        this.setVelocity(this.getVelocity().multiply(0.99f));
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Override
    public boolean handleAttack(Entity attacker) {
        if(attacker instanceof PlayerEntity) {
            this.kill();
            return true;
        }
        return super.handleAttack(attacker);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    public void setStage(RocketStage stage) {
        this.stage = stage;
    }

    public RocketStage getStage() {
        return stage;
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public void networkUpdateData() {
        if(this.stage != null) {
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) getWorld(), this.getBlockPos())) {
                ServerPlayNetworking.send(player, new UpdateStagePayload(this.getId(), this.stage));
            }
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        DataResult<RocketStage> dataResult = RocketStage.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("stage_data"));
        if(dataResult.isSuccess()) {
            this.stage = dataResult.getOrThrow();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.stage != null) {
            DataResult<NbtElement> dataResult = RocketStage.CODEC.encodeStart(NbtOps.INSTANCE, this.stage);
            if (dataResult.isSuccess()) {
                nbt.put("stage_data", dataResult.getOrThrow());
            }
        }
    }
}
