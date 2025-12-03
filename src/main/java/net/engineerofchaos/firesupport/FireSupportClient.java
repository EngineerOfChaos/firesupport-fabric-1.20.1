package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.engineerofchaos.firesupport.entity.render.*;
import net.engineerofchaos.firesupport.entity.render.model.TestTurretEntityModel;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.item.ModModelPredicateProviders;
import net.engineerofchaos.firesupport.item.custom.TestShellItem;
import net.engineerofchaos.firesupport.network.*;
import net.engineerofchaos.firesupport.screen.BasicDirectionalTurretScreen;
import net.engineerofchaos.firesupport.screen.ModScreenHandlers;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.shell.Multipliers;
import net.engineerofchaos.firesupport.shell.ShellUtil;
import net.engineerofchaos.firesupport.turret.client.AutoloaderModels;
import net.engineerofchaos.firesupport.turret.client.model.autoloader.AC20MShortRecoilModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class FireSupportClient implements ClientModInitializer {

    private static KeyBinding FIRE_KEY;

    @Override
    public void onInitializeClient() {
        //FIRE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.firesupport.firekey",
        //        InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, "category.firesupport.test"));

        //ClientTickEvents.END_CLIENT_TICK.register(FireSupportClient::onClientTick);

        ClientPreAttackCallback.EVENT.register(((client, player, clickCount) -> {
            if (player.getRootVehicle() instanceof RideableTurretEntity turret) {
                //player.sendMessage(Text.literal("pew"));
                NetworkUtil.send(new C2SFireCommandPacket());
            }
            return false;
        }));

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAMAGED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE, RenderLayer.getTranslucent());

        ColorProviderRegistry.ITEM.register(TestShellItem::getColour, ModItems.TEST_SHELL);

        ModModelPredicateProviders.registerModelPredicateProviders();

        AutoloaderModels.initAutoloaderModels();

        //EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BULLET, BulletModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.TEST_TURRET, TestTurretEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.AC_20M_SHORT_RECOIL, AC20MShortRecoilModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.BULLET_THROWN, BulletEntityCustomRenderer::new);
        EntityRendererRegistry.register(ModEntities.BULLET, BulletEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.RIDEABLE_TURRET, TestTurretEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID,
                (S2CBulletVelocityPacket::handle));
        ClientPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.S2C_TURRET_TARGET_ANGLE,
                (S2CTurretTargetAnglePacket::handle));
        ClientPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.S2C_TURRET_SETUP_PACKET_ID,
                (S2CTurretSetupPacket::handle));



        HandledScreens.register(ModScreenHandlers.BASIC_DIRECTIONAL_TURRET_SCREEN_HANDLER, BasicDirectionalTurretScreen::new);
    }

    public static void onClientTick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (mc.player.getRootVehicle() instanceof RideableTurretEntity turret) {
            if (FIRE_KEY.isPressed()) {
                mc.player.sendMessage(Text.literal("pew"));
            }
        }


    }
}
