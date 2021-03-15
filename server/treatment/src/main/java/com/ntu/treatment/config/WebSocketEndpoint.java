package com.ntu.treatment.config;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/11 15:47
 */
//ws://localhost:8848/websocket/A
@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketEndpoint {

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     */

    @OnOpen
    public void onOpen(Session session , @PathParam("userId") String userId){
        // 分解获取的参数，把参数信息放入到session key中，以方便后续使用
        //把会话放入连接池
        this.session = session;
        SessionPool.sessions.put(userId, session);
    }

    /**
     *关闭连接
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        SessionPool.close(session.getId());
        session.close();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("客户端发来的消息"+message);
        SessionPool.sendMessage(message);
    }

}
