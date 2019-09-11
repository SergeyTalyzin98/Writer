package com.sergeytalyzin.writer.views

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpView

interface HostView: MvpView {
    fun startLogin()
    fun showBottomPanel()
    fun closeBottomPanel()
    fun changeFragment(fragment: Fragment, addToBackStack: Boolean = true)
    fun showData()
}