package com.sergeytalyzin.writer.presenters

import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.helpers.VKUserRequest
import com.sergeytalyzin.writer.models.User
import com.sergeytalyzin.writer.providers.LoginProvider
import com.sergeytalyzin.writer.views.LoginView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

@InjectViewState
class LoginPresenter: MvpPresenter<LoginView>() {

    private lateinit var user: User

    fun authorization(requestCode: Int, resultCode: Int, data: Intent?) : Boolean {

        viewState.deleteError()

        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewState.showStartLoading()
                getDataAboutUser()
            }

            override fun onLoginFailed(errorCode: Int) {
                viewState.showError(strId = R.string.error_authorization, errorCode = errorCode)
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            return true
        }

        return false
    }

    fun startHostActivity() {

        viewState.startHostActivity()
    }

    private fun getDataAboutUser() {

        VK.execute(VKUserRequest(), object : VKApiCallback<Map<String, Any>> {
            override fun success(result: Map<String, Any>) {
                // Пользователь авторизован и получен объект DataAboutUser со всеми данными пользователя
                user = result["user_object"] as User

                // Проверка есть ли пользователь в FB
                checkUserInFB(result["userId"].toString())
            }

            override fun fail(error: VKApiExecutionException) {
                viewState.showEndLoading()
                viewState.showError(strId = R.string.error_getDataFromVK, errorCode = error.code)
            }
        })
    }

    fun checkUserInFB(userId: String) {
        LoginProvider().checkUserInFB(userId = userId, user = user, loginPresenter = this@LoginPresenter)
    }

    fun errorCheckDataInFB() {
        viewState.showEndLoading()
        viewState.showError(strId = R.string.error_getDataFromFB, errorCode = 1)
    }
}