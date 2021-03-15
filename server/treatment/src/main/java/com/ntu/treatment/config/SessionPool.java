package com.ntu.treatment.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ntu.treatment.service.Impl.UserServiceImpl;
import com.ntu.treatment.utils.SpringUtil;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/11 15:57
 */
public class SessionPool {

    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void close(String sessionId) throws IOException {
        for (String userId : SessionPool.sessions.keySet()) {
            Session session = SessionPool.sessions.get(userId);
            if (session.getId().equals(sessionId)){
                sessions.remove(userId);
                break;
            }
        }

    }

    public static void sendMessage(String sessionId, String message) {
        sessions.get(sessionId).getAsyncRemote().sendText(message);
    }

    public static void sendMessage(String message) {
        JSONObject jsonObject = JSON.parseObject(message);

        String msg = jsonObject.getString("content");
        String checked_kinds = jsonObject.getString("checked_kinds");
        String username = jsonObject.getString("username");
        if (checked_kinds.equals("医生")){
            UserServiceImpl userService = (UserServiceImpl) SpringUtil.getBean(UserServiceImpl.class);
            String toUserIds = userService.findChatPatient(username);

            Session session = sessions.get(toUserIds);
            if (session != null){
                session.getAsyncRemote().sendText(msg);
            }
        }else {
            String toUserId = jsonObject.getString("toUserName");
            Session session = sessions.get(toUserId);
            if (session != null){
                session.getAsyncRemote().sendText(msg);
            }
        }
    }
}
