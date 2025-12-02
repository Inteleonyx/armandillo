package dev.inteleonyx.armandillo.core.event;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import lombok.Getter;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

public abstract class ArmandilloModule {
    @Getter
    private final String moduleName;

    protected ArmandilloModule(String moduleName) {
        this.moduleName = moduleName;
    }

    public abstract LuaValue getModuleEngine();

    public void init() {

    }

    public void onPostLoad() {

    }
}
