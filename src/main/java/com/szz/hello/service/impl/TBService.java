package com.szz.hello.service.impl;

import com.szz.hello.common.LocalBusGroup;
import com.szz.hello.service.TestService;

/**
 * @author szz
 */
@LocalBusGroup(switchClass = TAService.class)
public class TBService implements TestService {

    @Override
    public String bus() {
        return "测试业务b";
    }
}
