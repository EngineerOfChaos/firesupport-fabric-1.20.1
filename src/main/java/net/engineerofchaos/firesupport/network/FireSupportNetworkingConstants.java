package net.engineerofchaos.firesupport.network;

import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.util.Identifier;

public class FireSupportNetworkingConstants {
    public static final Identifier S2C_BULLET_VELOCITY_PACKET_ID =
            new Identifier(FireSupport.MOD_ID, "bullet_velocity");

    public static final Identifier C2S_BULLET_VELOCITY_REQUEST_PACKET_ID =
            new Identifier(FireSupport.MOD_ID, "bullet_velocity_request");

    public static final Identifier C2S_MANUAL_TURRET_FIRE_ID =
            new Identifier(FireSupport.MOD_ID, "manual_turret_fire");
}
