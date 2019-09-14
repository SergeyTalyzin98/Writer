package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.models.FireBaseHelper
import com.sergeytalyzin.writer.providers.ReadProvider
import com.sergeytalyzin.writer.views.ReadView

@InjectViewState
class ReadPresenter: MvpPresenter<ReadView>() {

    private val readProvider = ReadProvider()
    private var dataLoaded = false
    private var iRead = false
    var authorId = ""
    var workId = ""

    private fun addView(views: Int) = readProvider.addViewForWork(authorId, workId, views+1)

    fun loadDate () {

        if(!dataLoaded) {

            readProvider.checkIRead(workId = workId, answer = {

                iRead = if(it) {
                    viewState.changeTextInBtnIRead(I_READ)
                    true
                }
                else {
                    viewState.changeTextInBtnIRead(IN_READ)
                    false
                }
            })

            FireBaseHelper().getAuthor(authorId = authorId, data = {

                viewState.setUserInLayout(it)

            }, error = {

            })

            FireBaseHelper().getWork(authorId = authorId, workId = workId, data = { work ->

                addView(work.views!!)
                viewState.setWorkInLayout(work = work)

            }, error = {

            })
        }
    }

    fun pressBtnRead() {

        iRead = if(!iRead) {
            readProvider.workWithIRead(authorId = authorId, workId = workId, add = true)
            viewState.changeTextInBtnIRead(I_READ)
            true
        }
        else {
            readProvider.workWithIRead(authorId = authorId, workId = workId, add = false)
            viewState.changeTextInBtnIRead(IN_READ)
            false
        }
    }

    companion object {
        const val I_READ = "Читаю"
        const val IN_READ = "В Читаю"
    }
}