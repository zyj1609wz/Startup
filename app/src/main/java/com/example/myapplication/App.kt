package com.example.myapplication

import android.app.Application
import android.util.Log
import com.example.myapplication.sdk.SDK1
import com.example.myapplication.sdk.SDK2
import com.example.myapplication.sdk.SDK3
import com.example.myapplication.sdk.SDK4
import com.zyj.startup.StartupManager
import com.zyj.startup.TimeListener

/**
 * @author : zhaoyanjun
 * @time : 2021/9/27
 * @desc :
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        com.zyj.startup.StartupManager
            .addGroup {
                it.add(SDK1())
                it.add(SDK2())
            }
            .addGroup {
                it.add(SDK3())
                it.add(SDK4())
                it.add(SDK5())
            }
            .cost(object : com.zyj.startup.TimeListener {

                override fun itemCost(name: String, time: Long) {
                    Log.d("startup-", "itemCost:$name time:$time")
                }

                override fun groupCost(groupIndex: Int, time: Long) {
                    Log.d("startup-", "groupCost:$groupIndex time:$time")
                }

                override fun allCost(time: Long) {
                    Log.d("startup-", "allCost:$time")
                }
            })
            .start(this)
    }
}