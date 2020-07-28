package com.szz.hello.common;

/**
 * @author szz
 */
public enum  BusFlag {

    /**
     * Feign对接 状态
     */
    FeignBusGroup,

    /**
     * 本地原逻辑 状态
     */
    LocalBusGroup,

    /**
     * 混合 状态
     */
    MixedBusGroup;
}
