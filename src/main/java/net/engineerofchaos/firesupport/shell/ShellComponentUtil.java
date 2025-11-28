package net.engineerofchaos.firesupport.shell;

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

    public static List<ShellComponent> getComponents(ItemStack stack, HashMap<String, Float> additionalData) {
        return getComponents(stack.getNbt(), additionalData);
    }

    public static List<ShellComponent> getComponents(@Nullable NbtCompound nbt, HashMap<String, Float> additionalData) {
        List<ShellComponent> list = Lists.newArrayList();
        getComponents(nbt, list, additionalData);
        return list;
    }

    public static ItemStack addComponentsToStack(ItemStack stack, List<ShellComponent> components, HashMap<String, Float> additionalData) {
        if (!components.isEmpty()) {
            // get item nbt
            NbtCompound nbtCompound = stack.getOrCreateNbt();

            addComponentsToNBTCompound(nbtCompound, components, additionalData);

            stack.setNbt(nbtCompound);
        }
        return stack;
    }

    public static void addComponentsToNBTCompound(NbtCompound nbtCompound, List<ShellComponent> components, HashMap<String, Float> additionalData) {
        // make array of component ids
        StringBuilder componentIDsArray = new StringBuilder();
        // add ids to array
        for (ShellComponent component : components) {
            componentIDsArray.append(ShellComponent.getID(component));
            if (component != components.get(components.size() - 1)) {
                componentIDsArray.append(',');
            }
        }
        // add array to item nbt
        nbtCompound.putString("ShellComponents", componentIDsArray.toString());

        // processing for additional data components: if additional data is provided add to nbt
        if (additionalData != null) {
            for (ShellComponent dataComponent : components) {
                if (dataComponent instanceof AdditionalDataShellComponent && additionalData.containsKey(dataComponent.getID())) {

                    float data = additionalData.get(dataComponent.getID());
                    nbtCompound.putFloat(String.valueOf(dataComponent.getID()), data);
                }
            }
        }
    }

    public static ItemStack buildShellItem(ItemStack stack, List<ShellComponent> components) {
        return buildShellItem(stack, components, null);
    }

    public static ItemStack buildShellItem(ItemStack stack, List<ShellComponent> components, HashMap<String, Float> additionalData) {
        if (verifyComponentList(components)) {
            return addComponentsToStack(stack, components, additionalData);
        }
        return stack;
    }

    public static boolean verifyComponentList(List<ShellComponent> components) {
        if (!components.isEmpty()) {
            for (ShellComponent component : components) {
                List<ShellComponent> exclusives = getExclusives(component);
                List<ShellComponent> requiredDependencies = getDependencies(component);

                if (exclusives != null) {
                    for (ShellComponent forbiddenComponent : exclusives) {
                        if (components.contains(forbiddenComponent)) {
                            FireSupport.LOGGER.info("Cannot build shell! module {} is incompatible with {}", component.getTranslationKey(), forbiddenComponent.getTranslationKey());
                            return false;
                        }
                    }
                }

                if (requiredDependencies != null) {
                    for (ShellComponent dependency : requiredDependencies) {
                        if (!components.contains(dependency)) {
                            FireSupport.LOGGER.info("Cannot build shell! module {} requires {}", component.getTranslationKey(), dependency.getTranslationKey());
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void getComponents(@Nullable NbtCompound nbt, List<ShellComponent> list, HashMap<String, Float> additionalData) {
        if (nbt != null && nbt.contains("ShellComponents", NbtElement.STRING_TYPE)) {
            String shellComponentsString = nbt.getString("ShellComponents");
            List<String> stringList = Arrays.asList(shellComponentsString.split(","));

            for (String nextComponentID : stringList) {
                ShellComponent component = ShellComponent.byID(nextComponentID);
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

    private static float retrieveData(@NotNull NbtCompound nbt, String nextComponentID) {
        return nbt.getFloat(nextComponentID);
    }

    public static Vector2i getColours(ItemStack stack) {
        List<ShellComponent> components = getComponents(stack);
        Vector2i primary = new Vector2i(16777215, 255);
        Vector2i secondary = new Vector2i(4967423, 255);
        Vector2i temp;
        for (ShellComponent component : components) {
            if (component.isDualColour()) {
                return component.getColours(stack);
            }
            temp = component.getColours(stack);
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