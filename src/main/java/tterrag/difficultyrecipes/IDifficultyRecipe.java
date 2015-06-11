package tterrag.difficultyrecipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.EnumDifficulty;

public interface IDifficultyRecipe<T extends IRecipe> extends IRecipe
{
    T getRecipe(EnumDifficulty diff);

    Class<T> getType();
}
