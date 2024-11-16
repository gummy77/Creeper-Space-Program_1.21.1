package org.csp.client.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.csp.CspMain;
import org.csp.component.RocketPart;
import org.csp.component.RocketStage;
import org.csp.entity.RocketEntity;

import java.util.ArrayList;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    private final BlockRenderManager blockRenderManager;

    public RocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(RocketEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if(entity.getRocket() != null) {
            matrices.push();
            matrices.translate(-0.5f, 0f, -0.5f);

            ArrayList<RocketStage> stages = entity.getRocket().getStages();
            for (RocketStage stage : stages) {
                ArrayList<RocketPart> parts = stage.getParts();
                for (RocketPart part : parts) {
                    matrices.push();
                    matrices.translate(part.getOffset().getX(), part.getOffset().getY(), part.getOffset().getZ());
                    blockRenderManager.renderBlockAsEntity(part.getBlock(), matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
                    matrices.pop();
                }
            }
            matrices.pop();
        }
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return Identifier.of(CspMain.MOD_ID, "textures/entity/rocket.png");
    }
}
