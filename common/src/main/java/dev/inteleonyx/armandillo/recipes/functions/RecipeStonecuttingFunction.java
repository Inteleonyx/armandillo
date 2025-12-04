package dev.inteleonyx.armandillo.recipes.functions;

import com.google.gson.JsonObject;
import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import dev.inteleonyx.armandillo.recipes.RecipeBuilder;
import dev.inteleonyx.armandillo.utils.ItemEntry;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Inteleonyx. Created on 04/12/2025
 * @project armandillo
 */

public class RecipeStonecuttingFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        LuaTable table =  args.arg(1).checktable();
        String result = table.checkjstring();
        String ingredientString = table.checkjstring();

        ItemEntry entry = ItemEntry.parseItemAmount(result);
        String resultString = entry.itemId;

        RecipeBuilder.StonecuttingRecipeBuilder recipeBuilder = new RecipeBuilder.StonecuttingRecipeBuilder(resultString);
        recipeBuilder.ingredient(ingredientString);

        JsonObject recipeJson = recipeBuilder.build();

        String path = resultString.replace(":", "_") + "_stonecutting_" + Integer.toHexString(recipeJson.hashCode());

        RuntimeDataRegistry.addRecipeData(ResourceLocation.fromNamespaceAndPath("armandillo", path), recipeJson);

        return LuaValue.NIL;
    }
}
