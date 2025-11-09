// Made with Blockbench 5.0.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.engineerofchaos.firesupport.entity.render.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class TestTurretEntityModel extends EntityModel<Entity> {
	private final ModelPart head;
	private final ModelPart bb_main;
	public TestTurretEntityModel(ModelPart root) {
		this.head = root.getChild("head");
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -8.0F, 4.0F, 4.0F, 14.0F, new Dilation(0.0F))
		.uv(36, 12).cuboid(-6.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(40, 39).cuboid(-1.5F, -1.5F, 6.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 18).cuboid(-0.5F, 0.0F, 10.0F, 1.0F, 1.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.0F, 0.0F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(36, 0).cuboid(-7.0F, 0.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(16, 33).cuboid(6.0F, 15.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(16, 39).cuboid(5.0F, 0.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(30, 18).cuboid(-8.0F, 0.0F, -3.0F, 2.0F, 15.0F, 6.0F, new Dilation(0.0F))
		.uv(46, 16).cuboid(-8.0F, 15.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 33).cuboid(6.0F, 0.0F, -3.0F, 2.0F, 15.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		head.yaw = -netHeadYaw;
		bb_main.yaw = -netHeadYaw;
		head.pitch = headPitch;
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}