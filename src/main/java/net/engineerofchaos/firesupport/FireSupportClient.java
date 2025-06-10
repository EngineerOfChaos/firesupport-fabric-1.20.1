package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.render.BulletEntityRenderer;
import net.engineerofchaos.firesupport.entity.render.ModModelLayers;
import net.engineerofchaos.firesupport.entity.render.ModelBullet;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.item.custom.TestShellItem;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.joml.Vector3f;

public class FireSupportClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAMAGED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE, RenderLayer.getTranslucent());

        ColorProviderRegistry.ITEM.register(TestShellItem::getColour, ModItems.TEST_SHELL);

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BULLET, ModelBullet::getTexturedModelData);
        //EntityRendererRegistry.register(ModEntities.BULLET_THROWN, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BULLET_THROWN, BulletEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(FireSupportNetworkingConstants.BULLET_VELOCITY_PACKET_ID, ((client, handler, buf, responseSender) -> {
            Vector3f velocity = buf.readVector3f();
            int entityID = buf.readInt();

            client.execute( () -> {
                if (client.world != null) {
                    BulletEntity targetBullet = (BulletEntity) client.world.getEntityById(entityID);
                    if (targetBullet != null){
                        targetBullet.setVelocityClient(velocity.x, velocity.y, velocity.z);
                        targetBullet.setVelocity(velocity.x, velocity.y, velocity.z);
                    }
                }
            } );


        }));



    }
}
