package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer BULLET =
            new EntityModelLayer(new Identifier(FireSupport.MOD_ID, "bullet"), "main");

    public static final EntityModelLayer TEST_TURRET =
            new EntityModelLayer(new Identifier(FireSupport.MOD_ID, "test_turret"), "main");
}
