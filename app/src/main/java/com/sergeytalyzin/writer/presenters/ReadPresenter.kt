package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.providers.ReadProvider
import com.sergeytalyzin.writer.views.ReadView

@InjectViewState
class ReadPresenter: MvpPresenter<ReadView>() {

    private val readProvider = ReadProvider()
    private var dataLoaded = false
    var authorId = ""
    var workId = ""

    private fun addView(views: Int) = readProvider.addViewForWork(authorId, workId, views+1)

    fun loadDate () {

        if(!dataLoaded) {

            viewState.showStartLoadingData()

            readProvider.getAuthor(authorId = authorId, data = {

                viewState.setUserInLayout(it)

            }, error = {

            })

            readProvider.getWork(authorId = authorId, workId = workId, data = { work ->

                addView(work.views!!)

                viewState.showEndLoadingData()
                viewState.setWorkInLayout(work = work)

            }, error = {

            })
        }
    }
}