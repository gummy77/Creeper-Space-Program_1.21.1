package org.csp.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;

public class RocketState {
    private LaunchState launchState;
    private float launchTimer;
    private int currentStage;

    public RocketState(LaunchState launchState) {
        this.launchState = launchState;
        this.launchTimer = 2f;
        this.currentStage = 0;
    }

    public RocketState(String launchState, float launchTimer, int currentStage) {
        this.launchState = LaunchState.valueOf(launchState);
        this.launchTimer = launchTimer;
        this.currentStage = currentStage;
    }

    public void setLaunchTimer(float timer) { this.launchTimer = Math.max(timer, 0); }
    public float getLaunchTimer() { return this.launchTimer; }

    public void setLaunchState(LaunchState launchState) { this.launchState = launchState; }
    public String getLaunchStateString() { return launchState.toString(); }
    public LaunchState getLaunchState() { return launchState; }


    public void stage() { this.currentStage++; }
    public int getCurrentStage() {
        return this.currentStage;
    }

    public enum LaunchState {
        IDLE,
        IGNITION,
        LAUNCHING,
        COASTING
    }

    public static Codec<RocketState> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codecs.NON_EMPTY_STRING.fieldOf("launchState").forGetter(RocketState::getLaunchStateString),
                    Codec.FLOAT.fieldOf("launchTimer").forGetter(RocketState::getLaunchTimer),
                    Codec.INT.fieldOf("currentStage").forGetter(RocketState::getCurrentStage)
            ).apply(builder, RocketState::new));
}
