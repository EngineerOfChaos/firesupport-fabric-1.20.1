package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.engineerofchaos.firesupport.shell.CaseLength;
import net.engineerofchaos.firesupport.shell.Multipliers;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class S2CBulletVelocityPacket implements FSPacket{
    private final Vector3f velocity;
    private final Vector3f initialPos;
    private final int targetBulletID;
    private final int cal;
    private final int caseLength;
    private final int caseInset;
    private final int[] multipliers;
    private final PacketSender responseSender;

    public S2CBulletVelocityPacket (int targetBulletID, Vec3d velocity, Vec3d initialPos, int cal, CaseLength caseLength,
                                    float caseInset, int[] multipliers, PacketSender responseSender) {
        this.velocity = velocity.toVector3f();
        this.initialPos = initialPos.toVector3f();
        this.targetBulletID = targetBulletID;
        this.cal = cal;
        this.caseLength = caseLength.ordinal();
        this.caseInset = (int) Math.floor(caseInset * 100f);
        this.multipliers = multipliers;
        this.responseSender = responseSender;
    }

    @Override
    public void send(PacketByteBuf buf) {
        responseSender.sendPacket(FireSupportNetworkingConstants.S2C_BULLET_VELOCITY_PACKET_ID, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVector3f(velocity);
        buf.writeVector3f(initialPos);
        buf.writeVarInt(targetBulletID);
        buf.writeVarInt(cal);
        buf.writeVarInt(caseLength);
        buf.writeVarInt(caseInset);
        buf.writeIntArray(multipliers);
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf,
                              PacketSender responseSender) {
        Vector3f velocity = buf.readVector3f();
        Vector3f launchPos = buf.readVector3f();
        int entityID = buf.readVarInt();
        int cal = buf.readVarInt();
        int caseLengthOrdinal = buf.readVarInt();
        float caseInset = buf.readVarInt();
        int[] scaledMultipliersArray = buf.readIntArray();

        client.execute( () -> {
            if (client.world != null) {
                BulletEntity targetBullet = (BulletEntity) client.world.getEntityById(entityID);
                if (targetBullet != null){
                    targetBullet.setVelocityClient(velocity.x, velocity.y, velocity.z);
                    targetBullet.setVelocity(velocity.x, velocity.y, velocity.z);
                    targetBullet.setLaunchPos(new Vec3d(launchPos.x, launchPos.y, launchPos.z));
                    targetBullet.setCalibre(cal);
                    targetBullet.setCaseLength(CaseLength.getCaseLength(caseLengthOrdinal));
                    targetBullet.setCaseInset(caseInset / 100);
                    targetBullet.setMultipliers(Multipliers.fromScaledIntArray(scaledMultipliersArray));
                }
            }
        });
    }
}
