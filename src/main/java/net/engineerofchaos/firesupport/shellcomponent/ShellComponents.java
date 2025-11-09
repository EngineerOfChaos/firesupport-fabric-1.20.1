package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShellComponents {

    public static final HashMap<ShellComponent, List<ShellComponent>> EXCLUSIVITY_MAP = new HashMap<>();
    public static final HashMap<ShellComponent, List<ShellComponent>> DEPENDENCY_MAP = new HashMap<>();

    public static final ShellComponent HIGH_EXPLOSIVE = register("high_explosive",
            new HighExplosiveShellComponent(Arrays.asList(1F, 1F, 1F, 1F, 1F, 1F), 13874199, 2));

    public static final ShellComponent SOLID_AP = register("solid_ap",
            new ShellComponent(Arrays.asList(1.1F, 1.5F, 1.5F, 1F, 0.5F, 1F), 1447447, 1));

    public static final ShellComponent SOLID_SAP = register("solid_sap",
            new ShellComponent(Arrays.asList(1.1F, 1.3F, 1.3F, 1F, 0.7F, 1F), 6225920, 1));

    public static final ShellComponent AP_CORE = register("ap_core",
            new ShellComponent(ShellComponentUtil
                    .createMultipliers(1f, 0.5f, 2f, 1f, 0.5f, 0.8f),
                    7303023, 2));

    public static final ShellComponent TIMED_FUSE = register("timed_fuse",
            new TimedFuseShellComponent(Arrays.asList(1F, 1F, 1F, 1F, 1F, 1F), 0, 255));

    public static final ShellComponent SABOT = register("sabot",
            new ShellComponent(ShellComponentUtil
                    .createMultipliers(2f, 0.2f, 10f, 1f, 0.1f, 0.2f),
                    1776412, 14211288, 0));

    public static final ShellComponent PROXY_FUSE = register("proxy_fuse",
            new ProxyFuseShellComponent(Arrays.asList(1F, 1F, 1F, 1F, 1F, 1F), 0, 255));

    private static ShellComponent register(String name, ShellComponent component) {
        return Registry.register(ModRegistries.SHELL_COMPONENT, new Identifier(FireSupport.MOD_ID, name), component);
    }

    public static void registerShellComponents(){
        FireSupport.LOGGER.info("Registering shell components for " + FireSupport.MOD_ID);
    }

    public static void registerExclusivities() {
        EXCLUSIVITY_MAP.put(HIGH_EXPLOSIVE, List.of(AP_CORE));
        EXCLUSIVITY_MAP.put(SOLID_AP, List.of(SOLID_SAP));
        EXCLUSIVITY_MAP.put(SOLID_SAP, List.of(SOLID_AP));
        EXCLUSIVITY_MAP.put(AP_CORE, List.of(HIGH_EXPLOSIVE, TIMED_FUSE, PROXY_FUSE));
        EXCLUSIVITY_MAP.put(TIMED_FUSE, List.of(AP_CORE));
        EXCLUSIVITY_MAP.put(SABOT, List.of(HIGH_EXPLOSIVE, SOLID_AP, SOLID_SAP, AP_CORE, TIMED_FUSE, PROXY_FUSE));
        EXCLUSIVITY_MAP.put(PROXY_FUSE, List.of(AP_CORE));
    }

    // component needs this list of things in order to work
    public static void registerDependencies() {
        DEPENDENCY_MAP.put(AP_CORE, List.of(SOLID_AP));
    }

}
