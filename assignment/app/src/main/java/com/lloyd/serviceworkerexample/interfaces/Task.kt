package com.lloyd.serviceworkerexample.interfaces

interface Task<T> {
    fun onExecuteTask(): T

    fun onTaskCompleted(result: T)
}