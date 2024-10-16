package com.admin.ncs.kaicc.controller;

import com.admin.ncs.kaicc.ws.AddVaEngineWsServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MobileController {

    /**
     * 测试url:
     * <a href="http://localhost:8080/api/video?status=installing">...</a>
     * <a href="http://localhost:8080/api/video?status=installed">...</a>
     */
    @GetMapping("/video")
    public ResponseEntity<Map<String, String>> getVideo(@RequestParam("status") String status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Status update sent to WebSocket");

        // 根据传入的状态改变前端显示的内容和颜色
        if ("installing".equalsIgnoreCase(status)) {
            // 传递安装进行中的状态，并显示红色
            AddVaEngineWsServer.sendVaEngineInstallStatus("1234", "installing", List.of("Installation in progress..."));
        } else if ("installed".equalsIgnoreCase(status)) {
            // 传递安装完成的状态，并显示绿色
            AddVaEngineWsServer.sendVaEngineInstallStatus("1234", "installed", List.of("Installation completed successfully"));
        } else {
            // 其他情况，默认状态
            AddVaEngineWsServer.sendVaEngineInstallStatus("1234", "unknown", List.of("Unknown status"));
        }

        return ResponseEntity.ok(response);
    }
}