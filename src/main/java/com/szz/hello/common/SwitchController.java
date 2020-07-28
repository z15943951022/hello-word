package com.szz.hello.common;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author szz
 */
@Component
public class SwitchController {

    /**
     * 观察对象集合
     */
    private static final List<DynamicSwitchable> dynamicSwitches = new CopyOnWriteArrayList<>();

    /**
     * 标签对象集合
     */
    private static final Map<String,List<DynamicSwitchable>> labelContainer = new ConcurrentHashMap<>();


    public static void setDynamicSwitches(DynamicSwitchable dynamicSwitch) {
        dynamicSwitches.add(dynamicSwitch);
        List<Field> interfaceBusFields = dynamicSwitch.getInterfaceBusFields();
        if (null != interfaceBusFields && interfaceBusFields.size() > 0){
            addLabel(interfaceBusFields,dynamicSwitch);
        }
    }

    /**
     * 虽然IOC初始化的过程是同步的，但还是加个锁心里舒服一点
     * @param fields
     * @param dynamicSwitchable
     */
    private static void addLabel(List<Field> fields,DynamicSwitchable dynamicSwitchable){
        for (Field field : fields) {
            SelectorLabel selectorLabel;
            if (field == null || (selectorLabel = field.getType().getAnnotation(SelectorLabel.class)) == null) return;
            List<DynamicSwitchable> dynamicSwitchables;
            if ((dynamicSwitchables = labelContainer.get(selectorLabel.value())) != null){
                dynamicSwitchables.add(dynamicSwitchable);
            } else {
                synchronized (labelContainer){
                    if ((dynamicSwitchables = labelContainer.get(selectorLabel.value()))  == null){
                        List<DynamicSwitchable> list = new CopyOnWriteArrayList<>();
                        list.add(dynamicSwitchable);
                        labelContainer.put(selectorLabel.value(),list);
                    }else {
                        dynamicSwitchables.add(dynamicSwitchable);
                    }
                }
            }
        }
    }

    public void switchRemoteALLBean(){
        for (DynamicSwitchable dynamicSwitch : dynamicSwitches) {
            try {
                dynamicSwitch.switchoverRemoteBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchLocalALLBean(){
        for (DynamicSwitchable dynamicSwitch : dynamicSwitches) {
            try {
                dynamicSwitch.switchoverLocalBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchRemoteALLBeanByLabel(String label){
        List<DynamicSwitchable> dynamicSwitchables = labelContainer.get(label);
        if (dynamicSwitchables == null || dynamicSwitchables.size() == 0) return;
        for (DynamicSwitchable dynamicSwitchable : dynamicSwitchables) {
            try {
                dynamicSwitchable.switchoverRemoteBeanByLabel(label);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchLocalALLBeanByLabel(String label){
        List<DynamicSwitchable> dynamicSwitchables = labelContainer.get(label);
        if (dynamicSwitchables == null || dynamicSwitchables.size() == 0) return;
        for (DynamicSwitchable dynamicSwitchable : dynamicSwitchables) {
            try {
                dynamicSwitchable.switchoverLocalBeanByLabel(label);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ObjectStatusVo> printAllStatus(){
        List<ObjectStatusVo> map = new ArrayList<>();
        for (DynamicSwitchable dynamicSwitch : dynamicSwitches) {
            ObjectStatusVo objectStatusVo = new ObjectStatusVo();
            objectStatusVo.setClassName(dynamicSwitch.getClass().getName());
            objectStatusVo.setStatus(dynamicSwitch.getFlag());
        }
        return map;
    }
}
