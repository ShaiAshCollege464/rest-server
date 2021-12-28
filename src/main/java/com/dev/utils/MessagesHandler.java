package com.dev.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MessagesHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();
    private static int totalSessions;

//    @PostConstruct
//    public void init () {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    sendNewNotification();
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
//        Map<String, String> map = Utils.splitQuery(session.getUri().getQuery());
        sessionList.add(session);
        totalSessions = sessionList.size();
        System.out.println("afterConnectionEstablished");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("handleTextMessage");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        System.out.println("afterConnectionClosed");
    }

//    public void sendNewNotification () {
//        for (WebSocketSession session : sessionList) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("test", "test");
//            try {
//                session.sendMessage(new TextMessage(jsonObject.toString()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



}