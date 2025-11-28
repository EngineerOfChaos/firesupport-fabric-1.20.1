package net.engineerofchaos.firesupport.shell;

import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.List;

public class ShellUtil {

    public static ItemStack buildShellItem(Item baseItem, List<ShellComponent> components, HashMap<String,
            Float> componentData, int calibre, CaseLength caseLength) {
        ItemStack stack = new ItemStack(baseItem);
        if (!ShellComponentUtil.verifyComponentList(components)) {
            return stack;
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        ShellComponentUtil.addComponentsToNBTCompound(nbtCompound, components, componentData);
        nbtCompound.putInt("calibre", calibre);
        nbtCompound.putInt("caseLength", caseLength.ordinal());

        return stack;
    }

    public static int getCalibre(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getNbt();
        return nbtCompound != null ? nbtCompound.getInt("calibre") : 0;
    }

    public static CaseLength getCaseLength(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getNbt();
        if (nbtCompound != null) {
            return CaseLength.getCaseLength(nbtCompound.getInt("caseLength"));
        }
        return CaseLength.SHORT;
    }

    public static Multipliers sumAllMultipliers(ItemStack itemStack) {
        HashMap<String, Float> additionalData = new HashMap<>();
        List<ShellComponent> components = ShellComponentUtil.getComponents(itemStack, additionalData);

        return sumAllMultipliers(components, additionalData);
    }

    public static Multipliers sumAllMultipliers(List<ShellComponent> components, HashMap<String, Float> additionalData) {
        Multipliers totalMultipliers = new Multipliers();
        for (ShellComponent component : components) {
            if (component instanceof VariableMultiplierShellComponent) {
                totalMultipliers = totalMultipliers.combineWith(
                        ((VariableMultiplierShellComponent) component).getVariableMultipliers(additionalData));
            }
            totalMultipliers = totalMultipliers.combineWith(component.getMultipliers());
        }
        return totalMultipliers;
    }

    /**
     * Calculate the launch speed for the stack with full propellant burn.
     * Ensure to factor in barrel length after this.
     */
    public static float calculateLaunchSpeed(ItemStack stack) {
        Multipliers totalMultipliers = sumAllMultipliers(stack);
        float caseVolume = getCaseLength(stack).getCasingVolume(getCalibre(stack)) - 0; // Put inset here when implemented
        float launchEnergy = caseVolume * FireSupport.POWDER_ENERGY_DENSITY;
        float shellVolume = getShellVolume(stack) * totalMultipliers.volume();
        return MathHelper.sqrt((2 * launchEnergy)/shellVolume);
    }

    private static float getShellVolume(ItemStack stack) {
        CaseLength caseLength = getCaseLength(stack);
        return caseLength.getBulletVolume(getCalibre(stack));
    }
}
