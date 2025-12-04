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

public class RecipeCampfireFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        LuaTable table = args.arg(1).checktable();
        String result = table.get(1).checkjstring();
        String ingredient = table.get(2).checkjstring();

        int cookingTime = table.get(3).optint(0);
        double experience = table.get(4).optdouble(0.0);

        ItemEntry entry = ItemEntry.parseItemAmount(result);
        String resultString = entry.itemId;

        RecipeBuilder.SmeltingRecipeBuilder recipeBuilder = RecipeBuilder.campfire(result);
        recipeBuilder.ingredient(ingredient);

        if (cookingTime > 0) recipeBuilder.time(cookingTime);
        if (experience > 0) recipeBuilder.experience((float) experience);

        JsonObject recipeJson = recipeBuilder.build();

        String path = resultString.replace(":", "_") + "_campfire_" + Integer.toHexString(recipeJson.hashCode());

        RuntimeDataRegistry.addRecipeData(ResourceLocation.fromNamespaceAndPath("armandillo", path), recipeJson);

        return LuaValue.NIL;
    }
}
