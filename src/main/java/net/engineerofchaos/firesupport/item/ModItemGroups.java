package net.engineerofchaos.firesupport.item;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.shell.ShellComponents;
import net.engineerofchaos.firesupport.shell.ShellUtil;
import net.engineerofchaos.firesupport.turret.Arrangements;
import net.engineerofchaos.firesupport.turret.Autoloaders;
import net.engineerofchaos.firesupport.turret.TurretBuilderUtil;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;

public class ModItemGroups {
    public static final ItemGroup SANDBAG_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(FireSupport.MOD_ID, "sandbag"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.sandbag"))
                    .icon(() -> new ItemStack(ModItems.SANDBAG)).entries((displayContext, entries) -> {

                        entries.add(ModItems.SANDBAG);
                        entries.add(ModItems.BRICK);
                        //entries.add(ModItems.TEST_SHELL);

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SOLID_AP, ShellComponents.HIGH_EXPLOSIVE),
                                null, 30, CaseLength.LONG, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.HIGH_EXPLOSIVE),
                                null, 40, CaseLength.SHORT, 1.8f));
                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.HIGH_EXPLOSIVE),
                                null, 40, CaseLength.SHORT, 0f));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SOLID_SAP, ShellComponents.HIGH_EXPLOSIVE),
                                null, 30, CaseLength.LONG, 0));

                        HashMap<String, Float> testData = new HashMap<>();
                        testData.put(ShellComponents.TIMED_FUSE.getID(), 10F);

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.HIGH_EXPLOSIVE, ShellComponents.TIMED_FUSE),
                                testData, 40, CaseLength.LONG, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.HIGH_EXPLOSIVE, ShellComponents.PROXY_FUSE),
                                null, 40, CaseLength.LONG, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SABOT),
                                null, 30, CaseLength.LONG, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SOLID_AP),
                                null, 8, CaseLength.SHORT, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SOLID_AP),
                                null, 6, CaseLength.LONG, 0));

                        entries.add(ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                                Arrays.asList(ShellComponents.SABOT),
                                null, 120, CaseLength.LONG, 0));

                        entries.add(ModItems.CASING);

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

                        entries.add(ModBlocks.DIRECTIONAL_TURRET);
                        entries.add(ModBlocks.TURRET_RING);

                        entries.add(TurretBuilderUtil.buildTurretItem(ModItems.TURRET_ITEM, Arrangements.SINGLE_1X1_TEST,
                                Autoloaders.AC_20M_SHORT_RECOIL));
                        entries.add(TurretBuilderUtil.buildTurretItem(ModItems.TURRET_ITEM, Arrangements.DOUBLE_2X1_TEST,
                                Autoloaders.AC_20M_SHORT_RECOIL));
                        entries.add(TurretBuilderUtil.buildTurretItem(ModItems.TURRET_ITEM, Arrangements.QUAD_2X2_RING,
                                Autoloaders.AC_20M_SHORT_RECOIL));
                    }).build());

    public static void registerItemGroups() {
        FireSupport.LOGGER.info("Registering item groups for " + FireSupport.MOD_ID);
    }
}
