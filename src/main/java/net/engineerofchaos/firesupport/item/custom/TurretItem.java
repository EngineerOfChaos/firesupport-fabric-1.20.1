package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TurretItem extends Item {
    public TurretItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        if (world.getBlockState(pos) != ModBlocks.TURRET_RING.getDefaultState()) {
            return ActionResult.PASS;
        }
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack stack = context.getStack();
            spawnTurret(world, pos, stack);
            if(context.getPlayer() != null && !context.getPlayer().isCreative()) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
    }

    private void spawnTurret(World world, BlockPos pos, ItemStack stack) {
        RideableTurretEntity entity = new RideableTurretEntity(pos, world);
        world.spawnEntity(entity);
    }
}
