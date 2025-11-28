package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.network.C2STurretSetupRequestPacket;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.engineerofchaos.firesupport.network.NetworkUtil;
import net.engineerofchaos.firesupport.network.S2CTurretTargetAnglePacket;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.shell.ShellComponents;
import net.engineerofchaos.firesupport.shell.ShellUtil;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Arrangements;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.engineerofchaos.firesupport.turret.TurretBuilderUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;

public class RideableTurretEntity extends Entity {
    public float targetYaw;
    public float targetPitch;

    private final float cyclePeriod = 2.5f;
    private float cycleCounter = 0;

    private Arrangement arrangement;
    private Autoloader autoloader;

    public RideableTurretEntity(EntityType<?> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
    }

    public RideableTurretEntity(BlockPos pos, World world) {
        super(ModEntities.RIDEABLE_TURRET, world);
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.1875, pos.getZ() + 0.5);
    }

    public RideableTurretEntity(BlockPos pos, World world, Arrangement arrangement, Autoloader autoloader) {
        super(ModEntities.RIDEABLE_TURRET, world);
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.1875, pos.getZ() + 0.5);
        this.arrangement = arrangement;
        this.autoloader = autoloader;
    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient && cycleCounter < cyclePeriod) {
            cycleCounter++;
        }

        Entity controller = this.getFirstPassenger();
        if (controller != null) {
            if (!getWorld().isClient) {
                this.targetYaw = controller.getYaw();
                this.targetPitch = controller.getPitch();
            }
        }

        if (!getWorld().isClient) {
            NetworkUtil.send(new S2CTurretTargetAnglePacket(this, new Vec2f(targetYaw, targetPitch)));
        }

        this.setPitch(MathHelper.stepUnwrappedAngleTowards(this.getPitch(), targetPitch, 5));
        this.setYaw(MathHelper.stepUnwrappedAngleTowards(this.getYaw(), targetYaw, 5));

    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        targetYaw = nbt.getFloat("targetYaw");
        targetPitch = nbt.getFloat("targetPitch");

        this.arrangement = TurretBuilderUtil.getArrangement(nbt);
        this.autoloader = TurretBuilderUtil.getAutoloader(nbt);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("targetYaw", targetYaw);
        nbt.putFloat("targetPitch", targetPitch);

        TurretBuilderUtil.addSetupToNBTCompound(nbt, this.arrangement, this.autoloader);
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

    public void shoot() {
        if (!this.getWorld().isClient) {
            if (cycleCounter >= cyclePeriod) {
                cycleCounter -= cyclePeriod;
                while (cycleCounter > 1) {
                    cycleCounter--;
                }
                ItemStack itemStack = ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                        Arrays.asList(ShellComponents.SOLID_AP, ShellComponents.HIGH_EXPLOSIVE),
                        null, 20, CaseLength.MED);

                BulletEntity bulletEntity = new BulletEntity(this.getWorld(), itemStack);

                Vec3d pos = new Vec3d(getX(), getEyeY() - 0.1, getZ());

                Vec3d velocity = bulletEntity.getShotVelocity(getPitch(), getYaw(), 0.0F, 20F, 0.2F);
                Vec3d barrelPos = pos.add(velocity.normalize().multiply(1.5));

                bulletEntity.setVelocity(velocity);
                bulletEntity.setInitialPosWithOffset(barrelPos, cycleCounter);

                HashMap<String, Float> fuseMap = new HashMap<>();

                // this is an optional step to override the default values
                fuseMap.put(ShellComponents.TIMED_FUSE.getID(), 5f);
                bulletEntity.programFuses(fuseMap);
                bulletEntity.initFuses();

                this.getWorld().spawnEntity(bulletEntity);
            }
        }
    }

    public Autoloader getAutoloader() {
        return this.autoloader;
    }

    public Arrangement getArrangement() {
        return this.arrangement;
    }

    public void clientResetSetup(Arrangement arrangement, Autoloader autoloader) {
        this.arrangement = arrangement;
        this.autoloader = autoloader;
    }

    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        NetworkUtil.send(new C2STurretSetupRequestPacket(this));
    }
}
