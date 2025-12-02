package dev.inteleonyx.armandillo.registry;

import dev.inteleonyx.armandillo.core.event.ArmandilloEvent;
import dev.inteleonyx.armandillo.core.event.ArmandilloModule;
import dev.inteleonyx.armandillo.event.TickEvent;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public class ArmandilloRegistries {
    public static final ArmandilloRegistry<ArmandilloModule> MODULES = new ArmandilloRegistry<ArmandilloModule>();
    public static final ArmandilloRegistry<ArmandilloEvent> EVENTS = new ArmandilloRegistry<ArmandilloEvent>();

    static {
        EVENTS.register("tick", () -> new TickEvent("tick"));
    }

    private ArmandilloRegistries() {}
}
