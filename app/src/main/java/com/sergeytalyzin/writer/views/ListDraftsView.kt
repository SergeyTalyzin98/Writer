package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView
import com.sergeytalyzin.writer.adapters.DraftsAdapter

interface ListDraftsView: MvpView {
    fun showStartLoading()
    fun showEndLoading()
    fun setList(data: MutableList<Array<String>>)
    fun getObjectDraftsAdapter(adapterDrafts: DraftsAdapter)
    fun startWriteFragment(draftId: String)
}