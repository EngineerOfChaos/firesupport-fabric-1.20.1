package net.engineerofchaos.firesupport.entity.damage;

import net.engineerofchaos.firesupport.FireSupport;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> BULLET_KINETIC_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
            new Identifier("firesupport", "projectile_kinetic"));

    public static void registerModDamageTypes() {
        FireSupport.LOGGER.info("Registering mod damage types for " + FireSupport.MOD_ID);
    }
}
