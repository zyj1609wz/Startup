package com.example.myapplication.sdk

import android.content.Context
import com.zyj.startup.Startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
class SDK2 : Startup() {

    override fun create(context: Context) {
        //模拟初始化时间
        Thread.sleep(50)
    }

    //主线程
    override fun callCreateOnMainThread(): Boolean = true

    override fun dependencies(): List<Class<out Startup>>? {
        return null
    }
}