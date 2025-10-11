package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.render.*;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.item.ModModelPredicateProviders;
import net.engineerofchaos.firesupport.item.custom.TestShellItem;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.engineerofchaos.firesupport.screen.BasicDirectionalTurretScreen;
import net.engineerofchaos.firesupport.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class FireSupportClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAMAGED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE, RenderLayer.getTranslucent());

        ColorProviderRegistry.ITEM.register(TestShellItem::getColour, ModItems.TEST_SHELL);

        ModModelPredicateProviders.registerModelPredicateProviders();

        //EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BULLET, BulletModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.BULLET_THROWN, BulletEntityCustomRenderer::new);
        EntityRendererRegistry.register(ModEntities.BULLET, BulletEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID,
                ((client, handler, buf, responseSender) -> {
            Vector3f velocity = buf.readVector3f();
            Vector3f launchPos = buf.readVector3f();
            int entityID = buf.readInt();

            client.execute( () -> {
                if (client.world != null) {
                    BulletEntity targetBullet = (BulletEntity) client.world.getEntityById(entityID);
                    if (targetBullet != null){
                        targetBullet.setVelocityClient(velocity.x, velocity.y, velocity.z);
                        targetBullet.setVelocity(velocity.x, velocity.y, velocity.z);
                        targetBullet.setLaunchPos(new Vec3d(launchPos.x, launchPos.y, launchPos.z));
                    }
                }
            } );


        }));

        HandledScreens.register(ModScreenHandlers.BASIC_DIRECTIONAL_TURRET_SCREEN_HANDLER, BasicDirectionalTurretScreen::new);
    }
}
