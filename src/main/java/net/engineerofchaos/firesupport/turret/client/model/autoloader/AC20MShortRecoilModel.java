package net.engineerofchaos.firesupport.turret.client.model.autoloader;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class AC20MShortRecoilModel extends AutoloaderModel<Autoloader> {

    public AC20MShortRecoilModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 14.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(FireSupport.MOD_ID, "textures/entity/bullet.png");
    }

}
