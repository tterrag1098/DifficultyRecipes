package tterrag.difficultyrecipes.nei;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tterrag.difficultyrecipes.recipes.DifficultyRecipe;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapelessRecipeHandler;

import com.google.common.collect.Lists;

public class NEIShapelessDifficultyRecipe extends ShapelessRecipeHandler
{
    @Override
    public String getRecipeName()
    {
        return StatCollector.translateToLocal("difficultyrecipes.shapeless.name");
    }

    private List<DifficultyRecipe<ShapelessOreRecipe>> cached = Lists.newArrayList();

    @SuppressWarnings("unchecked")
    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("crafting"))
        {
            for (DifficultyRecipe<?> rec : DifficultyRecipe.allRecipes)
            {
                CachedShapelessRecipe cached = null;
                if (rec.getType() == ShapelessOreRecipe.class)
                {
                    DifficultyRecipe<ShapelessOreRecipe> recipe = (DifficultyRecipe<ShapelessOreRecipe>) rec;

                    cached = forgeShapelessRecipe(((DifficultyRecipe<ShapelessOreRecipe>) rec)
                            .getRecipe(Minecraft.getMinecraft().theWorld.difficultySetting));

                    if (cached == null)
                        continue;

                    arecipes.add(cached);
                    this.cached.add(recipe);
                }
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (DifficultyRecipe<?> rec : DifficultyRecipe.allRecipes)
        {
            if (rec.getType() == ShapelessOreRecipe.class)
            {
                DifficultyRecipe<ShapelessOreRecipe> recipe = (DifficultyRecipe<ShapelessOreRecipe>) rec;
                EnumDifficulty diff = Minecraft.getMinecraft().theWorld.difficultySetting;
                ShapelessOreRecipe irecipe = recipe.getRecipe(diff);
                if (irecipe != null && NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
                {
                    CachedShapelessRecipe cached = null;
                    cached = forgeShapelessRecipe(recipe.getRecipe(diff));
                    if (cached == null)
                        continue;
                    arecipes.add(cached);
                    this.cached.add(recipe);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        for (DifficultyRecipe<?> rec : DifficultyRecipe.allRecipes)
        {
            if (rec.getType() == ShapelessOreRecipe.class)
            {
                DifficultyRecipe<ShapelessOreRecipe> recipe = (DifficultyRecipe<ShapelessOreRecipe>) rec;
                CachedShapelessRecipe cached = null;
                cached = forgeShapelessRecipe(recipe.getRecipe(Minecraft.getMinecraft().theWorld.difficultySetting));

                if (cached == null || !cached.contains(cached.ingredients, ingredient.getItem()))
                    continue;

                if (cached.contains(cached.ingredients, ingredient))
                {
                    cached.setIngredientPermutation(cached.ingredients, ingredient);
                    arecipes.add(cached);
                    this.cached.add(recipe);
                }
            }
        }
    }
    
    @Override
    public CachedShapelessRecipe forgeShapelessRecipe(ShapelessOreRecipe recipe)
    {
        return recipe == null ? null : super.forgeShapelessRecipe(recipe);
    }

    @Override
    public void drawExtras(int recipe)
    {
        super.drawExtras(recipe);
        EnumDifficulty diff = Minecraft.getMinecraft().theWorld.difficultySetting;
        String s = I18n.format(diff.getDifficultyResourceKey());
        StringBuilder sb = new StringBuilder();
        Collection<EnumDifficulty> dupes = DifficultyRecipe.getDuplicatedRecipes(cached.get(recipe), diff);
        if (!dupes.isEmpty())
        {
            sb.append("Also: ");
            for (EnumDifficulty e : dupes)
            {
                sb.append(I18n.format(e.getDifficultyResourceKey()));
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        GuiDraw.drawString(s, 84, 10, DifficultyRecipe.getColorFor(diff), false);
        GuiDraw.drawStringC(sb.toString(), 83, 60, 0x666666, false);
    }
}
