package com.szz.hello.controller;

import com.szz.hello.common.SwitchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author szz
 */
@RestController
public class MController {

    @Autowired
    private SwitchController swtichController;

    @GetMapping("/switch")
    public ResponseEntity swtichLocal(boolean flag) {
        if (flag){
            swtichController.switchLocalALLBean();
        }else {
            swtichController.switchRemoteALLBean();
        }
        return ResponseEntity.ok().body(true);
    }
}