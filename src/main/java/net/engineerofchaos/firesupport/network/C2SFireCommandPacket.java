package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public record C2SFireCommandPacket() implements FSPacket{

    @Override
    public void send(PacketByteBuf buf) {
        ClientPlayNetworking.send(FireSupportNetworkingConstants.C2S_MANUAL_TURRET_FIRE_ID, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (player.getRootVehicle() instanceof RideableTurretEntity turret) {
                turret.shoot();
            }
        });
    }
}
