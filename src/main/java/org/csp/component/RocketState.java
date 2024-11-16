package org.csp.component;

import net.minecraft.util.math.Vec3d;

public class RocketState {
    private LaunchState launchState;


    public enum LaunchState {
        IDLE,
        IGNITION,
        LAUNCHING,
        COASTING
    }
}
