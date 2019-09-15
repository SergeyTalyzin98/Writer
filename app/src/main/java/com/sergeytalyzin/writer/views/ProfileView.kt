package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sergeytalyzin.writer.models.DataForItemWork

interface ProfileView: MvpView {
    fun showError(strId: Int, errorCode: Int = -10) //
    fun setDataAboutUser()
    fun showWorks(works: List<DataForItemWork>)
    fun showRead(works: List<DataForItemWork>)
    fun showStartLoading() // Показывает загрузку
    fun showEndLoading() // остановка загрузки
    @StateStrategyType(value = SkipStrategy::class)
    fun startReadFragment(workId: String) //
}