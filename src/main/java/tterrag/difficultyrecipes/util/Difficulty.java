package tterrag.difficultyrecipes.util;

import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;

/**
 * Mojang's {@link EnumDifficulty} causes compile errors for whatever reason.
 * 
 * So I made my own enum, with blackjack, and hookers!
 */
public enum Difficulty
{
    PEACEFUL, EASY, NORMAL, HARD;

    public String getUnlocName()
    {
        return EnumDifficulty.values()[ordinal()].getDifficultyResourceKey();
    }

    public String getLocName()
    {
        return StatCollector.translateToLocal(getUnlocName());
    }

    public static Difficulty get(EnumDifficulty diff)
    {
        return values()[diff.ordinal()];
    }
}
