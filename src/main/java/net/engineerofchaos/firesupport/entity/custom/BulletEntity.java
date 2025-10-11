package net.engineerofchaos.firesupport.entity.custom;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.ModEntities;
import net.engineerofchaos.firesupport.entity.damage.ModDamageTypes;
import net.engineerofchaos.firesupport.network.FireSupportNetworkingConstants;
import net.engineerofchaos.firesupport.shellcomponent.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BulletEntity extends Entity implements Ownable {

    private List<ShellComponent> components;
    private List<FuseShellComponent> fuses;
    private List<PayloadShellComponent> payloads;
    public final HashMap<Integer, Float> additionalData = new HashMap<>();
    public boolean detonationFlag = false;
    private Entity owner = null;
    public float proxyMult;
    private Vec3d lastPos;
    private Vec3d launchPos = Vec3d.ZERO;

    public BulletEntity(EntityType<?> type, World world) {
        super(type, world);
        components = new ArrayList<>();
        fuses = new ArrayList<>();
        payloads = new ArrayList<>();
        this.ignoreCameraFrustum = true;
    }

    public BulletEntity(World world, ItemStack shell) {
        super(ModEntities.BULLET, world);
        components = ShellComponentUtil.getComponents(shell, additionalData);
        fuses = ShellComponentUtil.getFuses(components);
        payloads = ShellComponentUtil.getPayloads(components);
        this.ignoreCameraFrustum = true;
    }

    @Override
    public void tick() {
        this.attemptTickInVoid();
//        if (!this.firstUpdate) {
//            this.launchPos = this.getPos();
//        }
        this.firstUpdate = false;

        setLastPos(this.getPos());

        if (detonationFlag) {
            executePayloadEffects(getPos());
            if (this.getWorld().isClient()) {
                // TODO add impact particle spawning here
            }
            this.discard();
        } else {

            if (this.getWorld().isClient()) {
                // TODO: Tracer setup here

                // overwrite last pos and last vel
            }

            // check projectile path for collisions
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            boolean collision = hitResult != null && hitResult.getType() != HitResult.Type.MISS;
            Vec3d fuseDetPos = tickFuses();
            Vec3d collisionPos = null;

            // If we have collided with something: get the pos
            if (collision) {
                // TODO: Add pierce logic
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    collisionPos = getClosestPosition(hitResult);
                } else {
                    collisionPos = hitResult.getPos();
                }
            }

            if (collision && fuseDetPos != null) {
                // logic for checking which comes first
                double collisionDistance = this.getPos().subtract(collisionPos).lengthSquared();
                double fuseDistance = this.getPos().subtract(fuseDetPos).lengthSquared();
                if (collisionDistance > fuseDistance) {
                    collision = false;
                } else {
                    fuseDetPos = null;
                }
                // set the latter one to null
            }

            if (collision) {
                // TODO add old entity hitResult processing
                this.setPosition(collisionPos);

                this.detonationFlag = true;

                if (!this.getWorld().isClient && hitResult.getType() == HitResult.Type.ENTITY) {
                    Entity target = ((EntityHitResult) hitResult).getEntity();
                    DamageSource damageSource = new DamageSource(this.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.BULLET_KINETIC_DAMAGE));
                    target.damage(damageSource, 5);
                }

            } else if (fuseDetPos != null) {
                // TODO add processing for fuse det
                this.setPosition(fuseDetPos);

                this.detonationFlag = true;
            } else {
                // move entity as normal
                this.setPosition(this.getPos().add(this.getVelocity()));
                this.addVelocity(this.getGravityAccel());
            }

        }



        //this.getWorld().isChunkLoaded()
    }

    private boolean canHit(Entity entity) {
        if (!entity.canBeHitByProjectile()) {
            return false;
        }
        if (entity == getOwner() && this.age < 5) {
            return false;
        }
        return true;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        additionalData.clear();
        components = ShellComponentUtil.getComponents(nbt, additionalData);
        fuses = ShellComponentUtil.getFuses(components);
        payloads = ShellComponentUtil.getPayloads(components);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        ShellComponentUtil.addComponentsToNBTCompound(nbt, components, additionalData);
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5f;
    }



    // custom functions

    private void executePayloadEffects(Vec3d pos) {
        for (PayloadShellComponent payload : payloads) {
            payload.executePayloadEffect(pos, this.getWorld(), 1);
        }
    }



    // --- TRAJECTORY LOGIC ---

    public float getYaw() {
        float x = (float) this.getVelocity().getX();
        float z = (float) this.getVelocity().getZ();
        if (Float.isNaN(x) || Float.isNaN(z)) {
            return 0;
        }
        if (z > 0) {
            return (float) -(Math.toDegrees(Math.atan(x/z)));
        } else {
            float angle = (float) Math.toDegrees(Math.atan(z/x));
            if (x > 0) {
                return -(90 - angle);
            } else if (x < 0) {
                return -(-90 - angle);
            } else {
                return 180;
            }
        }
    }

    public float getPitch() {
        float horizontal = new Vec2f((float) this.getVelocity().getX(), (float) this.getVelocity().getZ()).length();
        if (horizontal != 0) {
            return (float) -Math.toDegrees(Math.atan(this.getVelocity().getY() / horizontal));
        }
        return 0;
    }

    public Vec3d getGravityAccel() {
        return new Vec3d(0, -0.03, 0);
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


    // --- FUSE LOGIC ---

    /**
    * Runs fuse setup for all fuses in the list
     */
    public void initFuses() {
        for (FuseShellComponent fuse : fuses) {
            fuse.initFuse(this);
        }
    }

    @Nullable
    private Vec3d tickFuses() {
        Vec3d location = null;
        for (FuseShellComponent fuse : fuses) {
            Vec3d temp = fuse.checkFuseCondition(this, additionalData);
            if (temp != null) {
                location = temp;
            }
        }
        return location;
    }

    public void programFuses(HashMap<Integer, Float> fuseMap) {
        for (FuseShellComponent fuse : fuses) {
            int fuseID = ShellComponent.getRawID((ShellComponent) fuse);
            if (fuseMap.containsKey(fuseID)) {
                float value = fuseMap.get(fuseID);
                ((AdditionalDataShellComponent) fuse).setData(value, additionalData);
            }
        }
    }



    // --- FIRING LOGIC ---

    public Vec3d getShotVelocity(float pitch, float yaw, float roll, float speed, float divergence) {
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

    public void setInitialPos(Vec3d pos) {
        this.setPosition(pos);
        this.setLastPos(pos);
        this.launchPos = pos;
    }

    public void setLastPos(Vec3d pos) {
        this.lastPos = pos;
    }

    public Vec3d getLastPos() {
        return this.lastPos;
    }

    public void setLaunchPos(Vec3d pos){
        this.launchPos = pos;
    }

    public Vec3d getLaunchPos(){
        return this.launchPos;
    }


    // -- OWNER LOGIC --

    @Override
    public @Nullable Entity getOwner() {
        return this.owner;
        // TODO: put this in the spawn packet that sends velocity!
    }



    // --- RENDER LOGIC ---

    @Override
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0;
        if (Double.isNaN(d)) {
            d = 4.0;
        }

        d *= 256.0;
        return distance < d * d;
    }

    public float getTracerLength(float tickDelta) {
        float defaultLength = (float) (this.getVelocity().length() / 4);
        Vec3d pos = getPos();
        Vec3d launchPos = getLaunchPos();
        Vec3d vel = getVelocity();

        Vec3d delta = pos.subtract(launchPos);
        //FireSupport.LOGGER.info("Travel distance: {}", pos.subtract(launchPos).length());

        if (launchPos == Vec3d.ZERO) {
            return 0.1F;
        }

        if (delta.lengthSquared() < vel.multiply(1.3).lengthSquared()) {
            float frameOneDistance = (float) (tickDelta * delta.length());
            return Math.min(defaultLength, frameOneDistance);
        }

        //return 10F;
        return defaultLength;

    }



    // --- NETWORKING ---

//    public void sendBulletVelocity(Vec3d velocity) {
//
//        PacketByteBuf buf = PacketByteBufs.create();
//        buf.writeVector3f(velocity.toVector3f());
//        buf.writeInt(this.getId());
//
//        for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
//            ServerPlayNetworking.send(player, FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID, buf);
//        }
//    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        FireSupport.LOGGER.info("spawn packet received by client");

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());

        ClientPlayNetworking.send(FireSupportNetworkingConstants.C2S_BULLET_VELOCITY_REQUEST_PACKET_ID, buf);
        // INFO: This packet shows up when the entity is loaded, not just spawned the first time!
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return super.createSpawnPacket();
    }
}
