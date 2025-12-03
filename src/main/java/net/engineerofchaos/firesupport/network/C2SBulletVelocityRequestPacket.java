package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class C2SBulletVelocityRequestPacket implements FSPacket {
    private final Entity target;

    public C2SBulletVelocityRequestPacket(Entity target) {
        this.target = target;
    }

    @Override
    public void send(PacketByteBuf buf) {
        ClientPlayNetworking.send(FireSupportNetworkingConstants.C2S_BULLET_VELOCITY_REQUEST_PACKET_ID, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(target.getId());
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                       PacketByteBuf buf, PacketSender responseSender) {

        int targetBulletID = buf.readVarInt();

        server.execute(() -> {

            BulletEntity targetBullet = (BulletEntity) player.getWorld().getEntityById(targetBulletID);

            if (targetBullet != null) {
                Vec3d velocity = targetBullet.getVelocity();
                Vec3d initialPos = targetBullet.getLaunchPos();
                int cal = targetBullet.calibre;
                CaseLength caseLength = targetBullet.caseLength;
                float caseInset = targetBullet.caseInset;
                int[] multipliers = targetBullet.getMultipliers().toScaledIntArray();

                NetworkUtil.send(new S2CBulletVelocityPacket(targetBulletID, velocity, initialPos, cal, caseLength,
                        caseInset, multipliers, responseSender));

//                PacketByteBuf responseBuf = PacketByteBufs.create();
//                responseBuf.writeVector3f(velocity.toVector3f());
//                responseBuf.writeVector3f(initialPos.toVector3f());
//                responseBuf.writeVarInt(targetBulletID);
//                responseBuf.writeVarInt(cal);
//                responseBuf.writeVarInt(caseLength.ordinal());
//                responseBuf.writeVarInt(caseInset);
//                responseBuf.writeIntArray(multipliers);
//
//                responseSender.sendPacket(FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID, responseBuf);
            } else {
                FireSupport.LOGGER.info("Server: No target bullet found matching ID!");
            }
        });
    }
}
