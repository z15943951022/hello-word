package com.szz.hello.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author szz
 */
public class DynamicSwitchable implements ApplicationContextAware {


    private BusFlag flag = BusFlag.FeignBusGroup;

    /**
     * 只读
     */
    private static final Map<String, Object> beansWithFeignBusGroup = new HashMap<>();

    /**
     * 只读
     */
    private static final Map<String, Object> beansWithLocalBusGroup = new HashMap<>();


    private List<Field> fields;


    public DynamicSwitchable() {
        SwitchController.setDynamicSwitches(this);
        fields = getInterfaceBusFields();
    }


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


    /**
     * 切换到Local
     * @throws IllegalAccessException
     */
    public void switchoverLocalBean() throws IllegalAccessException {
        Class<? extends DynamicSwitchable> aClass = this.getClass();
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
        flag = BusFlag.LocalBusGroup;
    }

    /**
     * 切换到Remote
     * @throws IllegalAccessException
     */
    public void switchoverRemoteBean() throws IllegalAccessException {
        Class<? extends DynamicSwitchable> aClass = this.getClass();
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
        flag = BusFlag.FeignBusGroup;
    }

    /**
     * 根据标签切换到Remote
     * @throws IllegalAccessException
     */
    public void switchoverRemoteBeanByLabel(String label) throws IllegalAccessException {
        if (fields == null || fields.size() == 0 || label == null || "".equals(label)) return;
        for (Field field : fields) {
            field.setAccessible(true);
            LocalBusGroup localAnnotation;
            if (label.equals(field.getType().getAnnotation(SelectorLabel.class).value()) && (localAnnotation = field.get(this).getClass().getAnnotation(LocalBusGroup.class)) != null){
                Class<?> switchClass = localAnnotation.switchClass();
                Object localBusObj = beansWithFeignBusGroup.get(switchClass.getName());
                if (null != localBusObj){
                    try {
                        field.set(this,localBusObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 根据标签切换到Local
     * @throws IllegalAccessException
     */
    public void switchoverLocalBeanByLabel(String label) throws IllegalAccessException {
        if (fields == null || fields.size() == 0 || label == null || "".equals(label)) return;
        for (Field field : fields) {
            field.setAccessible(true);
            RemoteBusGroup FeignAnnotation;
            if (label.equals(field.getType().getAnnotation(SelectorLabel.class).value()) && (FeignAnnotation = field.get(this).getClass().getAnnotation(RemoteBusGroup.class)) != null){
                Class<?> switchClass = FeignAnnotation.switchClass();
                Object localBusObj = beansWithLocalBusGroup.get(switchClass.getName());
                if (null != localBusObj){
                    try {
                        field.set(this,localBusObj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public List<Field> getInterfaceBusFields() {
        List<Field> fields = new ArrayList<>();
        Class<? extends DynamicSwitchable> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (getFieldLabel(field) != null){
                fields.add(field);
            }
        }
        return fields;
    }


    private SelectorLabel getFieldLabel(Field field) {
        SelectorLabel selectorLabel = field.getType().getAnnotation(SelectorLabel.class);
        return selectorLabel;
    }


    public BusFlag getFlag() {
        return flag;
    }
}
