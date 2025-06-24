package net.engineerofchaos.firesupport.shellcomponent;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class HighExplosiveShellComponent extends ShellComponent implements PayloadShellComponent {
    public HighExplosiveShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    @Override
    public boolean executePayloadEffect(Vec3d pos, World world, float payloadMultiplier) {
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), payloadMultiplier, false, World.ExplosionSourceType.NONE);
        return true;
    }
}
