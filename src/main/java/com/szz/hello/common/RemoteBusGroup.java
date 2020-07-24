package com.szz.hello.common;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author szz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Primary
public @interface RemoteBusGroup {

    Class<?> switchClass();
}
