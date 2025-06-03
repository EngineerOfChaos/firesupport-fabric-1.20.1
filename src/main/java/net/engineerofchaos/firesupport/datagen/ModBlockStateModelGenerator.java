package net.engineerofchaos.firesupport.datagen;

import com.google.gson.JsonElement;
import net.engineerofchaos.firesupport.block.ModBlocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockStateModelGenerator extends BlockStateModelGenerator {
    public ModBlockStateModelGenerator(Consumer<BlockStateSupplier> blockStateCollector, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector, Consumer<Item> simpleItemModelExemptionCollector) {
        super(blockStateCollector, modelCollector, simpleItemModelExemptionCollector);
    }

//    public void registerBarbedWire() {
//        TextureMap textureMap = TextureMap.texture(ModBlocks.BARBED_WIRE);
//        Identifier identifier =
//    }
}
