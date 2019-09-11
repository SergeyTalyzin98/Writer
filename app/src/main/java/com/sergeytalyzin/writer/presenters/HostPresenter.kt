package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.helpers.User
import com.sergeytalyzin.writer.helpers.VKIdRequest
import com.sergeytalyzin.writer.providers.HostProvider
import com.sergeytalyzin.writer.views.HostView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

@InjectViewState
class HostPresenter: MvpPresenter<HostView>() {

    fun getUser() {

        if (VK.isLoggedIn()) {

            if(User.getCreated()) {
                viewState.showData()
                return
            }

            VK.execute(VKIdRequest(), object : VKApiCallback<String> {
                override fun success(result: String) {

                    HostProvider().getUser(userId = result, data = { user ->

                        // Создания объекта User один раз и навсегда :)
                        User.create(id = result, name = "${user.first_name} ${user.last_name}",
                            city = user.city!!, photo_100 = user.photo_100!!, photo_200 = user.photo_200!!)

                        viewState.showData()

                    }, error = {})
                }

                override fun fail(error: VKApiExecutionException) {}
            })
        }
        else
            viewState.startLogin()
    }

    fun showBottomPanel() = viewState.showBottomPanel()

    fun closeBottomPanel() = viewState.closeBottomPanel()
}