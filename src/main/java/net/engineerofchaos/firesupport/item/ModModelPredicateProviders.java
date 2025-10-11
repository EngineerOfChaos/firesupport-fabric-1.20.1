package net.engineerofchaos.firesupport.item;

import net.engineerofchaos.firesupport.shellcomponent.ShellComponentUtil;
import net.engineerofchaos.firesupport.shellcomponent.ShellComponents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicateProviders {

    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(ModItems.TEST_SHELL, new Identifier("sabot"),
                (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return ShellComponentUtil.getComponents(itemStack).contains(ShellComponents.SABOT) ? 1.0F : 0.0F;
        });
    }
}
