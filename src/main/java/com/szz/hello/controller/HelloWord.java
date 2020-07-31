package com.szz.hello.controller;

import com.szz.hello.client.TestClient;
import com.szz.hello.common.DynamicSwitchable;
import com.szz.hello.service.MyService;
import com.szz.hello.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szz
 */
@RestController
public class HelloWord extends DynamicSwitchable {

    @Autowired
    private MyService service;


    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok().body(service.getMessage());
    }

}
