package net.engineerofchaos.firesupport.entity;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.custom.BulletEntityOld;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<BulletEntityOld> BULLET_THROWN = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(FireSupport.MOD_ID, "bullet_thrown"),
            FabricEntityTypeBuilder.<BulletEntityOld>create(SpawnGroup.MISC, BulletEntityOld::new)
                    .dimensions(EntityDimensions.changing(0.2f, 0.2f))
                    .forceTrackedVelocityUpdates(false)
                    .trackRangeChunks(16)
                    .build());

    public static void registerModEntities() {
        FireSupport.LOGGER.info("Registering entities for " + FireSupport.MOD_ID);
    }
}
