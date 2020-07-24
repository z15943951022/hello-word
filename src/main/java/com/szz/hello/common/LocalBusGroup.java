package com.szz.hello.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author szz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LocalBusGroup {

    Class<?> switchClass();
}
