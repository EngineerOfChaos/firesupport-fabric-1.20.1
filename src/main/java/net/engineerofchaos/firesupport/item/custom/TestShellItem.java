package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.shellcomponent.ShellComponent;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.joml.Vector2i;

import java.util.List;

public class TestShellItem extends Item {

    public TestShellItem(Settings settings) {
        super(settings);
    }

    public static int getColour(ItemStack stack, int tintIndex) {
        Vector2i colours = ShellComponentUtil.getColours(stack);
        if (tintIndex == 0) {
            return colours.x;
        } else if (tintIndex == 1) {
            return colours.y;
        }
        return -1;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        Vector2i colours = ShellComponentUtil.getColours(stack);
        user.sendMessage(Text.literal("Detected colours: primary = " + colours.x + ", secondary = " + colours.y));

        List<ShellComponent> components = ShellComponentUtil.getComponents(stack);
        for (ShellComponent component : components) {
            user.sendMessage(Text.literal("Detected component: " + ShellComponent.getRawID(component)));
        }

        NbtCompound nbt = stack.getNbt();
        if (nbt != null) {
            user.sendMessage(Text.literal("Detected components tag: " + nbt.contains("ShellComponents")));
        } else {
            user.sendMessage(Text.literal("No data!"));
        }

        return super.use(world, user, hand);
    }
}
