/*
package com.admin.ncs.kaicc.ws;


import com.alibaba.fastjson.JSONObject;


import com.admin.ncs.kaicc.dto.AddVaEngineWsDTO;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/AddVaEngine/{vaEngineId}")
@Component
@Slf4j
@EqualsAndHashCode
public class AddVaEngineWsServer {
    private static CopyOnWriteArraySet<AddVaEngineWsServer> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;
    private String vaEngineId;

    public static void sendVaEngineInstallStatus(String vaEngineId, String modelInstallStatus, List<String> messages) {
        System.out.println("send to " + vaEngineId + ", modelInstallStatus : " + modelInstallStatus + " " + messages);
        AddVaEngineWsDTO addVaEngineWsDTO = new AddVaEngineWsDTO(null,"200", messages,modelInstallStatus);
        String json = JSONObject.toJSONString(addVaEngineWsDTO);
        webSocketSet.stream()
                .filter(server ->
                        server.vaEngineId.equals(vaEngineId))
                .forEach(server -> server.sendMessage(json));
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("vaEngineId") String vaEngineId){
        System.out.println("AddVaEngineWsServer onOpen " + vaEngineId);
        this.session = session;
        this.vaEngineId = vaEngineId;
        webSocketSet.add(this);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        System.out.println("remove the task list window");
        // remove from webSocketSet
        webSocketSet.remove(this);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("websocket error:" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println("error :"+e);
        }
    }
}
*/
