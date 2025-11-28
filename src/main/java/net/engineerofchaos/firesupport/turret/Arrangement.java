package net.engineerofchaos.firesupport.turret;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arrangement {
    private final ArrayList<Arrangements.Pivot> pivots;

    public Arrangement(Arrangements.Pivot... pivots) {
        this.pivots = new ArrayList<>(Arrays.asList(pivots));
    }

    public List<Arrangements.Pivot> getPivots() {
        return pivots;
    }
}
