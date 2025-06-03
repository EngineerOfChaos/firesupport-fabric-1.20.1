package net.engineerofchaos.firesupport.block;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.block.custom.BarbedWireBlock;
import net.engineerofchaos.firesupport.block.custom.DamagedGlassBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block DAMAGED_GLASS = registerBlock("damaged_glass",
            new DamagedGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
    public static final Block BARBED_WIRE = registerBlock("barbed_wire",
            new BarbedWireBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)));
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(FireSupport.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(FireSupport.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        FireSupport.LOGGER.info("Registering blocks for " + FireSupport.MOD_ID);
    }
}
