package tterrag.difficultyrecipes;

import net.minecraft.item.crafting.IRecipe;
import tterrag.difficultyrecipes.util.Difficulty;

public interface IDifficultyRecipe<T extends IRecipe> extends IRecipe
{
    T getRecipe(Difficulty diff);

    Class<T> getType();
}
