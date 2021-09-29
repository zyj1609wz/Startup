# Startup
android 启动库

使用方法 
```
StartupManager
            .addGroup {
                it.add(SDK1())
                it.add(SDK2())
            }
            .addGroup {
                it.add(SDK3())
                it.add(SDK4())
                it.add(SDK5())
            }
            .cost(object : TimeListener {

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
```
