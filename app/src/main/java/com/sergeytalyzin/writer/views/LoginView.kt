package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView

interface LoginView: MvpView {
    fun showStartLoading() // Показывает загрузку
    fun showEndLoading() // остановка загрузки
    fun startHostActivity() // После успешной авторизации переход на активити с новостями
    fun showError(strId: Int, errorCode: Int) // Показывает ошибки
    fun deleteError() // очищение строки ошибок
}