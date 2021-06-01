package com.mz.jarboot.core.basic;

import com.mz.jarboot.core.constant.CoreConstant;
import com.mz.jarboot.core.ws.MessageHandler;
import com.mz.jarboot.core.ws.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton core factory for create socket client, thread pool, strategy instance.
 * @author jianzhengma
 */
public class SingletonCoreFactory {
    private static final Logger logger = LoggerFactory.getLogger(CoreConstant.LOG_NAME);
    private static volatile SingletonCoreFactory instance = null; //NOSONAR
    private WebSocketClient client = null;
    public static SingletonCoreFactory getInstance() {
        if (null == instance) {
            synchronized (SingletonCoreFactory.class) {
                if (null == instance) {
                    instance = new SingletonCoreFactory();
                }
            }
        }
        return instance;
    }
    public synchronized WebSocketClient createSingletonClient(MessageHandler handler) {
        if (null != client) {
            return client;
        }
        String url = String.format("ws://%s/jarboot-agent/ws/%s",
                EnvironmentContext.getHost(), EnvironmentContext.getServer());
        logger.debug("initClient {}", url);
        client = new WebSocketClient(url);
        boolean isOk = client.connect(handler);
        if (!isOk) {
            logger.warn("连接jarboot-server服务失败");
            client.disconnect();
            client = null;
            return null;
        }
        logger.info("createSingletonClient>>>>");
        return client;
    }

    public WebSocketClient getSingletonClient() {
        return this.client;
    }
}