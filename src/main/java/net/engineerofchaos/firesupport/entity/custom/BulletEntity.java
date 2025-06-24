package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.engineerofchaos.firesupport.shellcomponent.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BulletEntity extends ThrownItemEntity {

    private final List<ShellComponent> components;
    private final List<FuseShellComponent> fuses;
    private final List<PayloadShellComponent> payloads;

    public BulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
        components = new ArrayList<>();
        fuses = new ArrayList<>();
        payloads = new ArrayList<>();
    }

    public BulletEntity(World world, ItemStack shell) {
        super(ModEntities.BULLET_THROWN, world);
        components = ShellComponentUtil.getComponents(shell);
        fuses = ShellComponentUtil.getFuses(components);
        payloads = ShellComponentUtil.getPayloads(components);
    }

    public void initFuses() {
        for (FuseShellComponent fuse : fuses) {
            fuse.initFuse(this);
        }
    }

    @Override
    public void tick() {
        Vec3d detonationLocation = tickFuses();
        if (detonationLocation != null) {
            boolean destroy = executePayloadEffects(detonationLocation);
            if (destroy) {
                this.discard();
            }
        }
        super.tick();
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.TEST_SHELL;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte) 3);
            HitResult.Type type = hitResult.getType();
            if (type == HitResult.Type.BLOCK) {
                executePayloadEffects(hitResult.getPos());
                this.discard();
            }
            if (type == HitResult.Type.ENTITY) {
                Vec3d pos = getClosestPosition(hitResult);
                executePayloadEffects(pos);
                this.discard();
            }
        }

        super.onCollision(hitResult);
    }

    private Vec3d getClosestPosition(HitResult hitResult) {
        Vec3d normal = this.getVelocity();
        FireSupport.LOGGER.info("Current bullet velocity {}", normal);


        // plane normal vector (bullet velocity)
        double a = normal.getX();
        double b = normal.getY();
        double c = normal.getZ();

        // point on the plane (the target entity)
        double ex = hitResult.getPos().getX();
        double ey = hitResult.getPos().getY();
        double ez = hitResult.getPos().getZ();

        // bullet pos
        double bx = this.getX();
        double by = this.getY();
        double bz = this.getZ();

        // solve for last parameter for plane equation
        double planeOffset = -(ex*a + ey*b + ez*c);

        // solve for the multiplier of how far along the bullet's velocity the closest point is (should be <1)
        double numerator = -(planeOffset + (a*bx) + (b*by) + (c*bz));
        double denominator = (a*a + b*b + c*c);
        double mult = numerator/denominator;
        //double mult = (-planeOffset - (a * bx) - (b * by) - (c * bz)) / (a*a + b*b + c*c);

        return new Vec3d(bx + mult * a, by + mult * b, bz + mult * c);
    }

    /**
     *
     * @param pos position of detonation
     * @return bool does any payload destroy the shell
     */
    private boolean executePayloadEffects(Vec3d pos) {
        boolean destroyShell = false;
        boolean flag;
        for (PayloadShellComponent payload : payloads) {
            flag = payload.executePayloadEffect(pos, this.getWorld(), 1);
            if (flag) { destroyShell = true; }
        }
        return destroyShell;
    }

    @Nullable
    private Vec3d tickFuses() {
        Vec3d location = null;
        for (FuseShellComponent fuse : fuses) {
            Vec3d temp = fuse.checkFuseCondition(this.getVelocity(), this.getPos());
            if (temp != null) {
                location = temp;
            }
        }
        return location;
    }

    public void sendBulletVelocity(Vec3d velocity) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVector3f(velocity.toVector3f());
        buf.writeInt(this.getId());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos())) {
            ServerPlayNetworking.send(player, FireSupportNetworkingConstants.BULLET_VELOCITY_PACKET_ID, buf);
        }
    }


    public Vec3d calcVelocity(float pitch, float yaw, float roll, float speed, float divergence) {
        float x = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float y = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float z = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        return new Vec3d (x, y, z).normalize()
                .add(
                this.random.nextTriangular(0.0, 0.0172275 * divergence),
                this.random.nextTriangular(0.0, 0.0172275 * divergence),
                this.random.nextTriangular(0.0, 0.0172275 * divergence)
        ).multiply(speed);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        int i = packet.getId();
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.updateTrackedPosition(d, e, f);
        this.refreshPositionAfterTeleport(d, e, f);
        this.setPitch(packet.getPitch());
        this.setYaw(packet.getYaw());
        this.setId(i);
        this.setUuid(packet.getUuid());
    }

    @Override
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0;
        if (Double.isNaN(d)) {
            d = 4.0;
        }

        d *= 256.0;
        return distance < d * d;
    }

    public float getYaw() {
        float x = (float) this.getVelocity().getX();
        float z = (float) this.getVelocity().getZ();
        if (z > 0) {
            return (float) Math.toDegrees(Math.atan(x/z));
        } else {
            float angle = (float) Math.toDegrees(Math.atan(z/x));
            if (x > 0) {
                return 90 - angle;
            } else if (x < 0) {
                return -90 - angle;
            } else {
                return 180;
            }
        }
    }

    public float getPitch() {
        float horizontal = new Vec2f((float) this.getVelocity().getX(), (float) this.getVelocity().getZ()).length();
        return (float) - Math.toDegrees(Math.atan(this.getVelocity().getY() / horizontal));
    }

    public void programFuses(HashMap<Integer, Float> fuseMap) {
        for (FuseShellComponent fuse : fuses) {
            int fuseID = ShellComponent.getRawID((ShellComponent) fuse);
            if (fuseMap.containsKey(fuseID)) {
                float value = fuseMap.get(fuseID);
                ((AdditionalDataShellComponent) fuse).setData(value);
            }
        }
    }
}
