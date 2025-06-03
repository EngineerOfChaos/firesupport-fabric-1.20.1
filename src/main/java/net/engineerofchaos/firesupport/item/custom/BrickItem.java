package net.engineerofchaos.firesupport.item.custom;

import net.engineerofchaos.firesupport.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class BrickItem extends Item {
    public BrickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient()){
            BlockPos positionClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();

            if(context.getWorld().getBlockState(positionClicked).isOf(Blocks.GLASS)){
                context.getWorld().breakBlock(positionClicked, false);
                context.getWorld().setBlockState(positionClicked, ModBlocks.DAMAGED_GLASS.getDefaultState(),
                        Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                context.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, positionClicked,
                        GameEvent.Emitter.of(player, ModBlocks.DAMAGED_GLASS.getDefaultState()));
//                context.getWorld().playSound(null, positionClicked, SoundEvents.BLOCK_GLASS_BREAK,
//                        SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
        return ActionResult.SUCCESS;
    }
}
