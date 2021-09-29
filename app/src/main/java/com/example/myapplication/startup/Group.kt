package com.example.myapplication.startup

import android.content.Context
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * @author : zhaoyanjun
 * @time : 2021/9/29
 * @desc :
 */
class Group {

    private val mainResult = mutableListOf<Startup>() //主线程列表
    private val ioResult = mutableListOf<Startup>()   //子线程列表
    private val workTaskCount = AtomicInteger()  //运行在子线程的任务个数
    private val countDownLatch by lazy { CountDownLatch(workTaskCount.get()) }
    private val executor = Executors.newCachedThreadPool()

    /**
     * 添加一个任务
     */
    fun add(startup: Startup) {
        if (!startup.callCreateOnMainThread()) {
            //在子线程中运行
            ioResult.add(startup)
            workTaskCount.incrementAndGet()
        } else {
            //在主线程运行
            mainResult.add(startup)
        }
    }

    fun execute(context: Context, timeListener: TimeListener?) {
        //子线程
        ioResult.forEach {
            executor.execute {
                val costTime = measureTimeMillis {
                    it.create(context)
                }
                timeListener?.itemCost(it.javaClass.simpleName, costTime)
                countDownLatch.countDown()
            }
        }

        //主线程
        mainResult.forEach {
            val costTime = measureTimeMillis {
                it.create(context)
            }
            timeListener?.itemCost(it.javaClass.simpleName, costTime)
        }

        //阻塞线程
        runCatching {
            countDownLatch.await()
        }
    }
}