package net.engineerofchaos.firesupport.screen;

import net.engineerofchaos.firesupport.FireSupport;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<BasicDirectionalTurretScreenHandler> BASIC_DIRECTIONAL_TURRET_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(FireSupport.MOD_ID, "directional_turret_screen"),
                    new ExtendedScreenHandlerType<>(BasicDirectionalTurretScreenHandler::new));

    public static void registerScreenHandlers() {
        FireSupport.LOGGER.info("Registering Screen Handlers for " + FireSupport.MOD_ID);
    }
}
