package com.zyj.startup

import android.content.Context
import java.util.concurrent.CountDownLatch

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

    open fun dependencies(): List<Class<out Startup>>? = null

    abstract fun waitOnMainThread(): Boolean

    private val countDownLatch by lazy { CountDownLatch(1) }

    internal fun execute(context: Context) {
        create(context)
        countDownLatch.countDown()
    }

    internal fun await() {
        runCatching {
            countDownLatch.await()
        }.onFailure {
            it.printStackTrace()
        }
    }

    //组id
    internal var groupId: Int = 0

    //别名
    internal val aliasName by lazy {
        javaClass.simpleName
    }
}