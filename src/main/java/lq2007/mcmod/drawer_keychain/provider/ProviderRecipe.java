package lq2007.mcmod.drawer_keychain.provider;

import com.jaquadro.minecraft.storagedrawers.core.ModItems;
import lq2007.mcmod.drawer_keychain.KeyChain;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class ProviderRecipe extends RecipeProvider {

    public ProviderRecipe(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(KeyChain.INSTANCE.getKey())
                .group("keychain")
                .requires(Items.IRON_NUGGET)
                .requires(ModItems.DRAWER_KEY)
                .requires(ModItems.QUANTIFY_KEY)
                .requires(ModItems.SHROUD_KEY)
                .unlockedBy("has_keys", has(KeyChain.INSTANCE.getKey()))
                .save(consumer);
    }
}
