package dev.inteleonyx.armandillo.core.lang.functions;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.api.luaj.Varargs;
import dev.inteleonyx.armandillo.api.luaj.lib.VarArgFunction;
import dev.inteleonyx.armandillo.core.event.ArmandilloEvent;
import dev.inteleonyx.armandillo.core.registry.EventRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class ArmandilloEventFunction extends VarArgFunction {
    private final Map<String, LuaValue> registeredCallbacks = new HashMap<>();

    @Override
    public Varargs invoke(Varargs args) {
        LuaValue eventNameLua = args.arg(1);
        LuaValue callback = args.arg(2);

        if (!eventNameLua.isstring()) {
            throw new IllegalArgumentException("First argument must be a string (event name)");
        }
        if (!callback.isfunction()) {
            throw new IllegalArgumentException("Second argument must be a function (callback)");
        }

        String eventName = eventNameLua.tojstring();

        ArmandilloEvent event = EventRegistry.getInstance()
                .getEvent(eventName)
                .orElseThrow(() -> new IllegalArgumentException("Unknown event name: " + eventName));

        registeredCallbacks.put(eventName, callback);

        event.setLuaHandler(nativeEvent -> {
            try {
                LuaValue luaEvent = convertNativeEventToLua(nativeEvent);
                callback.call(luaEvent);
            } catch (Exception e) {
                System.err.println("[Armandillo] Error calling Lua event callback for '" + eventName + "': " + e.getMessage());
                e.printStackTrace();
            }
        });

        return LuaValue.NIL;
    }

    private LuaValue convertNativeEventToLua(Object nativeEvent) {
        return LuaValue.userdataOf(nativeEvent);
    }
}

