package com.demo.barry.demoPage.demoActivity

import android.util.Log
import android.view.View
import com.demo.barry.demoPage.R
import com.demo.barry.base_utils.utils.Paths
import com.demo.barry.base_utils.base.BaseActivity
import com.demo.barry.base_utils.base.BasePresenter
import kotlinx.android.synthetic.main.activity_threadpool.*
import pers.victor.smartgo.Path
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

@Path(Paths.DemoPage.ThreadPoolActivity)
class ThreadPoolActivity : BaseActivity<BasePresenter>() {

    private var corePoolSize = 3 //核心线程数
    //    private var maximumPoolSize = 10 //尚未运行的（队列中）最大线程数
//    private var keepAliveTime = 5 //非核心空闲线程的等待的最长时间
//    private var unit = TimeUnit.SECONDS
    //用于存储已提交的任务但 未执行的任务即等候的任务
//    private val workQueue = ArrayBlockingQueue<Runnable>(maximumPoolSize)
    private var executorService: ExecutorService? = null
    private var fixedExecutorService: ExecutorService? = null
    private var schuleExecutorService: ScheduledExecutorService? = null
    private var singleExecutorService: ExecutorService? = null

    override fun initWidgets() {
        //创建线程池
        //缓存型:n个线程并发执行(创建n个线程)
        executorService = Executors.newCachedThreadPool(threadFactory)
        //固定型的线程池:corePoolSize个线程并发执行(创建corePoolSize个线程)执行完之后,在执行下面的corePoolSize个线程
        fixedExecutorService = Executors.newFixedThreadPool(corePoolSize, threadFactory)
        //调度型线程池:与固定型不同的就是可以设置延时执行 (创建了corePoolSize个线程)
        schuleExecutorService = Executors.newScheduledThreadPool(corePoolSize, threadFactory)
        //单线程池:只创建了一个线程
        singleExecutorService = Executors.newSingleThreadExecutor(threadFactory)

    }

    override fun setListeners() {
        click(btn_cache, btn_double, btn_fixed, btn_reset, btn_sche, btn_single)
    }

    override fun onWidgetsClick(v: View) {
        when (v) {
            btn_cache -> {
                executorService?.execute(runnable1)
                executorService?.execute(runnable2)
                executorService?.execute(runnable3)
                executorService?.execute(runnable4)
                executorService?.execute(runnable5)
                executorService?.execute(runnable6)
                executorService?.execute(runnable7)
                executorService?.execute(runnable8)
                executorService?.execute(runnable9)
            }
            btn_fixed -> {
                fixedExecutorService?.execute(runnable1)
                fixedExecutorService?.execute(runnable2)
                fixedExecutorService?.execute(runnable3)
                fixedExecutorService?.execute(runnable4)
                fixedExecutorService?.execute(runnable5)
                fixedExecutorService?.execute(runnable6)
                fixedExecutorService?.execute(runnable7)
                fixedExecutorService?.execute(runnable8)
                fixedExecutorService?.execute(runnable9)
                fixedExecutorService?.execute(runnable10)
            }
            btn_sche -> {
                schuleExecutorService?.execute(runnable1)
                schuleExecutorService?.execute(runnable2)
                schuleExecutorService?.execute(runnable3)
                schuleExecutorService?.execute(runnable4)
                schuleExecutorService?.execute(runnable5)
                schuleExecutorService?.schedule(runnable6, 10L, TimeUnit.SECONDS)
                schuleExecutorService?.schedule(runnable7, 20L, TimeUnit.SECONDS)
                schuleExecutorService?.schedule(runnable8, 30L, TimeUnit.SECONDS)
                schuleExecutorService?.schedule(runnable9, 40L, TimeUnit.SECONDS)
                schuleExecutorService?.schedule(runnable10, 50L, TimeUnit.SECONDS)
            }
            btn_single -> {
                singleExecutorService?.execute(runnable1)
                singleExecutorService?.execute(runnable2)
                singleExecutorService?.execute(runnable3)
                singleExecutorService?.execute(runnable4)
                singleExecutorService?.execute(runnable5)
            }
            btn_reset -> {
                pb1.progress = 0
                pb2.progress = 0
                pb3.progress = 0
                pb4.progress = 0
                pb5.progress = 0
                pb6.progress = 0
                pb7.progress = 0
                pb8.progress = 0
                pb9.progress = 0
                pb10.progress = 0
            }
            btn_double -> {
                singleExecutorService?.execute(runnable6)
                singleExecutorService?.execute(runnable7)
                singleExecutorService?.execute(runnable8)
                singleExecutorService?.execute(runnable9)
                singleExecutorService?.execute(runnable10)
            }
        }

    }

    override fun bindLayout() = R.layout.activity_threadpool

    /**
     * 创建线程的工厂
     */
    private val threadFactory = object : ThreadFactory {
        var integer = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            return Thread(r, "thread:" + integer.getAndIncrement())
        }
    }

    private val runnable1 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb1.progress < pb1.max) {
                Thread.sleep(100)
                pb1.progress = pb1.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable2 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb2.progress < pb2.max) {
                Thread.sleep(100)
                pb2.progress = pb2.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable3 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb3.progress < pb3.max) {
                Thread.sleep(100)
                pb3.progress = pb3.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable4 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb4.progress < pb4.max) {
                Thread.sleep(100)
                pb4.progress = pb4.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable5 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb5.progress < pb5.max) {
                Thread.sleep(100)
                pb5.progress = pb5.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable6 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb6.progress < pb6.max) {
                Thread.sleep(100)
                pb6.progress = pb6.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable7 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb7.progress < pb7.max) {
                Thread.sleep(100)
                pb7.progress = pb7.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable8 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb8.progress < pb8.max) {
                Thread.sleep(100)
                pb8.progress = pb8.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable9 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb9.progress < pb9.max) {
                Thread.sleep(100)
                pb9.progress = pb9.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private val runnable10 = Runnable {
        try {
            Log.e("TAG", "pb1:" + Thread.currentThread().name)
            while (pb10.progress < pb10.max) {
                Thread.sleep(100)
                pb10.progress = pb10.progress + 5
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


}