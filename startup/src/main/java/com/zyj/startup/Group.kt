package com.zyj.startup

import android.content.Context
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * @author : zhaoyanjun
 * @time : 2021/9/29
 * @desc : 任务组
 */
class Group(private val id: Int) {

    private val mainResult = mutableListOf<Startup>() //主线程列表
    private val ioResult = mutableListOf<Startup>()   //子线程列表
    private val workTaskCount = AtomicInteger()  //运行在子线程的任务个数
    private val countDownLatch by lazy { CountDownLatch(workTaskCount.get()) }


    /**
     * 添加一个任务
     */
    fun add(startup: Startup) {
        startup.groupId = id
        StartupManager.startupIdMap[startup.getAliasName()] = id
        if (!startup.callCreateOnMainThread()) {
            //在子线程中运行
            ioResult.add(startup)
            workTaskCount.incrementAndGet()
        } else {
            //在主线程运行
            mainResult.add(startup)
        }
    }

    /**
     * 执行任务
     */
    internal fun execute(context: Context, timeListener: TimeListener?) {
        //子线程
        ioResult.forEach {
            StartupManager.executor.execute {
                checkDependenciesLegal(it)
                val costTime = measureTimeMillis {
                    it.create(context)
                }
                timeListener?.itemCost(it.getAliasName(), costTime)
                countDownLatch.countDown()
            }
        }

        //主线程
        mainResult.forEach {
            checkDependenciesLegal(it)
            val costTime = measureTimeMillis {
                it.create(context)
            }
            timeListener?.itemCost(it.getAliasName(), costTime)
        }

        //阻塞线程
        runCatching {
            countDownLatch.await()
        }
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
            throw Exception("${startup.getAliasName()} dependencies is illegal, groupId should is 0")
        }
        var dependenciesMaxGroupId = 0
        dependencies.forEach { item ->
            //取最大值
            dependenciesMaxGroupId =
                dependenciesMaxGroupId.coerceAtLeast(
                    StartupManager.startupIdMap[item.simpleName] ?: 0
                )
        }
        if (startup.groupId <= dependenciesMaxGroupId) {
            throw Exception("${startup.getAliasName()} dependencies is illegal")
        }
    }
}