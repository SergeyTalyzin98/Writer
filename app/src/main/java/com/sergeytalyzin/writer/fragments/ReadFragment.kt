package com.sergeytalyzin.writer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.helpers.parseDate
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.User
import com.sergeytalyzin.writer.presenters.ReadPresenter
import com.sergeytalyzin.writer.views.ReadView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment : MvpAppCompatFragment(), ReadView {

    @InjectPresenter
    lateinit var readPresenter: ReadPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        readPresenter.authorId = arguments!!.getString("authorId")!!
        readPresenter.workId = arguments!!.getString("workId")!!

        readPresenter.loadDate()

        in_read_btn.setOnClickListener {
            readPresenter.pressBtnRead()
        }
    }

    override fun changeTextInBtnIRead(text: String) {
        in_read_btn.text = text
    }

    override fun setUserInLayout(author: User) {
        Picasso.with(context).load(author.photo_100).into(avatar_read)
        val name = "${author.first_name} ${author.last_name}"
        name_read.text = name
    }

    override fun setWorkInLayout(work: Draft) {

        if(work.posterDownloadUrl != "")
            Picasso.with(context).load(work.posterDownloadUrl).into(poster_read)
        date_read.text = parseDate(work.date!!)
        title_read.text = work.titleWork
        text_read.text = work.textWork
    }

    fun newInstance(authorId: String, workId: String): ReadFragment {
        val args = Bundle()
        args.putString("authorId", authorId)
        args.putString("workId", workId)
        val fragment = ReadFragment()
        fragment.arguments = args
        return fragment
    }
}
