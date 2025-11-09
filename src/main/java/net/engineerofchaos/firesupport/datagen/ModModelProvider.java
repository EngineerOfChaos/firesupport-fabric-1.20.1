package net.engineerofchaos.firesupport.datagen;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

import java.util.Arrays;

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

        blockStateModelGenerator.registerSimpleState(ModBlocks.DIRECTIONAL_TURRET);
        blockStateModelGenerator.registerSimpleState(ModBlocks.TURRET_RING);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SANDBAG, Models.GENERATED);
        itemModelGenerator.register(ModItems.BRICK, Models.GENERATED);
        itemModelGenerator.register(ModItems.CASING, Models.GENERATED);
        itemModelGenerator.register(ModItems.TURRET_ITEM, Models.GENERATED);

        for (int cal : Arrays.asList(20, 30, 40)) {
            for (int caseLength : Arrays.asList(0, 1)) {
                String caseLengthString = caseLength == 0 ? "short" : "long";
                // generate normal model file
                registerThreeLayerShellModel(fsID("item/shell_%d_%s".formatted(cal, caseLengthString)),
                        fsID("item/shell_primary_%s".formatted(caseLengthString)),
                        fsID("item/shell_secondary_%s".formatted(caseLengthString)),
                        fsID("item/casing_%d_%s".formatted(cal, caseLengthString)),
                        itemModelGenerator);
                for (String altModel : Arrays.asList("sabot", "heat")) {
                    // generate alternate model file
                }
            }
        }
    }

    public void registerThreeLayerShellModel(Identifier id, Identifier layer0, Identifier layer1, Identifier layer2,
                                             ItemModelGenerator itemModelGenerator) {
        Models.GENERATED_THREE_LAYERS.upload(id, TextureMap.layered(layer0, layer1, layer2), itemModelGenerator.writer);
    }

    private Identifier fsID(String path) {
        return new Identifier("firesupport", path);
    }
}
