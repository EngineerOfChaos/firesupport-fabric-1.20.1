package net.engineerofchaos.firesupport.turret.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;

@FunctionalInterface
public interface ModelFactory {
    EntityModel create(ModelPart root);
}
