package net.engineerofchaos.firesupport.block;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.block.custom.BarbedWireBlock;
import net.engineerofchaos.firesupport.block.custom.DamagedGlassBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
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
    public static final Block ARMOUR = registerBlock("armour",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block ARMOUR_STAIRS = registerBlock("armour_stairs",
            new StairsBlock(ModBlocks.ARMOUR.getDefaultState(), FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
    public static final Block ARMOUR_SLAB = registerBlock("armour_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block ARMOUR_BUTTON = registerBlock("armour_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON, 10, false));
    public static final Block ARMOUR_PRESSURE_PLATE = registerBlock("armour_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));

    public static final Block ARMOUR_FENCE = registerBlock("armour_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));
    public static final Block ARMOUR_FENCE_GATE = registerBlock("armour_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), WoodType.ACACIA));
    public static final Block ARMOUR_WALL = registerBlock("armour_wall",
            new WallBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block ARMOUR_DOOR = registerBlock("armour_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));
    public static final Block ARMOUR_TRAPDOOR = registerBlock("armour_trapdoor",
            new TrapdoorBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), BlockSetType.IRON));

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
