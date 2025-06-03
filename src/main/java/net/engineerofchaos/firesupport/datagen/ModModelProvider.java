package net.engineerofchaos.firesupport.datagen;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DAMAGED_GLASS);
        BlockStateModelGenerator.BlockTexturePool armourPool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.ARMOUR);

        armourPool.stairs(ModBlocks.ARMOUR_STAIRS);
        armourPool.slab(ModBlocks.ARMOUR_SLAB);
        armourPool.button(ModBlocks.ARMOUR_BUTTON);
        armourPool.pressurePlate(ModBlocks.ARMOUR_PRESSURE_PLATE);
        armourPool.fence(ModBlocks.ARMOUR_FENCE);
        armourPool.fenceGate(ModBlocks.ARMOUR_FENCE_GATE);
        armourPool.wall(ModBlocks.ARMOUR_WALL);

        blockStateModelGenerator.registerDoor(ModBlocks.ARMOUR_DOOR);
        blockStateModelGenerator.registerTrapdoor(ModBlocks.ARMOUR_TRAPDOOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SANDBAG, Models.GENERATED);
        itemModelGenerator.register(ModItems.BRICK, Models.GENERATED);
    }
}
