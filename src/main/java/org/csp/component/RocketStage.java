package org.csp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RocketStage {
    private ArrayList<RocketPart> parts;

    public RocketStage() {
        this.parts = new ArrayList<>();
    }

    public RocketStage(List<RocketPart> rocketParts) {
        this.parts = new ArrayList<>(rocketParts);
    }


    public void addPart(RocketPart part) {
        this.parts.add(part);
    }

    public ArrayList<RocketPart> getParts(){
        return this.parts;
    }

    public static Codec<RocketStage> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketPart.CODEC.listOf()).fieldOf("parts").forGetter(RocketStage::getParts)
            ).apply(builder, RocketStage::new));
}
