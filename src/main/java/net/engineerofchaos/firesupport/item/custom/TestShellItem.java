package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.shell.ShellComponentUtil;
import net.engineerofchaos.firesupport.shell.ShellComponents;
import net.engineerofchaos.firesupport.shell.ShellUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.HashMap;
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
        FireSupport.LOGGER.info("hi");
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
            Vec3d velocity = bulletEntity.getShotVelocity(user.getPitch(), user.getYaw(), 0.0F, ShellUtil.calculateLaunchSpeed(itemStack), 1.0F);

            // summon the bullet one tick ahead
            bulletEntity.setPosition(pos.add(velocity.normalize()));
            //bulletEntity.setPosition(pos);
            bulletEntity.setVelocity(velocity);
            HashMap<String, Float> fuseMap = new HashMap<>();

            // this is an optional step to override the default values
            fuseMap.put(ShellComponents.TIMED_FUSE.getID(), 5f);
            bulletEntity.programFuses(fuseMap);
            bulletEntity.initFuses();

            bulletEntity.setOwner(user);

            world.spawnEntity(bulletEntity);
            //bulletEntity.sendBulletVelocity(velocity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ShellComponentUtil.addComponentsToTooltip(stack, tooltip);
    }

    @Override
    public Text getName() {
        return super.getName();
    }
}
