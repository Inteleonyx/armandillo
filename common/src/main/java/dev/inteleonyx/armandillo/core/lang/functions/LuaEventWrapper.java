package dev.inteleonyx.armandillo.core.lang.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.lib.OneArgFunction;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class LuaEventWrapper extends LuaValue {


    private class RegisterFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (!arg.isfunction()) {
                throw new IllegalArgumentException("Expected function as argument");
            }

            luaCallback = arg;
            event.setLuaHandler(nativeEvent -> {
                luaCallback.call();
            });

            return LuaValue.NIL;
        }
    }
}
