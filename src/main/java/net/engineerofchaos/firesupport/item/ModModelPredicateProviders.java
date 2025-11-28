package net.engineerofchaos.firesupport.item;

import net.engineerofchaos.firesupport.shell.ShellComponentUtil;
import net.engineerofchaos.firesupport.shell.ShellComponents;
import net.engineerofchaos.firesupport.shell.ShellUtil;
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

        ModelPredicateProviderRegistry.register(ModItems.TEST_SHELL, new Identifier("calibre"),
                (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return (float) ShellUtil.getCalibre(itemStack) / 1000;
        });

        ModelPredicateProviderRegistry.register(ModItems.TEST_SHELL, new Identifier("case_length"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    }
                    return (float) ShellUtil.getCaseLength(itemStack).ordinal() / 2;
                });
    }
}
