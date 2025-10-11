package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntityOld;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class BulletEntityRenderer<T extends BulletEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(FireSupport.MOD_ID, "textures/entity/bullet_trail_small.png");
    private static final RenderLayer LAYER = RenderLayer.getBeaconBeam(TEXTURE, false);

    public BulletEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, 15728640);
        if (entity.getVelocity() != Vec3d.ZERO) {

            // change these to be defined by entity data later!
            float width = 0.03F;
            float length = entity.getTracerLength(tickDelta);

            //float length = (float) (entity.getVelocity().length() * 2.25);

            matrices.push();

            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(entity.getYaw()));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));

            //matrices.scale(0.1F, 0.1F, 0.1F);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);

            MatrixStack.Entry entry = matrices.peek();
            Matrix4f posMatrix = entry.getPositionMatrix();
            Matrix3f normMatrix = entry.getNormalMatrix();

            renderBehind(vertexConsumer, posMatrix, normMatrix, width, length, entity);

            matrices.pop();
        }

    }

    @Override
    protected int getBlockLight(BulletEntity entity, BlockPos pos) {
        return 15;
    }

    protected int getSkyLight(BulletEntity entity, BlockPos pos) {
        return 15;
    }

    public Identifier getTexture(BulletEntity entity) {
        return TEXTURE;
    }

    private static void vertex(
            VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z,
            int red, int green, int blue, float u, float v, float yOffset
    ) {
        vertexConsumer.vertex(positionMatrix, x, y + yOffset, z)
                .color(red, green, blue, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }

    private void renderBehind(VertexConsumer vertexConsumer, Matrix4f posMatrix, Matrix3f normMatrix, float width, float length, T entity) {
        float yOffset = entity.getDimensions(null).height / 2F;

        // up cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, -length, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, -length, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 1, 0, yOffset);
        // up ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, -length, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, -length, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 1, 0, yOffset);

        // down cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, -length, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, -length, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 1, 0, yOffset);
        // down ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, -length, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, -length, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 1, 0, yOffset);
    }

    private void renderAhead(VertexConsumer vertexConsumer, Matrix4f posMatrix, Matrix3f normMatrix, float width, float length, T entity) {
        float yOffset = entity.getDimensions(null).height / 0.2F;
        //float yOffset = 1F;

        // up cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, length, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, length, 255, 255, 255, 1, 0, yOffset);
        // up ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, length, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, length, 255, 255, 255, 1, 0, yOffset);

        // down cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, length, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, length, 255, 255, 255, 1, 0, yOffset);
        // down ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 1, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 0, 1, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, length, 255, 255, 255, 0, 0, yOffset);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, length, 255, 255, 255, 1, 0, yOffset);
    }

}
