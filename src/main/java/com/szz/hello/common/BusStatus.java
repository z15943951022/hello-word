package com.szz.hello.common;

/**
 * @author szz
 */
public enum BusStatus {

    /**
     * Feign对接 状态
     */
    FeignBus,

    /**
     * 本地原逻辑 状态
     */
    LocalBus,

    /**
     * 混合 状态
     */
    MixedBus,


    /**
     * 混合 状态
     */
    OtherBus;
}
