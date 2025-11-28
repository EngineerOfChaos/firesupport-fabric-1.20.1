package net.engineerofchaos.firesupport.shell;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface PayloadShellComponent {

    /**
     * Called when the fuse detonates the payload, or on contact.
     * @return boolean if the shell is destroyed on activation
     */
    boolean executePayloadEffect(Vec3d pos, World world, float payloadMultiplier);
}
