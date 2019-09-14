package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.providers.ProfileProvider
import com.sergeytalyzin.writer.views.ProfileView

@InjectViewState
class ProfilePresenter: MvpPresenter<ProfileView>() {

    private val profileProvider = ProfileProvider()

    fun setUser() {
        viewState.setDataAboutUser()
    }

    fun getWorksAndSet() {

        profileProvider.getPostsByUser(data = { works ->

            viewState.showWorks(works)

        }, error = {

        })
    }

    fun getReadAndSet() {

        profileProvider.getPostsFromIRead(answer = {

            viewState.showRead(it)

        }, error = {

        })
    }

    fun openAuthor(authorId: String) {

    }

    fun openWork(authorId: String, workId: String) {
        viewState.startReadFragment(authorId = authorId, workId = workId)
    }
}