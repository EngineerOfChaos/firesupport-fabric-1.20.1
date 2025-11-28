package net.engineerofchaos.firesupport.util;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.shell.ShellComponent;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistries {

    public static final RegistryKey<Registry<ShellComponent>> SHELL_COMPONENT_REGKEY =
            RegistryKey.ofRegistry(new Identifier(FireSupport.MOD_ID, "shell_component"));

    public static final Registry<ShellComponent> SHELL_COMPONENT =
            FabricRegistryBuilder.createSimple(SHELL_COMPONENT_REGKEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    public static final RegistryKey<Registry<Autoloader>> AUTOLOADER_REGKEY =
            RegistryKey.ofRegistry(new Identifier(FireSupport.MOD_ID, "autoloader"));

    public static final Registry<Autoloader> AUTOLOADER =
            FabricRegistryBuilder.createSimple(AUTOLOADER_REGKEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    public static final RegistryKey<Registry<Arrangement>> ARRANGEMENT_REGKEY =
            RegistryKey.ofRegistry(new Identifier(FireSupport.MOD_ID, "arrangement"));

    public static final Registry<Arrangement> ARRANGEMENT =
            FabricRegistryBuilder.createSimple(ARRANGEMENT_REGKEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    public static void registerModRegistries() {
        FireSupport.LOGGER.info("Registering registries for " + FireSupport.MOD_ID);
    }

}
