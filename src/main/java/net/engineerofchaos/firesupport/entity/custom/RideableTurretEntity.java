package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.network.C2STurretSetupRequestPacket;
import net.engineerofchaos.firesupport.network.NetworkUtil;
import net.engineerofchaos.firesupport.network.S2CTurretTargetAnglePacket;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.shell.ShellComponents;
import net.engineerofchaos.firesupport.shell.ShellUtil;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Arrangements;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.engineerofchaos.firesupport.turret.TurretBuilderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RideableTurretEntity extends AbstractTurretEntity {

    public RideableTurretEntity(EntityType<?> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
    }

    public RideableTurretEntity(BlockPos pos, World world, Arrangement arrangement, Autoloader autoloader) {
        super(ModEntities.RIDEABLE_TURRET, world);
        this.intersectionChecked = true;
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.1875, pos.getZ() + 0.5);
        initSetup(arrangement, autoloader);
    }

    @Override
    public void tick() {
        super.tick();

        Entity controller = this.getFirstPassenger();
        if (controller != null) {
            if (!getWorld().isClient) {
                this.targetYaw = controller.getYaw();
                this.targetPitch = controller.getPitch();
            }
        }
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
    public double getMountedHeightOffset() {
        return 0.2;
    }
}
