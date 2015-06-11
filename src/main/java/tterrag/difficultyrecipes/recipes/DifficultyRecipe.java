package tterrag.difficultyrecipes.recipes;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import tterrag.difficultyrecipes.IDifficultyRecipe;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DifficultyRecipe<T extends IRecipe> implements IDifficultyRecipe<T>
{
    public static final List<DifficultyRecipe<?>> allRecipes = Lists.newArrayList();

    private EnumMap<EnumDifficulty, T> recipes = new EnumMap<>(EnumDifficulty.class);
    @Getter
    private Class<T> type;
    @Getter
    private EnumDifficulty defaultDiff;

    private transient IRecipe cur;

    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        cur = getRecipe(world.difficultySetting);
        if (cur != null && cur.matches(inv, world))
        {
            return true;
        }
        cur = null;
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return cur == null ? null : cur.getCraftingResult(inv);
    }

    @Override
    public int getRecipeSize()
    {
        return cur == null ? hasDefault() ? getRecipe(defaultDiff).getRecipeSize() : 0 : cur.getRecipeSize();
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return cur == null ? null : cur.getRecipeOutput();
    }

    @Override
    public T getRecipe(EnumDifficulty diff)
    {
        T rec = recipes.get(diff);
        return rec == null ? hasDefault() ? recipes.get(defaultDiff) : null : rec;
    }

    public boolean hasDefault()
    {
        return defaultDiff != null;
    }

    public static <T extends IRecipe> Collection<EnumDifficulty> getDuplicatedRecipes(DifficultyRecipe<T> recipe, EnumDifficulty actual)
    {
        List<EnumDifficulty> ret = Lists.newArrayList();
        T base = recipe.recipes.get(actual);
        for (EnumDifficulty e : EnumDifficulty.values())
        {
            T temp = recipe.recipes.get(e);
            // No recipe for the current difficulty, so we check for a possible default
            if (temp == null && recipe.hasDefault() && actual == recipe.defaultDiff)
            {
                ret.add(e);
            }
            // No recipe for the initial difficulty, so we check that this is either the default or defaulting
            else if ((temp == null || e == recipe.defaultDiff) && base == null && recipe.hasDefault() && e != actual)
            {
                ret.add(e);
            }
            // Otherwise if this is literally the same recipe duplicated, add
            else if (base != null && temp != null && temp.equals(base) && e != actual)
            {
                ret.add(e);
            }
        }
        return ret;
    }

    public static int getColorFor(EnumDifficulty diff)
    {
        // @formatter:off
        return diff == EnumDifficulty.PEACEFUL ? 0x555599 : 
               diff == EnumDifficulty.EASY     ? 0x669966 : 
               diff == EnumDifficulty.NORMAL   ? 0x997700 : 
                                                 0x990000;
        // @formatter:on
    }

    public static class Builder<T extends IRecipe>
    {
        private DifficultyRecipe<T> recipe;

        protected Builder(DifficultyRecipe<T> recipe, Class<T> type)
        {
            this.recipe = recipe;
            this.recipe.type = type;
        }

        public Builder<T> addRecipe(EnumDifficulty diff, T recipe)
        {
            this.recipe.recipes.put(diff, recipe);
            return this;
        }

        public Builder<T> setDefault(EnumDifficulty diff)
        {
            this.recipe.defaultDiff = diff;
            return this;
        }

        /**
         * Automatically registers to the GameRegistry
         */
        public DifficultyRecipe<T> build()
        {
            allRecipes.add(recipe);
            GameRegistry.addRecipe(recipe);
            return recipe;
        }
    }
}
