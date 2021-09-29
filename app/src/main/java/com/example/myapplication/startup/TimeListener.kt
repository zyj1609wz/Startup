package com.example.myapplication.startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/29
 * @desc : 耗时监听
 */
interface TimeListener {
    fun itemCost(name: String, time: Long)
    fun groupCost(groupIndex: Int, time: Long)
    fun allCost(time: Long)
}