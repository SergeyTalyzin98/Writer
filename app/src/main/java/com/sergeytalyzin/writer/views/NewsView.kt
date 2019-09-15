package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.sergeytalyzin.writer.models.DataForItemWork

interface NewsView: MvpView {
    fun startLoading()
    fun endLoading()
    fun setList(works: List<DataForItemWork>)
    @StateStrategyType(value = SkipStrategy::class)
    fun startReadFragment(authorId: String, workId: String)
    fun startAuthorFragment(authorId: String)
}