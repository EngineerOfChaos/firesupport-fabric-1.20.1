package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class BulletEntityCustomRenderer<T extends BulletEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(FireSupport.MOD_ID, "textures/entity/bullet_trail_small.png");
    private static final RenderLayer LAYER = RenderLayer.getBeaconBeam(TEXTURE, false);

    public BulletEntityCustomRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }



    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, 15728640);

        // change these to be defined by entity data later!
        float width = 0.3F;
        float length = 90F;

        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));

        matrices.scale(0.1F, 0.1F, 0.1F);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f posMatrix = entry.getPositionMatrix();
        Matrix3f normMatrix = entry.getNormalMatrix();

        // up cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 1, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 0, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, length, 255, 255, 255, 0, 0);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, length, 255, 255, 255, 1, 0);
        // up ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, 0, 255, 255, 255, 1, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, 0, 255, 255, 255, 0, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, width, width, length, 255, 255, 255, 0, 0);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, -width, length, 255, 255, 255, 1, 0);

        // down cw
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 1, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 0, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, length, 255, 255, 255, 0, 0);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, length, 255, 255, 255, 1, 0);
        // down ccw
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, 0, 255, 255, 255, 1, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, 0, 255, 255, 255, 0, 1);
        vertex(vertexConsumer, posMatrix, normMatrix, width, -width, length, 255, 255, 255, 0, 0);
        vertex(vertexConsumer, posMatrix, normMatrix, -width, width, length, 255, 255, 255, 1, 0);


        matrices.pop();

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
            VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v
    ) {
        vertexConsumer.vertex(positionMatrix, x, y, z)
                .color(red, green, blue, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .next();
    }

}
