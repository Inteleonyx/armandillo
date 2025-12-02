package dev.inteleonyx.armandillo.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Inteleonyx. Created on 01/12/2025
 * @project armandillo
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ArmandilloRuntime {

}
