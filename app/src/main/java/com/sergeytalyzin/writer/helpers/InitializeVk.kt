package com.sergeytalyzin.writer.helpers

import android.app.Application
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class InitializeVk: Application() {

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // token expired
        }
    }

    override fun onCreate() {
        super.onCreate()
        tokenTracker.onTokenExpired()
        VK.initialize(applicationContext)
    }
}