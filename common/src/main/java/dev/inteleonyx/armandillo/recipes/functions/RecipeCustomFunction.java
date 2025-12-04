package dev.inteleonyx.armandillo.recipes.functions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.inteleonyx.armandillo.api.luaj.LuaTable;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.registry.RuntimeDataRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Inteleonyx. Created on 04/12/2025
 * @project armandillo
 */

public class RecipeCustomFunction extends VarArgFunction {
    @Override
    public Varargs invoke(Varargs args) {
        LuaTable table = args.arg(1).checktable();
        String jsonRecipeString = table.get(1).checkjstring();

        JsonElement element =  JsonParser.parseString(jsonRecipeString);
        JsonObject object;

        if (element.isJsonObject()) {
            object = element.getAsJsonObject();
        } else {
            String type = element.isJsonArray() ? "JsonArray" :
                    element.isJsonPrimitive() ? "JsonPrimitive" :
                            "Unknown Type";
            throw new IllegalArgumentException("JSON string is valid, but represents a " + type +
                    ". A JsonObject was expected (e.g., starting and ending with curly braces {}).");
        }

        JsonObject resultObject = object.get("result").getAsJsonObject();

        String resultString = getResultId(resultObject);

        String path = resultString.replace(":", "_") + "_custom_" + Integer.toHexString(object.hashCode());

        RuntimeDataRegistry.addRecipeData(ResourceLocation.fromNamespaceAndPath("armandillo", path), object);

        return LuaValue.NIL;
    }

    public static String getResultId(JsonObject recipeJson) {
        JsonObject resultObject;

        if (recipeJson.has("result") && recipeJson.get("result").isJsonObject()) {
            resultObject = recipeJson.get("result").getAsJsonObject();
        } else {
            resultObject = recipeJson;
        }

        if (resultObject.has("item")) {
            return resultObject.get("item").getAsString();
        }

        if (resultObject.has("fluid")) {
            return resultObject.get("fluid").getAsString();
        }

        return null;
    }
}
