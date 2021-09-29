package com.example.myapplication

import android.content.Context
import com.example.myapplication.startup.Startup

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
class SDK5 : Startup {

    override fun create(context: Context) {
        //模拟初始化时间
        Thread.sleep(80)
    }

    //主线程
    override fun callCreateOnMainThread(): Boolean = false
}