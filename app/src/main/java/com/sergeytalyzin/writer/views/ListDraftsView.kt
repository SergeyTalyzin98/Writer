package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView

interface ListDraftsView: MvpView {
    fun showStartLoading()
    fun showEndLoading()
    fun setList(data: MutableList<Array<String>>)
    fun startWriteFragment(draftId: String)
}