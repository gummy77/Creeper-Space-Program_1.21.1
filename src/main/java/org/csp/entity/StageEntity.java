package org.csp.entity;

import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.csp.component.RocketStage;

public class StageEntity extends Entity {
    private RocketStage stage;

    public StageEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
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
