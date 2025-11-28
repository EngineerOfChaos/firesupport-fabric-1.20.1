package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public record S2CTurretTargetAnglePacket(RideableTurretEntity sender, Vec2f angles) implements FSPacket{
    @Override
    public void send(PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) sender.getWorld(), sender.getBlockPos())) {
            ServerPlayNetworking.send(player, FireSupportNetworkingConstants.S2C_TURRET_TARGET_ANGLE, buf);
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(sender.getId());
        buf.writeByte(NetworkUtil.angleToByte(angles.x));
        buf.writeByte(NetworkUtil.angleToByte(angles.y));
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int turretID = buf.readInt();
        float yaw = NetworkUtil.byteToAngle(buf.readByte());
        float pitch = NetworkUtil.byteToAngle(buf.readByte());

        client.execute(() -> {
            if (client.world != null) {
                RideableTurretEntity turret = (RideableTurretEntity) client.world.getEntityById(turretID);
                if (turret != null) {
                    turret.targetYaw = yaw;
                    turret.targetPitch = pitch;
                }
            }
        });
    }
}
