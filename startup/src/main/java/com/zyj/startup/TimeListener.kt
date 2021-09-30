package com.zyj.startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/29
 * @desc : 耗时监听
 */
interface TimeListener {
    fun itemCost(name: String, time: Long, threadName: String)
    fun allCost(time: Long)
}