package net.engineerofchaos.firesupport.network;

import net.minecraft.network.PacketByteBuf;

public interface FSPacket {

    void send(PacketByteBuf buf);

    void write(PacketByteBuf buf);
}
