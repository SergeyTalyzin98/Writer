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
import com.sergeytalyzin.writer.models.DataAboutUser
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.presenters.ProfilePresenter
import com.sergeytalyzin.writer.views.ProfileView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : MvpAppCompatFragment(), ProfileView {

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter
    private val worksAdapter = WorksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        worksAdapter.attachDelegate(object : ClickCallback {

            override fun clickUser(authorId: String) {

            }

            override fun clickWork(authorId: String, workId: String) {
                profilePresenter.openWork(authorId = authorId, workId = workId)
            }
        })

        profilePresenter.setUser()
        profilePresenter.getWorksAndSet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        works_recycler_profile.layoutManager = LinearLayoutManager(context)
        works_recycler_profile.adapter = worksAdapter

        btn_update_data_about_user_profile.setOnClickListener {

        }

        btn_add_history_profile.setOnClickListener {
            (activity as HostActivity).changeFragment(WriteFragment().newInstance(draftId = ""))
        }

        btn_drafts_profile.setOnClickListener {
            (activity as HostActivity).changeFragment(ListDraftsFragment().newInstance())
        }

        btn_my_work_profile.setOnClickListener {
            profilePresenter.getWorksAndSet()
        }

        btn_i_read_profile.setOnClickListener {
            profilePresenter.getReadAndSet()
        }
    }

    override fun showStartLoading() {
        loading_profile.visibility = View.VISIBLE
        data_in_profile.visibility = View.GONE
    }

    override fun showEndLoading() {
        loading_profile.visibility = View.GONE
        data_in_profile.visibility = View.VISIBLE
    }

    override fun startReadFragment(authorId: String, workId: String) {
        (activity as HostActivity).changeFragment(ReadFragment().newInstance(authorId = authorId, workId = workId))
    }

    override fun showError(strId: Int, errorCode: Int) {
        val text = "${resources.getText(strId)} $errorCode"
        error_profile.text = text
        error_profile.visibility = View.VISIBLE
    }

    override fun setDataAboutUser() {
        Picasso.with(context).load(DataAboutUser.getPhoto200()).into(avatar_profile)
        name_profile.text = DataAboutUser.getName()
        city_profile.text = DataAboutUser.getCity()
    }

    override fun showWorks(works: List<DataForItemWork>) {
        btn_my_work_profile.setTextColor(resources.getColor(R.color.colorPrimary))
        btn_i_read_profile.setTextColor(resources.getColor(R.color.colorBlack))
        worksAdapter.setListDrafts(works)
    }

    override fun showRead(works: List<DataForItemWork>) {
        btn_my_work_profile.setTextColor(resources.getColor(R.color.colorBlack))
        btn_i_read_profile.setTextColor(resources.getColor(R.color.colorPrimary))
        worksAdapter.setListDrafts(works)
    }
}


