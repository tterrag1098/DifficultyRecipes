package tterrag.difficultyrecipes.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public interface IDifficultyRecipe<T extends IRecipe> extends IRecipe
{
    T getRecipe(Difficulty diff);

    Class<T> getType();
    
    Difficulty getDifficulty(World world);
}
