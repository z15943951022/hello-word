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
    public ResponseEntity switchLocal(boolean flag) {
        if (flag){
            swtichController.switchLocalALLBean();
        }else {
            swtichController.switchRemoteALLBean();
        }
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/label")
    public ResponseEntity switchRemote(boolean flag,String label) {
        if (flag){
            swtichController.switchLocalALLBeanByLabel(label);
        }else {
            swtichController.switchRemoteALLBeanByLabel(label);
        }
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/print")
    public ResponseEntity switchRemote() {
        return ResponseEntity.ok().body(swtichController.printAllStatus());
    }

    @GetMapping("/a")
    public ResponseEntity a(String className,String FieldName) {
        swtichController.targetSwitch(className,FieldName,true);
        return ResponseEntity.ok().body(true);
    }

}
