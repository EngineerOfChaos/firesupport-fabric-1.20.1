package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector2i;

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

//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ItemStack stack = user.getStackInHand(hand);
//
//        Vector2i colours = ShellComponentUtil.getColours(stack);
//        user.sendMessage(Text.literal("Detected colours: primary = " + colours.x + ", secondary = " + colours.y));
//
//        List<ShellComponent> components = ShellComponentUtil.getComponents(stack);
//        for (ShellComponent component : components) {
//            user.sendMessage(Text.literal("Detected component: " + ShellComponent.getRawID(component)));
//        }
//
//        NbtCompound nbt = stack.getNbt();
//        if (nbt != null) {
//            user.sendMessage(Text.literal("Detected components tag: " + nbt.contains("ShellComponents")));
//        } else {
//            user.sendMessage(Text.literal("No data!"));
//        }
//
//        return super.use(world, user, hand);
//    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            BulletEntity bulletEntity = new BulletEntity(user, world);
            Vec3d velocity = bulletEntity.calcVelocity(user.getPitch(), user.getYaw(), 0.0F, 5F, 1.0F);
            bulletEntity.setItem(itemStack);
            bulletEntity.setVelocity(velocity);

            world.spawnEntity(bulletEntity);
            bulletEntity.sendBulletVelocity(velocity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }




}
