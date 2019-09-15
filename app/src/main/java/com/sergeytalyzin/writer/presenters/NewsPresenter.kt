package com.sergeytalyzin.writer.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.sergeytalyzin.writer.providers.NewsProvider
import com.sergeytalyzin.writer.views.NewsView

@InjectViewState
class NewsPresenter: MvpPresenter<NewsView>() {

    private val newsProvider = NewsProvider()

    fun getWorks() {

        viewState.startLoading()
        newsProvider.getPosts {
            viewState.endLoading()
            viewState.setList(it)
        }
    }

    fun openWork(authorId: String, workId: String) {
        viewState.startReadFragment(authorId = authorId, workId = workId)
    }
}