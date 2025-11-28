package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.engineerofchaos.firesupport.entity.render.model.TestTurretEntityModel;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Arrangements;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.engineerofchaos.firesupport.turret.client.AutoloaderModels;
import net.engineerofchaos.firesupport.turret.client.model.autoloader.AC20MShortRecoilModel;
import net.engineerofchaos.firesupport.turret.client.model.autoloader.AutoloaderModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestTurretEntityRenderer<T extends RideableTurretEntity> extends EntityRenderer<T> {
    private static final Identifier TEXTURE = new Identifier(FireSupport.MOD_ID, "textures/entity/test_turret.png");
    protected final TestTurretEntityModel model;
    private final HashMap<Autoloader, AutoloaderModel> autoloaderModels = new HashMap<>();

    public TestTurretEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TestTurretEntityModel(ctx.getPart(ModModelLayers.TEST_TURRET));

        //this.autoloaderModels.addAll(AutoloaderModels.loadAllModels(ctx));
        AutoloaderModels.loadAllModels(autoloaderModels, ctx);
    }

    @Override
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);



        Autoloader autoloader = entity.getAutoloader();
        Arrangement arrangement = entity.getArrangement();
        if (autoloader != null && arrangement != null) {
            //FireSupport.LOGGER.info("passed null checks");
            List<Arrangements.Pivot> pivots = arrangement.getPivots();
            EntityModelLayer autoloaderModelLayer = AutoloaderModels.retrieveModelLayer(autoloader);
            AutoloaderModel currentModel = autoloaderModels.get(autoloader);

            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(currentModel.getLayer(currentModel.getTexture()));

            matrices.push();
            float headYaw = (float) (Math.toRadians(entity.getYaw(tickDelta)));
            float headPitch = (float) (Math.toRadians(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));

            for (Arrangements.Pivot pivot : pivots) {
                matrices.push();
                //offset
                matrices.translate(pivot.pivotPoint.x, pivot.pivotPoint.y, pivot.pivotPoint.z);

                currentModel.setAngles(entity, 0, 0, 0, headYaw, headPitch);

                Vector3f offset = new Vector3f(pivot.offset);
                offset.rotateX(headPitch);
                offset.rotateY(-headYaw);
                matrices.translate(offset.x, offset.y, offset.z);

                currentModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
                matrices.pop();
            }
//        for (Arrangements.Pivot pivot : pivots) {
//            autoloaderModel.render(pivot, matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
//        }


            //old code
//        model.setAngles(entity, 0, 0, 0,
//                (float) (Math.toRadians(entity.getYaw(tickDelta))),
//                (float) (Math.toRadians(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()))));

            //VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity)));

            //model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

            matrices.pop();
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
