package net.engineerofchaos.firesupport.turret;

import net.engineerofchaos.firesupport.shell.CaseLength;
import net.minecraft.util.math.Vec3d;

public class Autoloader {
    private final float cyclePeriod;
    private final float barrelOffset;

    public Autoloader(int calibre, CaseLength caseLength, int fireRate, float barrelEndDistance,
                      Vec3d size, float weight) {
        this.cyclePeriod = 1200f / fireRate;
        this.barrelOffset = barrelEndDistance;
    }

    public float getCyclePeriod() {
        return cyclePeriod;
    }

    public float getBarrelOffset() {
        return barrelOffset;
    }
}
