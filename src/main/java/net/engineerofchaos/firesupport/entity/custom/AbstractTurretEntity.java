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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractTurretEntity extends Entity {

    public float targetYaw;
    public float targetPitch;
    private final List<Float> cycleCounters = new ArrayList<>();
    private Arrangement arrangement;
    private Autoloader autoloader;

    public AbstractTurretEntity(EntityType<?> type, World world) {
        super(type, world);
        this.intersectionChecked = true;
    }

    public AbstractTurretEntity(BlockPos pos, World world, Arrangement arrangement, Autoloader autoloader) {
        super(ModEntities.RIDEABLE_TURRET, world);
        this.intersectionChecked = true;
        this.setPosition(pos.getX() + 0.5, pos.getY() + 0.1875, pos.getZ() + 0.5);
        initSetup(arrangement, autoloader);
    }

    @Override
    public void tick() {
        super.tick();

        boolean flag = false;
        for (float counter : cycleCounters)  {
            if (counter >= getAutoloader().getCyclePeriod()) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            cycleCounters.replaceAll(count -> count + 1);
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

        initSetup(TurretBuilderUtil.getArrangement(nbt), TurretBuilderUtil.getAutoloader(nbt));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("targetYaw", targetYaw);
        nbt.putFloat("targetPitch", targetPitch);

        TurretBuilderUtil.addSetupToNBTCompound(nbt, this.arrangement, this.autoloader);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.discard();
        return true;
    }

    public void shoot() {
        List<Arrangements.Pivot> pivots = getArrangement().getPivots();
        float cyclePeriod = getAutoloader().getCyclePeriod();
        for (int i = 0; i < pivots.size(); i++) {
            Arrangements.Pivot pivot = pivots.get(i);
            float cycleCounter = cycleCounters.get(i);

            // if this pivot is ready to fire: reset its cycle counter
            if (cycleCounter >= cyclePeriod) {
                // FireSupport.LOGGER.info("cycle counter status: %s".formatted(cycleCounters));
                cycleCounter -= cyclePeriod;
                while (cycleCounter > 1) {
                    cycleCounter--;
                }
                cycleCounters.set(i, cycleCounter);

                if (!getWorld().isClient()) {
                    // generate placeholder round
                    ItemStack itemStack = ShellUtil.buildShellItem(ModItems.TEST_SHELL,
                            Arrays.asList(ShellComponents.SOLID_AP, ShellComponents.HIGH_EXPLOSIVE),
                            null, 20, CaseLength.MED, 0);

                    // make bullet
                    BulletEntity bulletEntity = new BulletEntity(this.getWorld(), itemStack);

                    //FireSupport.LOGGER.info("Calculated offset: %s".formatted(pivot.getRelativeLocation(getPitch(), getYaw())));

                    Vec3d loaderPos = pivot.getRelativeLocation(getPitch(), getYaw()).add(this.getPos());

                    Vec3d velocity = bulletEntity.getShotVelocity(getPitch(), getYaw(), 0.0F, 20F, 0.2F);
                    Vec3d barrelPos = loaderPos.add(velocity.normalize().multiply(getAutoloader().getBarrelOffset()));

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
    }

    public Autoloader getAutoloader() {
        return this.autoloader;
    }

    public Arrangement getArrangement() {
        return this.arrangement;
    }

    public void clientResetSetup(Arrangement arrangement, Autoloader autoloader) {
        initSetup(arrangement, autoloader);
    }

    public void initSetup(Arrangement arrangement, Autoloader autoloader) {
        this.arrangement = arrangement;
        this.autoloader = autoloader;
        this.cycleCounters.clear();
        float cycleStart = 0f;
        for (Arrangements.Pivot pivot : arrangement.getPivots()) {
            FireSupport.LOGGER.info("cycle start point: %s".formatted(cycleStart));
            this.cycleCounters.add(cycleStart);
            cycleStart += (autoloader.getCyclePeriod() / arrangement.getPivots().size());
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        NetworkUtil.send(new C2STurretSetupRequestPacket(this));
    }
}
