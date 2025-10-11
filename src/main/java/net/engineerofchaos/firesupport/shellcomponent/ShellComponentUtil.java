package net.engineerofchaos.firesupport.shellcomponent;

import com.google.common.collect.Lists;
import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ShellComponentUtil {

    public static List<ShellComponent> getComponents(ItemStack stack) {
        return getComponents(stack.getNbt(), new HashMap<>());
    }

    public static List<ShellComponent> getComponents(ItemStack stack, HashMap<Integer, Float> additionalData) {
        return getComponents(stack.getNbt(), additionalData);
    }

    public static List<ShellComponent> getComponents(@Nullable NbtCompound nbt, HashMap<Integer, Float> additionalData) {
        List<ShellComponent> list = Lists.newArrayList();
        getComponents(nbt, list, additionalData);
        return list;
    }

    public static ItemStack newShellWithComponents(ItemStack stack, List<ShellComponent> components) {
        return newShellWithComponents(stack, components, null);
    }

    public static ItemStack newShellWithComponents(ItemStack stack, List<ShellComponent> components, HashMap<Integer, Float> additionalData) {
        if (!components.isEmpty()) {
            // get item nbt
            NbtCompound nbtCompound = stack.getOrCreateNbt();

            addComponentsToNBTCompound(nbtCompound, components, additionalData);

            stack.setNbt(nbtCompound);
        }
        return stack;
    }

    public static void addComponentsToNBTCompound(NbtCompound nbtCompound, List<ShellComponent> components, HashMap<Integer, Float> additionalData) {
        // make array of component ids
        int[] componentIDsArray = new int[components.size()];
        // add ids to array
        for (int i = 0; i < components.size(); i++) {
            componentIDsArray[i] = ShellComponent.getRawID(components.get(i));
        }
        // add array to item nbt
        nbtCompound.putIntArray("ShellComponents", componentIDsArray);

        // processing for additional data components: if additional data is provided add to nbt
        if (additionalData != null) {
            for (ShellComponent dataComponent : components) {
                if (dataComponent instanceof AdditionalDataShellComponent && additionalData.containsKey(dataComponent.getRawID())) {

                    float data = additionalData.get(dataComponent.getRawID());
                    nbtCompound.putFloat(String.valueOf(dataComponent.getRawID()), data);
                }
            }
        }
    }

    public static ItemStack buildShellItem(ItemStack stack, List<ShellComponent> components) {
        if (!components.isEmpty()) {
            boolean flag = false;
            for (ShellComponent component : components) {
                List<ShellComponent> exclusives = getExclusives(component);
                List<ShellComponent> requiredDependencies = getDependencies(component);

                if (exclusives != null) {
                    for (ShellComponent forbiddenComponent : exclusives) {
                        if (components.contains(forbiddenComponent)) {
                            flag = true;
                            FireSupport.LOGGER.info("Cannot build shell! module {} is incompatible with {}", component.getTranslationKey(), forbiddenComponent.getTranslationKey());
                            break;
                        }
                    }
                }

                if (requiredDependencies != null) {
                    for (ShellComponent dependency : requiredDependencies) {
                        if (!components.contains(dependency)) {
                            flag = true;
                            FireSupport.LOGGER.info("Cannot build shell! module {} requires {}", component.getTranslationKey(), dependency.getTranslationKey());
                            break;
                        }
                    }
                }
            }

            if (!flag) {
                return newShellWithComponents(stack, components);
            }
            FireSupport.LOGGER.info("Shell build failed!");
        }
        return stack;
    }

    public static void getComponents(@Nullable NbtCompound nbt, List<ShellComponent> list, HashMap<Integer, Float> additionalData) {
        if (nbt != null && nbt.contains("ShellComponents", NbtElement.INT_ARRAY_TYPE)) {
            int[] nbtList = nbt.getIntArray("ShellComponents");

            for (int i = 0; i < nbtList.length; i++) {

                int nextComponentID = nbtList[i];
                ShellComponent component = ShellComponent.byRawID(nextComponentID);
                if (component != null) {

                    // if the component should have extra data, retrieve it from the nbt
                    if (component instanceof AdditionalDataShellComponent) {
                        float data = retrieveData(nbt, nextComponentID);
                        // if no stored value (aka 0.0f) then don't override data, leave as default
                        if (data != 0.0f) {
                            additionalData.put(nextComponentID, data);
                        } else {
                            additionalData.put(nextComponentID, ((AdditionalDataShellComponent) component).getDefaultData());
                        }
                    }

                    list.add(component);
                }
            }
        }
    }

    private static float retrieveData(@NotNull NbtCompound nbt, int nextComponentID) {
        return nbt.getFloat(String.valueOf(nextComponentID));
    }

    public static Vector2i getColours(ItemStack stack) {
        List<ShellComponent> components = getComponents(stack);
        Vector2i primary = new Vector2i(16777215, 255);
        Vector2i secondary = new Vector2i(4967423, 255);
        Vector2i temp;
        for (ShellComponent component : components) {
            if (component.isDualColour()) {
                return component.getColours();
            }
            temp = component.getColours();
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

    public static void addComponentsToTooltip(ItemStack stack, List<Text> tooltip) {
        List<ShellComponent> components = getComponents(stack);
        for (ShellComponent component : components) {
            tooltip.add(Text.translatable(component.getTranslationKey()).formatted(Formatting.DARK_GRAY));
        }
    }

    public static List<ShellComponent> getExclusives(ShellComponent component) {
        return ShellComponents.EXCLUSIVITY_MAP.get(component);
    }

    // component needs this list of things in order to work
    public static List<ShellComponent> getDependencies(ShellComponent component) {
        return ShellComponents.DEPENDENCY_MAP.get(component);
    }

    public static List<Float> createMultipliers(float speed, float damage, float ap,
                                           float inaccuracy, float payload, float knockback) {
        return Arrays.asList(speed, damage, ap, inaccuracy, payload, knockback);
    }

    /**
     * Gets the fuse components from a list of components
     * @param components
     * @return list of fuses
     */
    public static List<FuseShellComponent> getFuses(List<ShellComponent> components) {
        List<FuseShellComponent> fuses = new ArrayList<>();
        for (ShellComponent component : components) {
            if  (component instanceof FuseShellComponent) {
                fuses.add((FuseShellComponent) component);
            }
        }
        return fuses;
    }

    /**
     * Gets the payload components from a list of components
     * @param components
     * @return list of payloads
     */
    public static List<PayloadShellComponent> getPayloads(List<ShellComponent> components) {
        List<PayloadShellComponent> payloads = new ArrayList<>();
        for (ShellComponent component : components) {
            if  (component instanceof PayloadShellComponent) {
                payloads.add((PayloadShellComponent) component);
            }
        }
        return payloads;
    }
}