package com.szz.hello.common;

import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author szz
 */
@Component
public class SwitchController {

    private static List<DynamicSwitch> dynamicSwitches = new ArrayList();

    public static void setDynamicSwitches(DynamicSwitch dynamicSwitch) {
        dynamicSwitches.add(dynamicSwitch);
    }

    public void switchRemoteALLBean(){
        for (DynamicSwitch dynamicSwitch : dynamicSwitches) {
            try {
                dynamicSwitch.switchoverRemoteBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchLocalALLBean(){
        for (DynamicSwitch dynamicSwitch : dynamicSwitches) {
            try {
                dynamicSwitch.switchoverLocalBean();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
