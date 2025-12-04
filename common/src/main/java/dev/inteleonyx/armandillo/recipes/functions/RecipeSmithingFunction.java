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

public class RecipeSmithingFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        LuaTable table = args.arg(1).checktable();
        String result = table.get("1").checkjstring();
        String template = table.get("2").checkjstring();
        String base = table.get("3").checkjstring();
        String addition = table.get("4").checkjstring();

        ItemEntry entry = ItemEntry.parseItemAmount(result);
        String resultString = entry.itemId;

        RecipeBuilder.SmithingRecipeBuilder recipeBuilder = new RecipeBuilder.SmithingRecipeBuilder(result);
        recipeBuilder.template(template);
        recipeBuilder.addition(addition);
        recipeBuilder.base(base);

        JsonObject recipeJson = recipeBuilder.build();

        String path = resultString.replace(":", "_") + "_smithing_" + Integer.toHexString(recipeJson.hashCode());

        RuntimeDataRegistry.addRecipeData(ResourceLocation.fromNamespaceAndPath("armandillo", path), recipeJson);

        return LuaValue.NIL;
    }
}
