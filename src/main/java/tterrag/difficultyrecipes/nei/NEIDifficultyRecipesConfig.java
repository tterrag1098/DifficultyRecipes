package tterrag.difficultyrecipes.nei;

import net.minecraft.item.ItemStack;
import tterrag.difficultyrecipes.DifficultyRecipes;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class NEIDifficultyRecipesConfig implements IConfigureNEI
{
    @Override
    public String getName()
    {
        return DifficultyRecipes.NAME;
    }

    @Override
    public String getVersion()
    {
        return DifficultyRecipes.VERSION;
    }

    @Override
    public void loadConfig()
    {
        API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "limestone_slab_top")));
        API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_slab_top")));
        API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_pillar_slab_top")));

        TemplateRecipeHandler handler = new NEIShapedDifficultyRecipe();
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
        
        handler = new NEIShapelessDifficultyRecipe();
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
}
