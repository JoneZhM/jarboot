package com.mz.jarboot.ws;

import com.mz.jarboot.common.CommandConst;
import com.mz.jarboot.constant.CommonConst;
import com.mz.jarboot.event.NoticeEnum;
import com.mz.jarboot.event.WsEventEnum;
import com.mz.jarboot.task.TaskStatus;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketManager {
    private static volatile WebSocketManager instance = null;// NOSONAR
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private WebSocketManager(){}

    public static WebSocketManager getInstance() {
        if (null == instance) {
            synchronized (WebSocketManager.class) {
                if (null == instance) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }

    public void addNewConnect(Session session) {
        sessionMap.put(session.getId(), session);
    }

    public void delConnect(String id) {
        sessionMap.remove(id);
    }

    public void sendConsole(String server, String text) {
        String msg = formatMsg(server, WsEventEnum.CONSOLE_LINE, text);
        this.sessionMap.forEach((k, session) -> sendTextMessage(session, msg));
    }

    public void sendConsole(String server, String text, String sessionId) {
        if (CommandConst.SESSION_COMMON.equals(sessionId)) {
            //广播session的id
            sendConsole(server, text);
            return;
        }
        Session session = this.sessionMap.getOrDefault(sessionId, null);
        if (null != session) {
            String msg = formatMsg(server, WsEventEnum.CONSOLE_LINE, text);
            sendTextMessage(session, msg);
        }
    }

    public void renderJson(String server, String text) {
        String msg = formatMsg(server, WsEventEnum.RENDER_JSON, text);
        this.sessionMap.forEach((k, session) -> sendTextMessage(session, msg));
    }

    public void renderJson(String server, String text, String sessionId) {
        if (CommandConst.SESSION_COMMON.equals(sessionId)) {
            //广播session的id
            renderJson(server, text);
            return;
        }
        Session session = this.sessionMap.getOrDefault(sessionId, null);
        if (null != session) {
            String msg = formatMsg(server, WsEventEnum.RENDER_JSON, text);
            sendTextMessage(session, msg);
        }
    }

    public void publishStatus(String server, TaskStatus status) {
        //发布状态变化
        String msg = formatMsg(server, WsEventEnum.SERVER_STATUS, status.name());
        this.sessionMap.forEach((k, session) -> sendTextMessage(session, msg));
    }

    public void commandEnd(String server, String body) {
        String msg = formatMsg(server, WsEventEnum.CMD_END, body);
        this.sessionMap.forEach((k, session) -> sendTextMessage(session, msg));
    }

    public void commandEnd(String server, String body, String sessionId) {
        if (CommandConst.SESSION_COMMON.equals(sessionId)) {
            //广播session的id
            commandEnd(server, body);
            return;
        }
        Session session = this.sessionMap.getOrDefault(sessionId, null);
        if (null != session) {
            String msg = formatMsg(server, WsEventEnum.CMD_END, body);
            sendTextMessage(session, msg);
        }
    }

    public void notice(String text, NoticeEnum level) {
        WsEventEnum type = null;
        switch (level) {
            case INFO:
                type = WsEventEnum.NOTICE_INFO;
                break;
            case WARN:
                type = WsEventEnum.NOTICE_WARN;
                break;
            case ERROR:
                type = WsEventEnum.NOTICE_ERROR;
                break;
            default:
                return;
        }
        String msg = formatMsg("", type, text);
        if (!sessionMap.isEmpty()) {
            this.sessionMap.forEach((k, session) -> sendTextMessage(session, msg));
        }
    }

    private static String formatMsg(String server, WsEventEnum event, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append(server).append(CommonConst.PROTOCOL_SPLIT)
                .append(event.ordinal()).append(CommonConst.PROTOCOL_SPLIT).append(body);
        return sb.toString();
    }
    private static void sendTextMessage(final Session session, String msg) {
        if (!session.isOpen()) {
            return;
        }
        synchronized (session) {// NOSONAR
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                //ignore
            }
        }
    }
}
