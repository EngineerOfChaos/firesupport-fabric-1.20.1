package net.engineerofchaos.firesupport.shell;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

public class TimedFuseShellComponent extends AdditionalDataShellComponent implements FuseShellComponent {

    public TimedFuseShellComponent(Multipliers multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    @Override
    float getDefaultData() {
        return 60;
    }

    @Override
    public @Nullable Vec3d checkFuseCondition(BulletEntity entity, HashMap<String, Float> additionalData) {
        int timeAlive = entity.age;
        //float detonationTime = additionalData.get(ShellComponents.TIMED_FUSE.getRawID());
        float detonationTime = this.getData(additionalData);
        if (timeAlive < detonationTime) {
            return null;
        }
        float stepDistance = timeAlive - detonationTime;
        return new Vec3d(entity.getPos().add(entity.getVelocity().multiply(stepDistance)).toVector3f());
    }

    @Override
    public void initFuse(BulletEntity entity) {

    }
}
