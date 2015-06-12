package tterrag.difficultyrecipes.recipes;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import tterrag.difficultyrecipes.IDifficultyRecipe;
import tterrag.difficultyrecipes.util.Difficulty;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.GameRegistry;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DifficultyRecipe<T extends IRecipe> implements IDifficultyRecipe<T>
{
    public static final List<DifficultyRecipe<?>> allRecipes = Lists.newArrayList();

    private Map<Difficulty, T> recipes = new EnumMap<Difficulty, T>(Difficulty.class);

    @Getter
    private Class<T> type;
    @Getter
    private Difficulty defaultDiff;

    private transient IRecipe cur;

    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        cur = getRecipe(Difficulty.get(world.difficultySetting));
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

    public T getRecipe(EnumDifficulty diff)
    {
        return getRecipe(Difficulty.get(diff));
    }

    @Override
    public T getRecipe(Difficulty diff)
    {
        T rec = recipes.get(diff);
        return rec == null ? hasDefault() ? recipes.get(defaultDiff) : null : rec;
    }

    public boolean hasDefault()
    {
        return defaultDiff != null;
    }

    public static <T extends IRecipe> Collection<Difficulty> getDuplicatedRecipes(DifficultyRecipe<T> recipe, Difficulty actual)
    {
        List<Difficulty> ret = Lists.newArrayList();
        T base = recipe.recipes.get(actual);
        for (Difficulty e : Difficulty.values())
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

        public Builder<T> addRecipe(Difficulty diff, T recipe)
        {
            this.recipe.recipes.put(diff, recipe);
            return this;
        }

        public Builder<T> setDefault(Difficulty diff)
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
