package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.RideableTurretEntity;
import net.engineerofchaos.firesupport.turret.Arrangement;
import net.engineerofchaos.firesupport.turret.Arrangements;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.engineerofchaos.firesupport.util.ModRegistries;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class S2CTurretSetupPacket implements FSPacket{
    private final Arrangement arrangement;
    private final Autoloader autoloader;
    private final PacketSender responseSender;
    private final int turretID;

    public S2CTurretSetupPacket(Arrangement arrangement, Autoloader autoloader, int turretID, PacketSender responseSender) {
        this.arrangement = arrangement;
        this.autoloader = autoloader;
        this.turretID = turretID;
        this.responseSender = responseSender;
    }

    @Override
    public void send(PacketByteBuf buf) {
        responseSender.sendPacket(FireSupportNetworkingConstants.S2C_TURRET_SETUP_PACKET_ID, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        Identifier id;
        if ((id = ModRegistries.ARRANGEMENT.getId(arrangement)) != null) {
            buf.writeString(id.toString());
        } else {
            FireSupport.LOGGER.info("invalid arrangement ID!");
        }
        if ((id = ModRegistries.AUTOLOADER.getId(autoloader)) != null) {
            buf.writeString(id.toString());
        } else {
            FireSupport.LOGGER.info("invalid autoloader ID!");
        }
        buf.writeInt(turretID);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
                       PacketSender responseSender) {
        String arrangementID = buf.readString();
        String autoloaderID = buf.readString();
        int turretID = buf.readInt();

        client.execute(() -> {
            Arrangement arrangement = ModRegistries.ARRANGEMENT.get(new Identifier(arrangementID));
            Autoloader autoloader = ModRegistries.AUTOLOADER.get(new Identifier(autoloaderID));

            if (client.world != null) {
                RideableTurretEntity targetTurret = (RideableTurretEntity) client.world.getEntityById(turretID);
                if (targetTurret != null) {
                    targetTurret.clientResetSetup(arrangement, autoloader);
                }
            }
        });
    }
}
