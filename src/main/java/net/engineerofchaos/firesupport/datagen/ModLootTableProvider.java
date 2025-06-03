package net.engineerofchaos.firesupport.datagen;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.BARBED_WIRE);
        // Look at BlockLootTableGenerator class for others
        addDrop(ModBlocks.ARMOUR);
        addDrop(ModBlocks.ARMOUR_STAIRS);
        addDrop(ModBlocks.ARMOUR_FENCE);
        addDrop(ModBlocks.ARMOUR_FENCE_GATE);
        addDrop(ModBlocks.ARMOUR_BUTTON);
        addDrop(ModBlocks.ARMOUR_WALL);
        addDrop(ModBlocks.ARMOUR_TRAPDOOR);
        addDrop(ModBlocks.ARMOUR_PRESSURE_PLATE);

        addDrop(ModBlocks.ARMOUR_DOOR, doorDrops(ModBlocks.ARMOUR_DOOR));
        addDrop(ModBlocks.ARMOUR_SLAB, slabDrops(ModBlocks.ARMOUR_SLAB));
    }
}
