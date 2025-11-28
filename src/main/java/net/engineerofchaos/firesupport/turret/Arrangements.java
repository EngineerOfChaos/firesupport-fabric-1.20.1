package net.engineerofchaos.firesupport.turret;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class Arrangements {

    public static final Arrangement SINGLE_1X1_TEST = register("single_1x1_test",
            new Arrangement(new Pivot(0, 0.8125f, 0).offset(0, 0.03125f, 0)));
    public static final Arrangement DOUBLE_2X1_TEST = register("double_2x1_test",
            new Arrangement(new Pivot(0, 1, 0).offset(1,0,0),
                    new Pivot(0,1,0).offset(-1,0,0)));
    public static final Arrangement QUAD_2X2_RING = register("quad_2x2_stacked",
            new Arrangement(new Pivot(0,1,0).offset(-0.5f,-0.2f,0),
                    new Pivot(0,1,0).offset(-0.5f,0.2f,0),
                    new Pivot(0,1,0).offset(0.5f,-0.2f,0),
                    new Pivot(0,1,0).offset(0.5f,0.2f,0)));


    private static Arrangement register(String name, Arrangement arrangement) {
        return Registry.register(ModRegistries.ARRANGEMENT, new Identifier(FireSupport.MOD_ID, name), arrangement);
    }

    public static void registerArrangements() {
        FireSupport.LOGGER.info("Registering autoloader arrangements for " + FireSupport.MOD_ID);
    }

    public static class Pivot {

        public final Vector3f pivotPoint;
        public Vector3f offset = null;
        public Ejection shellEject = Ejection.RIGHT;

        public Pivot(float x, float y, float z){
            pivotPoint = new Vector3f(x,y,z);
        }

        public Pivot offset(float dx, float dy, float dz) {
            this.offset = new Vector3f(dx, dy, dz);
            return this;
        }

        public Pivot eject(Ejection ejectDirection) {
            this.shellEject = ejectDirection;
            return this;
        }
    }
}
