# android-chat
## Android实现即时聊天
### 前言：本篇文章将介绍如何实现安卓软件用户之间的即时聊天。
### 1、选用WebSocket
#### websocket的介绍
WebSocket是一种网络传输协议，可在单个TCP连接上进行全双工通信，位于OSI模型的应用层。WebSocket协议在2011年由IETF标准化为RFC 6455，后由RFC 7936补充规范。Web IDL中的WebSocket API由W3C标准化。
WebSocket使得客户端和服务器之间的数据交换变得更加简单，允许服务端主动向客户端推送数据。在WebSocket API中，浏览器和服务器只需要完成一次握手，两者之间就可以创建持久性的连接，并进行双向数据传输。
简单来说，WebSocket可以双向连接，服务器可以主动推送消息到客户端。当客户端与服务器建立连接，其他客户端也与服务器建立连接，那么此时服务器就可以为这两个客户端建立连接，这两个客户端就可以即时通信。
当然，这并不是真正的建立连接，两个客户端之间的通信是通过服务器这个桥梁实现的，客户端发送消息时，携带客户端的id以及想要建立通信的对方id，存入Session，当某个id的客户端发来消息，服务器主动推送到目标客户端，从而实现即时通信。
### 2 服务器端代码
#### 2.1 配置文件WebSocketConfig

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 吼吼权
 * @version 1.0
 * @date 2021/3/11 15:45
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
```


#### 2.2 与客户端建立连接

```java
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
```
#### 2.3 新建SessionPool发送消息到客户端

```java
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

    public static void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
```

这里如果要实现发送消息到某一个客户端，那么你要重写sendMessage（）这个函数，并且在代码中加入 Session session = sessions.get(toUserIds);  
toUserIds是你要发送的客户端的id。如下：

```java
public static void sendMessage(String message) {
        String toUserIds = *****;
        Session session = sessions.get(toUserIds);
        session.getAsyncRemote().sendText(message);
    }
```
即可发送消息至目标客户端。
### 3、客户端代码
安卓端连接服务器的url为：ws://10.8.113.79:8848/websocket/id
ws为websocket的协议。
安卓端代码就不完全展示了，后面将会把完整项目代码的GitHub地址放在底部。
#### 首先连接服务器

```java
/**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        URI uri = URI.create(Util.ws);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);

                Intent intent = new Intent();
                intent.setAction("com.xch.servicecallback.content");
                intent.putExtra("message", message);
                sendBroadcast(intent);

                checkLockAndShowNotification(message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
```
然后发送消息

```java
/**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(JSONObject msg) {
        if (null != client) {
            Log.e("JWebSocketClientService", "发送的消息：" + msg);
            client.send(msg.toString());
        }
    }
```
