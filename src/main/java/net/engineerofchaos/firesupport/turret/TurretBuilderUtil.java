package net.engineerofchaos.firesupport.turret;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class TurretBuilderUtil {

    public static void addSetupToNBTCompound(NbtCompound nbtCompound, Arrangement arrangement, Autoloader autoloader) {
        Identifier id;
        if ((id = ModRegistries.ARRANGEMENT.getId(arrangement)) != null) {
            nbtCompound.putString("arrangement", id.toString());
        } else {
            FireSupport.LOGGER.info("invalid arrangement ID!");
        }
        if ((id = ModRegistries.AUTOLOADER.getId(autoloader)) != null) {
            nbtCompound.putString("autoloader", id.toString());
        } else {
            FireSupport.LOGGER.info("invalid autoloader ID!");
        }
    }

    public static Arrangement getArrangement(NbtCompound nbtCompound) {
        String arrangement = nbtCompound.getString("arrangement");
        return ModRegistries.ARRANGEMENT.get(new Identifier(arrangement));
    }

    public static Autoloader getAutoloader(NbtCompound nbtCompound) {
        String autoloader = nbtCompound.getString("autoloader");
        return ModRegistries.AUTOLOADER.get(new Identifier(autoloader));
    }

    public static ItemStack buildTurretItem(Item baseItem, Arrangement arrangement, Autoloader autoloader) {
        ItemStack stack = new ItemStack(baseItem);
        NbtCompound nbt = stack.getOrCreateNbt();
        addSetupToNBTCompound(nbt, arrangement, autoloader);
        return stack;
    }
}
