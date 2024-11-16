package org.csp.client.registry;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.csp.client.renderer.RocketEntityRenderer;
import org.csp.registry.EntityRegistry;

public class EntityRendererRegistry {

    public static void initializeRegistry() {
        registerRenderer(EntityRegistry.ROCKET_ENTITY, RocketEntityRenderer::new);
    }

    private static <E extends Entity> void registerRenderer(EntityType<E> entityType, EntityRendererFactory<E> rendererFactory) {
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(entityType, rendererFactory);
    }
}
