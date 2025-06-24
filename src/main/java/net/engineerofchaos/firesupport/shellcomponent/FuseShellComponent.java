package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.entity.custom.BulletEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface FuseShellComponent {

    /**
     * Called every tick, checking if the fuse should detonate the payload, and if so, where.
     * @return Null if no detonation, Vec3d of location if it should.
     */
    @Nullable
    Vec3d checkFuseCondition(Vec3d velocity, Vec3d pos);

    /**
     * called on entity creation, use for the fuse to tell the entity things, such as what size to be
     */
    void initFuse(BulletEntity entity);
}
