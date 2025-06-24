package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimedFuseShellComponent extends AdditionalDataShellComponent implements FuseShellComponent {

    private float timer;

    public TimedFuseShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    @Override
    float setDefaultData() {
        return 60;
    }

    @Override
    public @Nullable Vec3d checkFuseCondition(Vec3d velocity, Vec3d pos) {
        timer--;
        if (this.timer <= 0) {
            return pos.add(velocity.multiply(1 + timer)) ;
        }
        return null;
    }

    @Override
    public void initFuse(BulletEntity entity) {
        timer = this.getData();
    }
}
