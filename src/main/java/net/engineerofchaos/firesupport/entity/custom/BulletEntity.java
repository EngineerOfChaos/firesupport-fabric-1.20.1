package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.item.ModItems;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BulletEntity extends ThrownItemEntity {
    public BulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.BULLET_THROWN, livingEntity, world);
    }

    public BulletEntity(EntityType<BulletEntity> entityType, World world, ItemStack shell) {
        super(entityType, world);

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
        if (!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte) 3);
        }
        this.discard();
        super.onBlockHit(blockHitResult);
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

    public Vec3d getInterpolationOffset(float tickDelta) {
        Vec3d velocity = this.getVelocity();
        double x = velocity.getX() * (1 - tickDelta);
        double y = velocity.getY() * (1 - tickDelta);
        double z = velocity.getZ() * (1 - tickDelta);
        return new Vec3d(0, 0, z);
    }
}
