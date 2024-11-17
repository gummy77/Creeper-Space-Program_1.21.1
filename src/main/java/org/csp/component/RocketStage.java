package org.csp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RocketStage {
    private ArrayList<RocketPart> parts;
    private float burnTimeRemaining;

    public RocketStage() {
        this.parts = new ArrayList<>();
    }

    public RocketStage(List<RocketPart> rocketParts, float burnTimeRemaining) {
        this.parts = new ArrayList<>(rocketParts);
        this.burnTimeRemaining = burnTimeRemaining;
    }

    public void calculateBurnTime() {
        for (RocketPart rocketPart : parts) {
            if (rocketPart.getFuelComponent() != null) {
                this.burnTimeRemaining += ((rocketPart.getFuelComponent().getCapactity() * rocketPart.getFuelComponent().getFillLevel()) * rocketPart.getFuelComponent().getBurnSpeed());
            }
        }
        System.out.println("Calculated Burn Time: " + this.burnTimeRemaining);
    }

    public void setBurnTimeRemaining(float burnTimeRemaining) { this.burnTimeRemaining = burnTimeRemaining; }
    public float getBurnTimeRemaining() { return this.burnTimeRemaining; }

    public void addPart(RocketPart part) {
        this.parts.add(part);
    }

    public ArrayList<RocketPart> getParts(){
        return this.parts;
    }

    public int getHeight() {
        int height = 0;
        for (RocketPart rocketPart : parts) {
            height += 1;
        }
        return height;
    }


    public static Codec<RocketStage> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketPart.CODEC.listOf()).fieldOf("parts").forGetter(RocketStage::getParts),
                    Codec.FLOAT.fieldOf("burnTimeRemaining").forGetter(RocketStage::getBurnTimeRemaining)
            ).apply(builder, RocketStage::new));
}
