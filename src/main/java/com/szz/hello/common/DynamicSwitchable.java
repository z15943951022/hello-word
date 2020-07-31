package com.szz.hello.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author szz
 */
public class DynamicSwitchable implements SwitchBean {

    /**
     * 只读
     */
    private static final Map<String, Object> beansWithFeignBusGroup = BusContainer.beansWithFeignBusGroup;

    /**
     * 只读
     */
    private static final Map<String, Object> beansWithLocalBusGroup = BusContainer.beansWithLocalBusGroup;


    /**
     * 带标签得域
     */
    private List<Field> labelFields;

    /**
     * 所有可切换得域
     */
    private List<Field> usableFields;


    public DynamicSwitchable() {
        SwitchController.setDynamicSwitchBeans(this);
        labelFields = getInterfaceBusFields();
    }


    /**
     * 切换到Local
     * @throws IllegalAccessException
     */
    public void switchoverLocalBean() throws IllegalAccessException {
        List<Field> declaredFields = getUsableFields();
        for (Field declaredField : declaredFields) {
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
    }

    /**
     * 切换到Remote
     * @throws IllegalAccessException
     */
    public void switchoverRemoteBean() throws IllegalAccessException {
        List<Field> declaredFields = getUsableFields();
        for (Field declaredField : declaredFields) {
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
    }

    /**
     * 根据标签切换到Remote
     * @throws IllegalAccessException
     */
    public void switchoverRemoteBeanByLabel(String label) throws IllegalAccessException {
        if (labelFields == null || labelFields.size() == 0 || label == null || "".equals(label)) return;

        for (Field field : labelFields) {
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
        if (labelFields == null || labelFields.size() == 0 || label == null || "".equals(label)) return;

        for (Field field : labelFields) {
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

    @Override
    public void targetSwitchLocal(String fieldName) throws NoSuchFieldException {
        Field field = this.getClass().getDeclaredField(fieldName);
        if (null != field) {
            field.setAccessible(true);
            try {
                RemoteBusGroup remoteBusGroup = field.get(this).getClass().getAnnotation(RemoteBusGroup.class);
                Object obj;
                if (remoteBusGroup != null && (obj = beansWithLocalBusGroup.get(remoteBusGroup.switchClass().getName())) != null){
                    field.set(this,obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void targetSwitchRemote(String fieldName) throws NoSuchFieldException {
        Field field = this.getClass().getDeclaredField(fieldName);
        if (null != field) {
            field.setAccessible(true);
            try {
                LocalBusGroup localBusGroup = field.get(this).getClass().getAnnotation(LocalBusGroup.class);
                Object obj;
                if (localBusGroup != null && (obj = beansWithLocalBusGroup.get(localBusGroup.switchClass().getName())) != null){
                    field.set(this,obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取所有带标签的域
     * @return
     */
    @Override
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

    @Override
    public ObjectStatusVo getInfo() {
        ObjectStatusVo objectStatusVo = new ObjectStatusVo();
        AtomicBoolean localFlag = new AtomicBoolean(false);
        AtomicBoolean remoteFlag = new AtomicBoolean(false);

        List<Field> allFields = getUsableFields();
        usableFields = allFields.size() > 0 ? allFields : null;
        if (usableFields != null) {
            List<ObjectStatusVo.SubField> collect = usableFields.stream().map(f -> {
                f.setAccessible(true);
                SelectorLabel selectorLabel = f.getType().getAnnotation(SelectorLabel.class);
                BusStatus status = BusStatus.OtherBus;
                try {
                    LocalBusGroup localBusGroup = f.get(this).getClass().getAnnotation(LocalBusGroup.class);
                    RemoteBusGroup remoteBusGroup = f.get(this).getClass().getAnnotation(RemoteBusGroup.class);
                    if (localBusGroup != null) {
                        status = BusStatus.LocalBus;
                        localFlag.set(true);
                    } else if (remoteBusGroup != null) {
                        status = BusStatus.FeignBus;
                        remoteFlag.set(true);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                ObjectStatusVo.SubField subField = new ObjectStatusVo.SubField();
                subField.setFieldName(f.getName());
                subField.setLabel(selectorLabel != null ? selectorLabel.value() : null);
                subField.setStatus(status);
                return subField;
            }).collect(Collectors.toList());

            objectStatusVo.setSubField(collect);
            objectStatusVo.setClassName(this.getClass().getName());
            if (localFlag.get() && remoteFlag.get()){
                objectStatusVo.setStatus(BusStatus.MixedBus);
            }else if (localFlag.get() && !remoteFlag.get()){
                objectStatusVo.setStatus(BusStatus.LocalBus);
            }else if (!localFlag.get() && remoteFlag.get()){
                objectStatusVo.setStatus(BusStatus.FeignBus);
            }
        }
        return objectStatusVo;
    }

    @Override
    public void targetSwitchLocalByRefCN(String refClassName)  {
        List<Field> usableFields = getUsableFields();
        for (Field usableField : usableFields) {
            try {
                if (getSinkFieldsByClassName(usableField.get(this),refClassName)){
                    try {
                        this.targetSwitchLocal(usableField.getName());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void targetSwitchRemoteByRefCN(String refClassName){
        List<Field> usableFields = getUsableFields();
        for (Field usableField : usableFields) {
            if (getSinkFieldsByClassName(usableField.getType(),refClassName)){
                try {
                    this.targetSwitchRemote(usableField.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 根搜索目标className
     * @param object 根对象
     * @param refClassName 目标class
     * @return
     */
    private boolean getSinkFieldsByClassName(Object object,String refClassName) {
        if (object.getClass().getTypeName().equals(refClassName)){
            return true;
        }
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object sinkObj = null;
            try {
                sinkObj = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(sinkObj == null && sinkObj.getClass() == String.class) continue;
            if (getSinkFieldsByClassName(sinkObj,refClassName)){
                return true;
            }
        }
        return false;
    }


    /**
     * 查询当前对象受控制范围的域
     * @return
     * @throws IllegalAccessException
     */
    private List<Field> getUsableFields() {
        if (usableFields != null) return usableFields;
        List<Field> fields = new ArrayList<>();

        Class<? extends DynamicSwitchable> aClass = this.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.get(this).getClass().getAnnotation(LocalBusGroup.class) != null
                        || declaredField.get(this).getClass().getAnnotation(RemoteBusGroup.class) != null
                        || getFieldLabel(declaredField) != null){
                    fields.add(declaredField);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        usableFields = fields;
        return usableFields;
    }




}
