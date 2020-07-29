package com.szz.hello.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author szz
 */
@Component
public class BusIOC implements ApplicationContextAware {

    /**
     * 只读
     */
    public static final Map<String, Object> beansWithFeignBusGroup = new HashMap<>();

    /**
     * 只读
     */
    public static final Map<String, Object> beansWithLocalBusGroup = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> FeignBusGroupMap = applicationContext.getBeansWithAnnotation(RemoteBusGroup.class);
        Map<String, Object> LocalBusGroupMap = applicationContext.getBeansWithAnnotation(LocalBusGroup.class);
        if (null != FeignBusGroupMap){
            for (Object value : FeignBusGroupMap.values()) {
                beansWithFeignBusGroup.put(value.getClass().getName(),value);
            }
        }
        if (null != LocalBusGroupMap){
            for (Object value : LocalBusGroupMap.values()) {
                beansWithLocalBusGroup.put(value.getClass().getName(),value);
            }
        }
    }


}
