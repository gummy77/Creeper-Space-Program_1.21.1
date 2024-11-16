package org.csp.component.rocketpartcomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.csp.component.RocketPart;
import org.csp.entity.RocketEntity;

public class EngineComponent {
    private final float power;
    private final ParticleEffect exhaustParticle;

    public EngineComponent(float power, ParticleEffect exhaustparticle) {
        this.power = power;
        this.exhaustParticle = exhaustparticle;
    }

    public float getPower() {
        return power;
    }

    public ParticleEffect getExhaustparticle() {
        return exhaustParticle;
    }

    public void tick(World world, RocketEntity rocket, RocketPart rocketPart) {
        // add force to rocket
        rocket.addForce(this.power, rocketPart.getOffset());

        // render engine particles
        Vec3d particlePosition = rocket.getPos();
        particlePosition.add(rocketPart.getOffset().getX(), rocketPart.getOffset().getY(), rocketPart.getOffset().getZ());

        world.addImportantParticle(exhaustParticle, true, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
    }

    public static final Codec<EngineComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("power").forGetter(EngineComponent::getPower),
                    ParticleTypes.TYPE_CODEC.fieldOf("exhaustParticle").forGetter(EngineComponent::getExhaustparticle)
            ).apply(builder, EngineComponent::new));
}