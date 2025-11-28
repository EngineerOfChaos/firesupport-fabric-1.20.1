package net.engineerofchaos.firesupport.turret;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class Autoloaders {

    public static final Autoloader AC_20M_SHORT_RECOIL = register("ac_20m_short_recoil",
            new Autoloader(20, CaseLength.MED, 300, 1.5f,
                    new Vec3d(0.25, 0.25, 1), 1));

    private static Autoloader register(String name, Autoloader autoloader) {
        return Registry.register(ModRegistries.AUTOLOADER, new Identifier(FireSupport.MOD_ID, name), autoloader);
    }

    public static void registerAutoloaders() {
        FireSupport.LOGGER.info("Registering autoloaders for " + FireSupport.MOD_ID);
    }

}
