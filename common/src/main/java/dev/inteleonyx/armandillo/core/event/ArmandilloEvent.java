package dev.inteleonyx.armandillo.core.event;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.utils.annotations.ArmandilloRuntime;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

@ArmandilloRuntime
public abstract class ArmandilloEvent {
    @Getter
    protected final String eventName;

    @Setter
    protected Consumer<Object> luaHandler;

    protected ArmandilloEvent(String eventName) {
        this.eventName = eventName;
        LifecycleEvent.SERVER_STARTED.register(server -> listenGameEvent());
    }

    protected abstract void listenGameEvent();

    protected void dispatch(Object nativeEvent) {
        if (luaHandler != null) {
            luaHandler.accept(nativeEvent);
        }
    }


}
