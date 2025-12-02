package dev.inteleonyx.armandillo.event;

import dev.inteleonyx.armandillo.core.event.ArmandilloEvent;
import dev.architectury.event.events.common.LifecycleEvent;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class TickEvent extends ArmandilloEvent {
    public TickEvent(String eventName) {
        super(eventName);
    }

    @Override
    protected void listenGameEvent() {
        LifecycleEvent.SERVER_STARTED.register(this::dispatch);
    }
}
