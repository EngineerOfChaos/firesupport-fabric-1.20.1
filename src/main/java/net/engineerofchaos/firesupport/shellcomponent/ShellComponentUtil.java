package net.engineerofchaos.firesupport.shellcomponent;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import java.util.List;


public class ShellComponentUtil {

    public static List<ShellComponent> getComponents(ItemStack stack) {
        return getComponents(stack.getNbt());
    }

    public static List<ShellComponent> getComponents(@Nullable NbtCompound nbt) {
        List<ShellComponent> list = Lists.<ShellComponent>newArrayList();
        getComponents(nbt, list);
        return list;
    }

    public static ItemStack addShellComponents(ItemStack stack, List<ShellComponent> components) {
        if (components.isEmpty()) {
            return stack;
        } else {
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            int[] componentIDsArray = new int[components.size()];

            for (int i = 0; i < components.size(); i++) {
                componentIDsArray[i] = ShellComponent.getRawID(components.get(i));
            }

            nbtCompound.putIntArray("ShellComponents", componentIDsArray);
            stack.setNbt(nbtCompound);
            return stack;
        }
    }

    public static void getComponents(@Nullable NbtCompound nbt, List<ShellComponent> list) {
        if (nbt != null && nbt.contains("ShellComponents", NbtElement.INT_ARRAY_TYPE)) {
            int[] nbtList = nbt.getIntArray("ShellComponents");

            for (int i = 0; i < nbtList.length; i++) {

                int nextComponentID = nbtList[i];
                ShellComponent component = ShellComponent.byRawID(nextComponentID);
                if (component != null) {
                    list.add(component);
                }
            }
        }
    }

    public static Vector2i getColours(ItemStack stack) {
        List<ShellComponent> components = getComponents(stack);
        Vector2i primary = new Vector2i(16777215, 255);
        Vector2i secondary = new Vector2i(4967423, 255);
        Vector2i temp;
        for (ShellComponent component : components) {
            temp = component.getColourWithPriority();
            if (temp.y < primary.y) {
                secondary = primary;
                primary = temp;
            } else if (temp.y < secondary.y) {
                secondary = temp;
            }
        }
        if (secondary.y >= 255) {
            secondary = primary;
        }
        return new Vector2i(primary.x, secondary.x);
    }
}
