package com.zyj.startup

import android.content.Context

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc : 任务
 */
abstract class Startup {

    //开始初始化
    abstract fun create(context: Context)

    //是不是在主线程初始化
    abstract fun callCreateOnMainThread(): Boolean

    abstract fun dependencies(): List<Class<out Startup>>?

    //组id
    internal var groupId: Int = 0

    //别名
    internal fun getAliasName(): String = javaClass.simpleName
}