package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class ShellComponents {

    public static final ShellComponent HIGH_EXPLOSIVE = register("high_explosive",
            new ShellComponent(Arrays.asList(1F, 1F, 1F, 1F, 1F, 1F), 13874199, 2));

    public static final ShellComponent SOLID_AP = register("solid_ap",
            new ShellComponent(Arrays.asList(1.1F, 1.5F, 1.5F, 1F, 0.5F, 1F), 1447447, 1));

    public static final ShellComponent SOLID_SAP = register("solid_sap",
            new ShellComponent(Arrays.asList(1.1F, 1.3F, 1.3F, 1F, 0.7F, 1F), 6225920, 1));

    private static ShellComponent register(String name, ShellComponent component) {
        return Registry.register(ModRegistries.SHELL_COMPONENT, new Identifier(FireSupport.MOD_ID, name), component);
    }

    public static void registerShellComponents(){
        FireSupport.LOGGER.info("Registering shell components for " + FireSupport.MOD_ID);
    }

}
