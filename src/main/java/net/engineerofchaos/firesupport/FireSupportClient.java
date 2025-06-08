package net.engineerofchaos.firesupport;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.item.custom.TestShellItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public class FireSupportClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAMAGED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BARBED_WIRE, RenderLayer.getTranslucent());

        ColorProviderRegistry.ITEM.register(TestShellItem::getColour, ModItems.TEST_SHELL);
    }
}
