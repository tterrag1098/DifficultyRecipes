package tterrag.difficultyrecipes.util;

import net.minecraft.world.World;

public interface IDifficultyCallback
{
    Difficulty getDifficulty(World world);
}
