# Startup
android 启动库

# 添加依赖库

Add it in your root build.gradle at the end of repositories:

```java
allprojects {
     repositories {
	   ...
	   maven { url 'https://jitpack.io' }
     }
}
```

Add the dependency

```java
implementation 'com.github.zyj1609wz:Startup:2.4.0'
```

## 使用方法

首先创建 Startup 实例

```java
class SDK1 : Startup() {

    override fun create(context: Context) {
        //模拟初始化时间
        Thread.sleep(100)
    }

    //可以在子线程初始化
    override fun callCreateOnMainThread(): Boolean = false

    //依赖
    override fun dependencies(): List<Class<out Startup>>? {
        return null
    }

    //是否需要等待主线程
    override fun waitOnMainThread(): Boolean {
        return false
    }
}
```
然后添加任务管理

```java
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

                override fun itemCost(name: String, time: Long, threadName: String) {
                    Log.d("startup-", "itemCost:$name time:$time threadName:$threadName")
                }

                override fun allCost(time: Long) {
                    Log.d("startup-", "allCost:$time")
                }
            })
            .start(this)
```

## 相关博客 
[Java CountDownLatch的两种常用场景](https://blog.csdn.net/zhaoyanjun6/article/details/120506758)
