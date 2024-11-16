package org.csp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class Rocket {
    private ArrayList<RocketStage> stages;
    private RocketState state;


    public Rocket(ArrayList<RocketStage> stages) {
        this.stages = stages;
    }
    public Rocket(List<RocketStage> stages) {
        this.stages = new ArrayList<>(stages);
    }

    public ArrayList<RocketStage> getStages() {
        return stages;
    }

    public static Codec<Rocket> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketStage.CODEC.listOf()).fieldOf("stages").forGetter(Rocket::getStages)
            ).apply(builder, Rocket::new));
}
