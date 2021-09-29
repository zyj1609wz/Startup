package com.example.myapplication.startup

import android.content.Context

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
interface Startup {

    //开始初始化
    fun create(context: Context)

    //是不是在主线程初始化
    fun callCreateOnMainThread(): Boolean
}