package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.engineerofchaos.firesupport.entity.render.model.TestTurretEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TestTurretEntityRenderer<T extends RideableTurretEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(FireSupport.MOD_ID, "textures/entity/test_turret.png");
    protected final TestTurretEntityModel model;

    public TestTurretEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TestTurretEntityModel(ctx.getPart(ModModelLayers.TEST_TURRET));
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        model.setAngles(entity, 0, 0, 0,  (float) (entity.getYaw() * Math.PI/180), (float) (entity.getPitch() * Math.PI/180));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
