package com.szz.hello.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author szz
 */
@Component
public class DynamicSwitch implements ApplicationContextAware {

    private BusFlag flag = BusFlag.FeignBusGroup;

    private static final Map<String, Object> beansWithFeignBusGroup = new HashMap<>();

    private static final Map<String, Object> beansWithLocalBusGroup = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SwitchController.setDynamicSwitches(this);
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


    /**
     * 切换到Remote
     * @throws IllegalAccessException
     */
    public void switchoverRemoteBean() throws IllegalAccessException {
        Class<? extends DynamicSwitch> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            RemoteBusGroup FeignAnnotation;
            if ((FeignAnnotation = declaredField.get(this).getClass().getAnnotation(RemoteBusGroup.class)) != null){
                Class<?> switchClass = FeignAnnotation.switchClass();
                Object localBusObj = beansWithLocalBusGroup.get(switchClass.getName());
                if (null != localBusObj){
                    try {
                        declaredField.set(this,localBusObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        flag = BusFlag.FeignBusGroup;
    }


    /**
     * 切换到Local
     * @throws IllegalAccessException
     */
    public void switchoverLocalBean() throws IllegalAccessException {
        Class<? extends DynamicSwitch> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            LocalBusGroup localAnnotation;
            if ((localAnnotation = declaredField.get(this).getClass().getAnnotation(LocalBusGroup.class)) != null){
                Class<?> switchClass = localAnnotation.switchClass();
                Object localBusObj = beansWithFeignBusGroup.get(switchClass.getName());
                if (null != localBusObj){
                    try {
                        declaredField.set(this,localBusObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        flag = BusFlag.LocalBusGroup;
    }





}
