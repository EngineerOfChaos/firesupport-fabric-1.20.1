package net.engineerofchaos.firesupport.datagen;

import net.engineerofchaos.firesupport.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // Look at RecipeProvider class to see what is available
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SANDBAG, 4)
                .pattern(" P ")
                .pattern("PSP")
                .pattern(" P ")
                .input('P', Items.PAPER)
                .input('S', ItemTags.SAND)
                .criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SANDBAG)));

    }
}
