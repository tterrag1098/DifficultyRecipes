package tterrag.difficultyrecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tterrag.difficultyrecipes.recipes.ShapedDifficultyRecipe;
import tterrag.difficultyrecipes.recipes.ShapelessDifficultyRecipe;
import tterrag.difficultyrecipes.util.Difficulty;
import tterrag.difficultyrecipes.util.IDifficultyCallback;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import static tterrag.difficultyrecipes.DifficultyRecipes.*;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class DifficultyRecipes
{
    public static final String MODID = "difficultyrecipes";
    public static final String NAME = "Difficulty Recipes";
    public static final String VERSION = "@VERSION@";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
//        addDebugRecipes();
    }

    @SuppressWarnings("unused")
    private void addDebugRecipes() {
        ShapedDifficultyRecipe.builder()
            .addRecipe(Difficulty.PEACEFUL, new ShapedOreRecipe(new ItemStack(Items.snowball), "x ", " x", 'x', Blocks.pumpkin))
            .addRecipe(Difficulty.EASY, new ShapedOreRecipe(Items.snowball, "x ", " x", 'x', Items.bone))
            .setDefault(Difficulty.EASY)
            .build();

        ShapedDifficultyRecipe.builder()
            .addRecipe(Difficulty.EASY, new ShapedOreRecipe(new ItemStack(Items.snowball), "x ", " x", 'x', Blocks.stone))
            .addRecipe(Difficulty.HARD, new ShapedOreRecipe(Items.snowball, "x ", " x", 'x', Blocks.obsidian))
            .setDefault(Difficulty.EASY)
            .build();

        ShapedDifficultyRecipe.builder()
            .addRecipe(Difficulty.EASY, new ShapedOreRecipe(new ItemStack(Items.snowball), "x ", " x", 'x', Items.diamond))
            .addRecipe(Difficulty.NORMAL, new ShapedOreRecipe(Items.snowball, "x ", " x", 'x', Items.emerald))
            .build();

        ShapelessDifficultyRecipe.builder()
            .addRecipe(Difficulty.PEACEFUL, new ShapelessOreRecipe(new ItemStack(Items.snowball), Blocks.pumpkin))
            .addRecipe(Difficulty.EASY, new ShapelessOreRecipe(Items.snowball, Items.bone))
            .setDefault(Difficulty.EASY)
            .build();

        ShapelessDifficultyRecipe.builder()
            .addRecipe(Difficulty.EASY, new ShapelessOreRecipe(new ItemStack(Items.snowball), Blocks.stone))
            .addRecipe(Difficulty.HARD, new ShapelessOreRecipe(Items.snowball, Blocks.obsidian))
            .setDefault(Difficulty.EASY)
            .build();

        ShapelessDifficultyRecipe.builder()
            .addRecipe(Difficulty.EASY, new ShapelessOreRecipe(new ItemStack(Items.snowball), Items.diamond))
            .addRecipe(Difficulty.NORMAL, new ShapelessOreRecipe(Items.snowball, Items.emerald))
            .setCallback(new IDifficultyCallback()
            {
                @Override
                public Difficulty getDifficulty(World world)
                {
                    return Difficulty.values()[3 - world.difficultySetting.ordinal()];
                }
            })
            .build();
    }
}
