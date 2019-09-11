package com.sergeytalyzin.writer.views

import com.arellomobile.mvp.MvpView
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.User

interface ReadView: MvpView {
    fun showStartLoadingData()
    fun showEndLoadingData()
    fun setUserInLayout(author: User)
    fun setWorkInLayout(work: Draft)
}