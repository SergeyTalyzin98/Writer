package com.sergeytalyzin.writer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.views.NewsView

class NewsFragment: MvpAppCompatFragment(), NewsView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun startLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun endLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setList(works: List<DataForItemWork>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startReadFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startAuthorFragment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
