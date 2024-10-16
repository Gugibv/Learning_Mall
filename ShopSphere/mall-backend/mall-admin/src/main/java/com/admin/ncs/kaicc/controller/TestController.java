package com.admin.ncs.kaicc.controller;



import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;

@RestController
public class TestController {


    /**
     * 使用下面命令创建json内容：
     * echo { > large.json
     * for /L %i in (1,1,10000) do echo "data%i": "This is a test string.", >> large.json
     * echo } >> large.json
     *
     * 使用下面命令发送请求测试
     * curl -X POST -H "Content-Type: application/json" -d @large.json http://localhost:8080/test
     * @return
     */
    @PostMapping(value = "/test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> test(@RequestBody Mono<String> body) {
        return body.map(data -> "Received data of length: " + data.length());
    }
}
