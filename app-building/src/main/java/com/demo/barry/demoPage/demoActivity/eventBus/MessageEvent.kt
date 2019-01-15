package com.demo.barry.demoPage.demoActivity.eventBus

/**
 * Created by Barry.Zhou on 2019/1/15
 */
class MessageEvent internal constructor(message: String) {

    private var message: String? = null
    init {
        this.message = message
    }
    internal fun getMessage(): String? {
        return message
    }
    fun setMessage(message: String) {
        this.message = message
    }
}