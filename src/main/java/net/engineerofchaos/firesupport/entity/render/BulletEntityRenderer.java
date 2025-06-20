package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;


public class BulletEntityRenderer<T extends BulletEntity> extends EntityRenderer<BulletEntity> {
    private static final Identifier TEXTURE = new Identifier(FireSupport.MOD_ID, "textures/entity/bullet_trail_outline.png");
    private final BulletModel<T> model;

    public BulletEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BulletModel<>(ctx.getPart(ModModelLayers.BULLET));
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));

        matrices.translate(0, -1, 0);

        this.model.setAngles(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        //VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.model.render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, 15728640);

    }

    @Override
    protected int getBlockLight(BulletEntity entity, BlockPos pos) {
        return 15;
    }

//    protected int getSkyLight(BulletEntity entity, BlockPos pos) {
//        return 15;
//    }

    public Identifier getTexture(BulletEntity entity) {
        return TEXTURE;
    }

}
