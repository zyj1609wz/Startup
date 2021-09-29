package com.zyj.startup

import android.content.Context
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc : 启动工具类
 */
object StartupManager {

    private val groupResult = mutableListOf<Group>() //分组
    private var timeListener: TimeListener? = null
    internal val startupIdMap = mutableMapOf<String, Int>()  //存所有任务groupId
    internal val executor = Executors.newCachedThreadPool()

    fun addGroup(block: (Group) -> Unit): StartupManager {
        val gr = Group(groupResult.size)
        block(gr)
        groupResult.add(gr)
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
        val allTime = measureTimeMillis {
            groupResult.forEachIndexed { index, group ->
                val groupTime = measureTimeMillis {
                    group.execute(context, timeListener)
                }
                timeListener?.groupCost(index, groupTime)
            }
        }
        timeListener?.allCost(allTime)
        clear()
    }

    /**
     * 释放资源
     */
    private fun clear() {
        groupResult.clear()
        startupIdMap.clear()
    }
}

