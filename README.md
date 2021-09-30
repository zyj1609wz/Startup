# Startup
android 启动库

# 添加依赖库

Add it in your root build.gradle at the end of repositories:

```aidl
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
}
```

Add the dependency

```aidl
implementation 'com.github.zyj1609wz:Startup:2.3.0'
```

## 使用方法

```aidl
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
