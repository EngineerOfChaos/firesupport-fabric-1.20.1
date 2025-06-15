package net.engineerofchaos.firesupport.block.entity;

import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BasicDirectionalTurretBlockEntity> DIRECTIONAL_TURRET_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(FireSupport.MOD_ID, "directional_turret_be"),
                    FabricBlockEntityTypeBuilder.create(BasicDirectionalTurretBlockEntity::new,
                            ModBlocks.DIRECTIONAL_TURRET).build()
            );

    public static void registerBlockEntities() {
        FireSupport.LOGGER.info("Registering block entities for " + FireSupport.MOD_ID);
    }
}
