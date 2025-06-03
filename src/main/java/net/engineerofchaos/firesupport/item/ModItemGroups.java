package net.engineerofchaos.firesupport.item;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup SANDBAG_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(FireSupport.MOD_ID, "sandbag"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.sandbag"))
                    .icon(() -> new ItemStack(ModItems.SANDBAG)).entries((displayContext, entries) -> {

                        entries.add(ModItems.SANDBAG);
                        entries.add(ModItems.BRICK);

                        entries.add(ModBlocks.DAMAGED_GLASS);
                        entries.add(ModBlocks.BARBED_WIRE);
                        entries.add(ModBlocks.ARMOUR);

                        entries.add(ModBlocks.ARMOUR_STAIRS);
                        entries.add(ModBlocks.ARMOUR_SLAB);
                        entries.add(ModBlocks.ARMOUR_FENCE);
                        entries.add(ModBlocks.ARMOUR_FENCE_GATE);
                        entries.add(ModBlocks.ARMOUR_WALL);
                        entries.add(ModBlocks.ARMOUR_BUTTON);
                        entries.add(ModBlocks.ARMOUR_PRESSURE_PLATE);
                        entries.add(ModBlocks.ARMOUR_DOOR);
                        entries.add(ModBlocks.ARMOUR_TRAPDOOR);

                    }).build());

    public static void registerItemGroups() {
        FireSupport.LOGGER.info("Registering item groups for " + FireSupport.MOD_ID);
    }
}
