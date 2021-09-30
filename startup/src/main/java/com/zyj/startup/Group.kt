package com.zyj.startup


/**
 * @author : zhaoyanjun
 * @time : 2021/9/29
 * @desc : 任务组
 */
class Group(private val id: Int) {

    /**
     * 添加一个任务
     */
    fun add(startup: Startup) {
        startup.groupId = id
        StartupManager.startupIdMap[startup.aliasName] = id
        StartupManager.allStartup[startup.aliasName] = startup
        if (!startup.callCreateOnMainThread()) {
            //在子线程中运行
            StartupManager.ioResult.add(startup)
            if (startup.waitOnMainThread()) {
                StartupManager.waitOnMainThreadResult.add(startup)
            }
        } else {
            //在主线程运行
            StartupManager.mainResult.add(startup)
        }
    }
}