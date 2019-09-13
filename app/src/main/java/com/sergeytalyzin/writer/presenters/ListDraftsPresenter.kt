package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.helpers.User
import com.sergeytalyzin.writer.providers.ListDraftsProvider
import com.sergeytalyzin.writer.views.ListDraftsView

@InjectViewState
class ListDraftsPresenter: MvpPresenter<ListDraftsView>() {

    private var dataLoaded = false
    private val listDraftsProvider = ListDraftsProvider()

    fun loadDate() {

        if(!dataLoaded) {

            viewState.showStartLoading()

            listDraftsProvider.getDraftsByUser(userId = User.getId(), data = { data ->

                dataLoaded = true
                viewState.showEndLoading()
                viewState.setList(data)

            }, error = {

            })
        }
    }

    fun pressOnItem(draftId: String) {
        viewState.startWriteFragment(draftId = draftId)
    }
}