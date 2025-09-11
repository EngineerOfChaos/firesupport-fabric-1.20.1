package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.entity.custom.BulletEntityOld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class ProxyFuseShellComponent extends AdditionalDataShellComponent implements FuseShellComponent{

    public ProxyFuseShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    @Override
    float getDefaultData() {
        return 10;
    }

    @Override
    public @Nullable Vec3d checkFuseCondition(BulletEntityOld entity, HashMap<Integer, Float> additionalData) {
        return null;
    }

    @Override
    public void initFuse(BulletEntityOld entity) {
        entity.proxyMult = getData(entity.additionalData);
    }
}
