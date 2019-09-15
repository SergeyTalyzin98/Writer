package com.sergeytalyzin.writer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.activities.HostActivity
import com.sergeytalyzin.writer.adapters.ClickCallback
import com.sergeytalyzin.writer.adapters.WorksAdapter
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.presenters.NewsPresenter
import com.sergeytalyzin.writer.views.NewsView
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment: MvpAppCompatFragment(), NewsView {

    @InjectPresenter
    lateinit var newsPresenter: NewsPresenter
    private val worksAdapter = WorksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        worksAdapter.attachDelegate(object : ClickCallback {

            override fun clickWork(authorId: String, workId: String) {
                newsPresenter.openWork(authorId = authorId, workId = workId)
            }

            override fun clickUser(authorId: String) {

            }
        })

        newsPresenter.getWorks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        works_recycler_news.layoutManager = LinearLayoutManager(context)
        works_recycler_news.adapter = worksAdapter
    }

    override fun startLoading() {
        loading_news.visibility = View.VISIBLE
        works_recycler_news.visibility = View.GONE
    }

    override fun endLoading() {
        loading_news.visibility = View.GONE
        works_recycler_news.visibility = View.VISIBLE
    }

    override fun setList(works: List<DataForItemWork>) {
        worksAdapter.setListDrafts(works)
    }

    override fun startReadFragment(authorId: String, workId: String) {
        (activity as HostActivity).changeFragment(ReadFragment().newInstance(authorId = authorId, workId = workId))
    }

    override fun startAuthorFragment(authorId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
