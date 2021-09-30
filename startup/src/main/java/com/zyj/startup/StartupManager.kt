package com.zyj.startup

import android.content.Context
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc : 启动工具类
 */
object StartupManager {

    private var timeListener: TimeListener? = null
    internal val startupIdMap = mutableMapOf<String, Int>()  //存所有任务groupId
    internal val allStartup = mutableMapOf<String, Startup>() //  //存所有任务groupId
    private val executor = Executors.newCachedThreadPool()
    internal val mainResult = mutableListOf<Startup>() //主线程列表
    internal val ioResult = mutableListOf<Startup>()   //子线程列表
    internal val waitOnMainThreadResult = mutableListOf<Startup>()   //所有等主线程的任务
    private val groupCount = AtomicInteger()  //组id

    private val startupCount = AtomicInteger()  //运行在子线程的任务个数
    private val countDownLatch by lazy { CountDownLatch(startupCount.get()) }

    fun addGroup(block: (Group) -> Unit): StartupManager {
        val gr = Group(groupCount.getAndIncrement())
        block(gr)
        return this
    }

    fun cost(time: TimeListener): StartupManager {
        timeListener = time
        return this
    }

    /**
     * 开始执行
     */
    fun start(context: Context) {
        val start = System.currentTimeMillis()
        //子线程
        ioResult.forEach {
            executor.execute {
                checkDependenciesLegal(it)
                it.dependencies()?.forEach { item ->
                    allStartup[item.simpleName]?.await()
                }
                val costTime = measureTimeMillis {
                    it.execute(context)
                    countDownLatch.countDown()
                }
                timeListener?.itemCost(it.getAliasName(), costTime)
            }
        }

        //主线程
        mainResult.forEach {
            checkDependenciesLegal(it)
            it.dependencies()?.forEach { item ->
                allStartup[item.simpleName]?.await()
            }

            val costTime = measureTimeMillis {
                it.execute(context)
                countDownLatch.countDown()
            }
            timeListener?.itemCost(it.getAliasName(), costTime)
        }

        //等待所有等待主线程的任务执行完
        waitOnMainThreadResult.forEach {
            runCatching {
                it.await()
            }
        }
        timeListener?.allCost(System.currentTimeMillis() - start)
        clear()
    }

    /**
     * 检查依赖合法性
     * groupId > 所有依赖的最大id
     */
    private fun checkDependenciesLegal(startup: Startup) {
        val dependencies = startup.dependencies()
        if (dependencies.isNullOrEmpty()) {
            if (startup.groupId == 0) {
                return
            }
            throw Exception("${startup.getAliasName()} dependencies is illegal,Should be placed in the first group")
        }
        var dependenciesMaxGroupId = 0
        dependencies.forEach { item ->
            //取最大值
            dependenciesMaxGroupId =
                dependenciesMaxGroupId.coerceAtLeast(
                    startupIdMap[item.simpleName] ?: 0
                )
        }
        if (startup.groupId - dependenciesMaxGroupId != 1) {
            throw Exception("${startup.getAliasName()} dependencies is illegal,Should be placed in the ${dependenciesMaxGroupId + 2}st group")
        }
    }

    /**
     * 释放资源
     */
    private fun clear() {
        executor.execute {
            runCatching {
                countDownLatch.await()
                startupIdMap.clear()
                allStartup.clear()
                mainResult.clear()
                ioResult.clear()
                waitOnMainThreadResult.clear()
            }
        }
    }
}

