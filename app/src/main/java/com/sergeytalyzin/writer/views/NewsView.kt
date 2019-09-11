package com.sergeytalyzin.writer.views

import com.sergeytalyzin.writer.models.DataForItemWork

interface NewsView {
    fun startLoading()
    fun endLoading()
    fun setList(works: List<DataForItemWork>)
    fun startReadFragment()
    fun startAuthorFragment()
}