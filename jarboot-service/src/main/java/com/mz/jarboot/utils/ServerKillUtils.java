package com.mz.jarboot.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 原命令行参数扩展，暂弃用
 */
@Deprecated
public class ServerKillUtils {
    public static ServerKillUtils getInstance() {
        return new ServerKillUtils();
    }

    /**
     * 杀死所有包括自己
     */
    public void killAllIncludeSelf() {
        //获取安装根目录
        //先扫描所有服务启动的jar包
        String servicesDirPath = SettingUtils.getServicesPath();
        List<String> jarNameList = this.getAllExecJar(servicesDirPath);
        //杀死所有的服务jar包
        this.killServerByJar(jarNameList);
    }

    private void killServerByJar(List<String> jarNameList) {
        if (CollectionUtils.isEmpty(jarNameList)) {
            return;
        }
        jarNameList.forEach(jar -> TaskUtils.killJavaByName(jar, null));
    }

    private List<String> getAllExecJar(String servicesDirPath) {
        List<String> jarList = new ArrayList<>();
        File servicesDir = new File(servicesDirPath);
        if (!servicesDir.isDirectory() || !servicesDir.exists()) {
            return jarList;
        }
        File[] serviceDirs = servicesDir.listFiles(
                (dir, name) -> (!StringUtils.equals("lib", name)) );
        if (null == serviceDirs || serviceDirs.length < 1) {
            return jarList;
        }
        for (File f : serviceDirs) {
            String jarName = getJarFileNameInDir(f);
            if (StringUtils.isNotEmpty(jarName)) {
                jarList.add(jarName);
            }
        }
        return jarList;
    }
    private String getJarFileNameInDir(File dir) {
        String jarName = "";
        if (!dir.exists() || !dir.isDirectory()) {
            return jarName;
        }
        String[] extensions = {"jar"};
        Collection<File> execJar =  FileUtils.listFiles(dir, extensions, false);
        if (CollectionUtils.isEmpty(execJar)) {
            return jarName;
        }
        File jarFile = execJar.iterator().next();
        if (null == jarFile || !jarFile.exists() || !jarFile.isFile()) {
            return jarName;
        }
        jarName = jarFile.getName();
        return jarName;
    }
    private ServerKillUtils() {}
}
