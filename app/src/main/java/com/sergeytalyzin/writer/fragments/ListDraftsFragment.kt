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
import com.sergeytalyzin.writer.adapters.ClickDraft
import com.sergeytalyzin.writer.adapters.DraftsAdapter
import com.sergeytalyzin.writer.presenters.ListDraftsPresenter
import com.sergeytalyzin.writer.views.ListDraftsView
import kotlinx.android.synthetic.main.fragment_list_drafts.*

class ListDraftsFragment : MvpAppCompatFragment(), ListDraftsView {

    @InjectPresenter
    lateinit var listDraftsPresenter: ListDraftsPresenter
    private val adapterDrafts = DraftsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterDrafts.attachDelegate(object : ClickDraft {

            override fun click(draftId: String) {
                listDraftsPresenter.pressOnItem(draftId = draftId)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_drafts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        listDraftsPresenter.loadDate()

        drafts_recycler_view_list_drafts.layoutManager = LinearLayoutManager(context!!)
        drafts_recycler_view_list_drafts.adapter = adapterDrafts
    }

    override fun startWriteFragment(draftId: String) {
        (activity as HostActivity).changeFragment(WriteFragment().newInstance(draftId = draftId), false)
    }

    override fun showStartLoading() {
        drafts_recycler_view_list_drafts.visibility = View.GONE
        loading_list_drafts.visibility = View.VISIBLE
    }

    override fun showEndLoading() {
        loading_list_drafts.visibility = View.GONE
        drafts_recycler_view_list_drafts.visibility = View.VISIBLE
    }

    override fun setList(data: MutableList<Array<String>>) {
        adapterDrafts.setListDrafts(data)
    }

    fun newInstance() = ListDraftsFragment()
}
