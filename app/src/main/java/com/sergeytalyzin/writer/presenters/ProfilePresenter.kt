package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.models.DataAboutUser
import com.sergeytalyzin.writer.models.FireBaseHelper
import com.sergeytalyzin.writer.models.User
import com.sergeytalyzin.writer.providers.ProfileProvider
import com.sergeytalyzin.writer.views.ProfileView

@InjectViewState
class ProfilePresenter: MvpPresenter<ProfileView>() {

    private val profileProvider = ProfileProvider()
    private val fireBaseHelper = FireBaseHelper()

    fun setUser() {
        viewState.setDataAboutUser()
    }

    fun getWorksAndSet() {

        val user = User(
                name = DataAboutUser.getName(),
                photo_100 = DataAboutUser.getPhoto100()
        )

        fireBaseHelper.getPostsByAuthor(data = { works ->

            viewState.showWorks(works)

        }, error = {

        }, authorId = DataAboutUser.getId(), author = user)
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