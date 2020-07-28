package com.szz.hello.common;

import java.lang.annotation.*;

/**
 * @author szz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SelectorLabel {

    String value() default "default";
}
