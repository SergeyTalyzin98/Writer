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
    var workId = ""

    private fun addView(views: Int) = readProvider.addViewForWork(workId, views+1)

    fun loadDate () {

        if(!dataLoaded) {

            viewState.startLoading()

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

            FireBaseHelper().getWork(workId = workId, data = { work ->

                addView(work.views!!)

                FireBaseHelper().getAuthor(authorId = work.authorId!!, data = {

                    viewState.endLoading()
                    viewState.setWorkInLayout(work = work)
                    viewState.setUserInLayout(it)

                }, error = {

                })

            }, error = {

            })
        }
    }

    fun pressBtnRead() {

        iRead = if(!iRead) {
            readProvider.workWithIRead(workId = workId, add = true)
            viewState.changeTextInBtnIRead(I_READ)
            true
        }
        else {
            readProvider.workWithIRead(workId = workId, add = false)
            viewState.changeTextInBtnIRead(IN_READ)
            false
        }
    }

    companion object {
        const val I_READ = "Читаю"
        const val IN_READ = "В Читаю"
    }
}