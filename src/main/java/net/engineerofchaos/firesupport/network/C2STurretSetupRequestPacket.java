package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.entity.custom.AbstractTurretEntity;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class C2STurretSetupRequestPacket implements FSPacket{
    private final Entity targetEntity;

    public C2STurretSetupRequestPacket(Entity entity){
        targetEntity = entity;
    }

    @Override
    public void send(PacketByteBuf buf) {
        ClientPlayNetworking.send(FireSupportNetworkingConstants.C2S_TURRET_SETUP_REQUEST_PACKET_ID, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(targetEntity.getId());
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender) {
        int turretID = buf.readVarInt();
        server.execute(() -> {
            Entity turret = player.getWorld().getEntityById(turretID);
            if (turret != null) {
                Arrangement arrangement = ((AbstractTurretEntity) turret).getArrangement();
                Autoloader autoloader = ((AbstractTurretEntity) turret).getAutoloader();

                NetworkUtil.send(new S2CTurretSetupPacket(arrangement, autoloader, turretID, responseSender));
            }

        });
    }
}
