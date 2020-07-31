package com.szz.hello.common;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author szz
 */
public interface SwitchBean {

    /**
     * 切换本地实现类
     * @throws IllegalAccessException
     */
    void switchoverLocalBean() throws IllegalAccessException;

    /**
     * 切换api实现类
     * @throws IllegalAccessException
     */
    void switchoverRemoteBean() throws IllegalAccessException;

    /**
     * 根据标签切换当前实现类到api
     * @param label
     * @throws IllegalAccessException
     */
    void switchoverRemoteBeanByLabel(String label) throws IllegalAccessException;

    /**
     * 根据标签切换当前实现类到本地逻辑
     *
     * @param label
     * @throws IllegalAccessException
     */
    void switchoverLocalBeanByLabel(String label) throws IllegalAccessException;

    /**
     * 指定域切换至local
     * @param fieldName 字段名称
     */
    void targetSwitchLocal(String fieldName) throws NoSuchFieldException;

    /**
     * 指定域切换至remote
     * @param fieldName 字段名称
     */
    void targetSwitchRemote(String fieldName) throws NoSuchFieldException;


    /**
     * 获取所有带标签的域
     * @return
     */
    List<Field> getInterfaceBusFields();

    /**
     * 获取当前对象信息
     * @return
     */
    ObjectStatusVo getInfo() throws IllegalAccessException;


    /**
     * 寻找引用这个类名得对象中得引用并切换指定至local
     * @param refClassName
     */
    void targetSwitchLocalByRefCN(String refClassName) throws IllegalAccessException;



    /**
     * 寻找引用这个类名得对象中得引用并切换指定至remote
     * @param refClassName
     */
    void targetSwitchRemoteByRefCN(String refClassName) throws IllegalAccessException;



}
