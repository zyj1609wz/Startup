package com.example.myapplication.sdk

import android.content.Context
import com.zyj.startup.Startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
class SDK1 : Startup {

    override fun create(context: Context) {
        //模拟初始化时间
        Thread.sleep(100)
    }

    //可以在子线程初始化
    override fun callCreateOnMainThread(): Boolean = false
}