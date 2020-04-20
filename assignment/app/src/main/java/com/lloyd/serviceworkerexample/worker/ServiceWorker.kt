package com.lloyd.serviceworkerexample.worker

import com.lloyd.serviceworkerexample.interfaces.Task
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class ServiceWorker {
    private val taskQueue: BlockingQueue<Task<Any>> = ArrayBlockingQueue(50)
    private var disposable: Disposable? = null
    private var isExecuting: Boolean = false

    /*
    Add tasks to the queue.
     */
    fun <T> addTask(task: Task<T>) {
        taskQueue.add(task as Task<Any>)
        if (isExecuting.not()) {
            isExecuting = true
            execute()
        }
    }

    private fun execute() {
        if (taskQueue.isEmpty()) {
            isExecuting = false
            return
        }
        val task = taskQueue.remove()
        disposable = Single.just(task)
                .map {
                    it.onExecuteTask()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            task.onTaskCompleted(it)
                            execute()
                        },
                        {
                            execute()
                        })
    }

    fun cancelTask() {
        disposable?.dispose()
        taskQueue.clear()
    }
}