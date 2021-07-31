package com.mz.jarboot.api;

import com.mz.jarboot.common.JarbootException;

/**
 * Jarboot Factory
 * @author jianzhengma
 */
public class JarbootFactory {
    /**
     * 创建AgentService实例<br>
     * 前置条件：使用Jarboot启动的进程，否则抛出异常{@link JarbootException}，调用端代码要做好异常防护
     * @return {@link AgentService}
     */
    public static AgentService createAgentService() {
        try {
            Class<?> cls = Class.forName("com.mz.jarboot.agent.client.AgentServiceImpl");
            return (AgentService)cls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new JarbootException("Current application maybe not started by jarboot", e);
        }
    }

    private JarbootFactory() {
        throw new JarbootException("Can not constructor.");
    }
}