package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.shellcomponent.FuseShellComponent;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponent;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponents;
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

import java.util.HashMap;

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
            BulletEntity bulletEntity = new BulletEntity(world, itemStack);
            Vec3d pos = new Vec3d(user.getX(), user.getEyeY(), user.getZ());
            Vec3d velocity = bulletEntity.calcVelocity(user.getPitch(), user.getYaw(), 0.0F, 16F, 1.0F);

            // summon the bullet one tick ahead
            //bulletEntity.setPosition(pos.add(velocity.multiply(0.1)));
            bulletEntity.setPosition(pos);
            bulletEntity.setVelocity(velocity);
            HashMap<Integer, Float> fuseMap = new HashMap<>();

            // this is an optional step to override the default values
            fuseMap.put(ShellComponent.getRawID(ShellComponents.TIMED_FUSE), 20f);
            bulletEntity.programFuses(fuseMap);
            bulletEntity.initFuses();

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
