package com.szz.hello.service.impl;

import com.szz.hello.common.RemoteBusGroup;
import com.szz.hello.service.TestService;

/**
 * @author szz
 */
@RemoteBusGroup(switchClass = TBService.class)
public class TAService implements TestService {

    @Override
    public String bus() {
        return "测试业务A";
    }
}
