package net.engineerofchaos.firesupport.shell;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ProxyFuseShellComponent extends AdditionalDataShellComponent implements FuseShellComponent {

    public ProxyFuseShellComponent(Multipliers multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    @Override
    float getDefaultData() {
        return 10;
    }

    @Override
    public @Nullable Vec3d checkFuseCondition(BulletEntity entity, HashMap<String, Float> additionalData) {
        return null;
    }

    @Override
    public void initFuse(BulletEntity entity) {
        entity.proxyMult = getData(entity.additionalData);
    }
}
