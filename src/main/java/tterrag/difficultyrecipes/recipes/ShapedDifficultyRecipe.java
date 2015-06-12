package tterrag.difficultyrecipes.recipes;

import tterrag.difficultyrecipes.DifficultyRecipes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShapedDifficultyRecipe extends DifficultyRecipe<ShapedOreRecipe>
{
    static
    {
        RecipeSorter.register(DifficultyRecipes.MODID + ":shaped", ShapedDifficultyRecipe.class, Category.SHAPED, "after:forge:shapedore");
    }

    public static Builder<ShapedOreRecipe> builder()
    {
        return new Builder<ShapedOreRecipe>(new ShapedDifficultyRecipe(), ShapedOreRecipe.class);
    }
}
