package com.szz.hello.common;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private static final List<SwitchBean> DYNAMIC_SWITCH_BEANS = new CopyOnWriteArrayList<>();

    /**
     * 标签对象集合
     */
    private static final Map<String, List<SwitchBean>> labelContainer = new ConcurrentHashMap<>();


    public static void setDynamicSwitchBeans(SwitchBean switchBean) {
        DYNAMIC_SWITCH_BEANS.add(switchBean);
        List<Field> interfaceBusFields = switchBean.getInterfaceBusFields();
        if (null != interfaceBusFields && interfaceBusFields.size() > 0) {
            addLabel(interfaceBusFields, switchBean);
        }
    }

    /**
     * 虽然IOC初始化的过程是同步的，但还是加个锁心里舒服一点
     *
     * @param fields
     * @param switchBean
     */
    private static void addLabel(List<Field> fields, SwitchBean switchBean) {
        for (Field field : fields) {
            SelectorLabel selectorLabel;
            if (field == null || (selectorLabel = field.getType().getAnnotation(SelectorLabel.class)) == null) return;
            List<SwitchBean> switchBeans;
            if ((switchBeans = labelContainer.get(selectorLabel.value())) != null) {
                switchBeans.add(switchBean);
            } else {
                synchronized (labelContainer) {
                    if ((switchBeans = labelContainer.get(selectorLabel.value())) == null) {
                        List<SwitchBean> list = new CopyOnWriteArrayList<>();
                        list.add(switchBean);
                        labelContainer.put(selectorLabel.value(), list);
                    } else {
                        switchBeans.add(switchBean);
                    }
                }
            }
        }
    }

    public void switchRemoteALLBean() {
        for (SwitchBean switchBean : DYNAMIC_SWITCH_BEANS) {
            try {
                switchBean.switchoverRemoteBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchLocalALLBean() {
        for (SwitchBean switchBean : DYNAMIC_SWITCH_BEANS) {
            try {
                switchBean.switchoverLocalBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchRemoteALLBeanByLabel(String label) {
        List<SwitchBean> switchBeans = labelContainer.get(label);
        if (switchBeans == null || switchBeans.size() == 0) return;
        for (SwitchBean switchBean : switchBeans) {
            try {
                switchBean.switchoverRemoteBeanByLabel(label);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void switchLocalALLBeanByLabel(String label) {
        List<SwitchBean> switchBeans = labelContainer.get(label);
        if (switchBeans == null || switchBeans.size() == 0) return;
        for (SwitchBean switchBean : switchBeans) {
            try {
                switchBean.switchoverLocalBeanByLabel(label);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ObjectStatusVo> printAllStatus() {
        List<ObjectStatusVo> list = new ArrayList<>();
        for (SwitchBean switchBean : DYNAMIC_SWITCH_BEANS) {
            ObjectStatusVo info = null;
            try {
                info = switchBean.getInfo();
                list.add(info);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 定位切换
     * @param className 类名
     * @param fieldName 字段名
     * @param flag true是切local  false切feign
     */
    public void targetSwitch(String className, String fieldName, boolean flag) {
        for (SwitchBean dynamicSwitchBean : DYNAMIC_SWITCH_BEANS) {
            if (Objects.equals(className, dynamicSwitchBean.getClass().getName())) {
                try {
                    if (flag) {
                        dynamicSwitchBean.targetSwitchLocal(fieldName);
                    } else {
                        dynamicSwitchBean.targetSwitchRemote(fieldName);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 寻找引用这个类名得对象中得引用并切换指定至local或者remote
     * @param refClassName
     */
    public void targetSwitch(String refClassName,boolean flag){
        for (SwitchBean dynamicSwitchBean : DYNAMIC_SWITCH_BEANS) {
            if (flag){
                try {
                    dynamicSwitchBean.targetSwitchLocalByRefCN(refClassName);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
