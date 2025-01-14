package com.mz.jarboot.core.cmd.model;

import java.lang.management.ThreadInfo;
import java.util.List;
import java.util.Map;

/**
 * Model of 'thread' command
 *
 * @author majianzheng
 * 以下代码基于开源项目Arthas适配修改
 */
public class ThreadModel extends ResultModel {

    //single thread: thread 12
    private ThreadInfo threadInfo;

    //thread -b
    private BlockingLockInfo blockingLockInfo;

    //thread -n 5
    private List<BusyThreadInfo> busyThreads;

    //thread stats
    private List<com.mz.jarboot.core.cmd.model.ThreadVO> threadStats;
    private Map<Thread.State, Integer> threadStateCount;
    private boolean all;

    public ThreadModel() {
    }

    public ThreadModel(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public ThreadModel(BlockingLockInfo blockingLockInfo) {
        this.blockingLockInfo = blockingLockInfo;
    }

    public ThreadModel(List<BusyThreadInfo> busyThreads) {
        this.busyThreads = busyThreads;
    }

    public ThreadModel(List<com.mz.jarboot.core.cmd.model.ThreadVO> threadStats, Map<Thread.State, Integer> threadStateCount, boolean all) {
        this.threadStats = threadStats;
        this.threadStateCount = threadStateCount;
        this.all = all;
    }

    @Override
    public String getName() {
        return "thread";
    }

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public BlockingLockInfo getBlockingLockInfo() {
        return blockingLockInfo;
    }

    public void setBlockingLockInfo(BlockingLockInfo blockingLockInfo) {
        this.blockingLockInfo = blockingLockInfo;
    }

    public List<BusyThreadInfo> getBusyThreads() {
        return busyThreads;
    }

    public void setBusyThreads(List<BusyThreadInfo> busyThreads) {
        this.busyThreads = busyThreads;
    }

    public List<com.mz.jarboot.core.cmd.model.ThreadVO> getThreadStats() {
        return threadStats;
    }

    public void setThreadStats(List<com.mz.jarboot.core.cmd.model.ThreadVO> threadStats) {
        this.threadStats = threadStats;
    }

    public Map<Thread.State, Integer> getThreadStateCount() {
        return threadStateCount;
    }

    public void setThreadStateCount(Map<Thread.State, Integer> threadStateCount) {
        this.threadStateCount = threadStateCount;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
