package net.engineerofchaos.firesupport.util;

import net.engineerofchaos.firesupport.shellcomponent.ShellComponent;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashMap;
import java.util.List;

public class ShellUtil {

    public static ItemStack buildShellItem(Item baseItem, List<ShellComponent> components, HashMap<Integer,
            Float> componentData, int calibre, int caseLength) {
        ItemStack stack = new ItemStack(baseItem);
        if (!ShellComponentUtil.verifyComponentList(components)) {
            return stack;
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        ShellComponentUtil.addComponentsToNBTCompound(nbtCompound, components, componentData);
        nbtCompound.putInt("calibre", calibre);
        nbtCompound.putInt("caseLength", caseLength);

        return stack;
    }

    public static int getCalibre(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getNbt();
        return nbtCompound != null ? nbtCompound.getInt("calibre") : 0;
    }

    public static int getCaseLength(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getNbt();
        return nbtCompound != null ? nbtCompound.getInt("caseLength") : 0;
    }
}
