package net.engineerofchaos.firesupport.entity.render;

import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer TEST_TURRET = create("test_turret", "main");
//            new EntityModelLayer(new Identifier(FireSupport.MOD_ID, "test_turret"), "main");
    public static final EntityModelLayer AC_20M_SHORT_RECOIL = create("ac_20m_short_recoil", "main");

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(new Identifier(FireSupport.MOD_ID, id), layer);
    }
}
