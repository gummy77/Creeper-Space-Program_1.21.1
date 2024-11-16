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

    private float cachedwidth = 0;
    private float cachedheight = 0;

    public Rocket(ArrayList<RocketStage> stages) {
        this.stages = stages;
    }
    public Rocket(List<RocketStage> stages) {
        this.stages = new ArrayList<>(stages);
    }

    public float getWidth() {
        if (this.cachedwidth != 0) return this.cachedwidth;
        float maxWidth = 0;
        for (RocketStage stage : stages) {
            for (RocketPart part : stage.getParts()) {
                if(part.getWidth() > maxWidth) maxWidth = part.getWidth();
            }
        }
        this.cachedwidth = maxWidth / 16f;
        return this.cachedwidth;
    }
    public float getHeight() {
        if (this.cachedheight != 0) return this.cachedheight;

        int height = 0;
        for (RocketStage stage : stages) {
            height += stage.getParts().size();
        }
        this.cachedheight = height;
        return this.cachedheight;
    }

    public ArrayList<RocketStage> getStages() {
        return stages;
    }

    public static Codec<Rocket> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.nonEmptyList(RocketStage.CODEC.listOf()).fieldOf("stages").forGetter(Rocket::getStages)
            ).apply(builder, Rocket::new));
}
