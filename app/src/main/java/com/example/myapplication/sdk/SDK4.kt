package com.example.myapplication.sdk

import android.content.Context
import com.zyj.startup.Startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
class SDK4 : Startup {

    override fun create(context: Context) {
        //模拟初始化时间
        Thread.sleep(100)
    }

    //主线程
    override fun callCreateOnMainThread(): Boolean = false
}