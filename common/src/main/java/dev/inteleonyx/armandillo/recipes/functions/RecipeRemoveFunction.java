package dev.inteleonyx.armandillo.recipes.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;
import dev.inteleonyx.armandillo.recipes.RecipesArmandilloModule;

/**
 * @author Inteleonyx. Created on 26/11/2025
 * @project armandillo
 */

public class RecipeRemoveFunction extends OneArgFunction {
    private final RecipesArmandilloModule module;

    public RecipeRemoveFunction(RecipesArmandilloModule module) {
        this.module = module;
    }

    @Override
    public LuaValue call(LuaValue var1) {
        return LuaValue.NIL;
    }
}
