package net.engineerofchaos.firesupport.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

public class NetworkUtil {
    public static void send(FSPacket packet) {
        PacketByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        packet.send(buf);
    }

    public static byte angleToByte(float angle) {
        return (byte) MathHelper.floor(angle * 256F/360F);
    }

    public static float byteToAngle(byte angle) {
        return angle * 360F/256F;
    }
}
