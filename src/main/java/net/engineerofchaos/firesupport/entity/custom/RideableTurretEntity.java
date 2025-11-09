package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RideableTurretEntity extends Entity {
    public RideableTurretEntity(EntityType<?> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
    }

    public RideableTurretEntity(BlockPos pos, World world) {
        super(ModEntities.RIDEABLE_TURRET, world);
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.1875, pos.getZ() + 0.5);
    }

    @Override
    public void tick() {
        super.tick();
        Entity controller = this.getFirstPassenger();
        if (!getWorld().isClient && controller != null) {
            this.setYaw(controller.getYaw());
            this.setPitch(controller.getPitch());
        }
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.hasPassengers()) {
            return ActionResult.PASS;
        } else if (!this.getWorld().isClient) {
            return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.2;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.discard();
        return true;
    }
}
