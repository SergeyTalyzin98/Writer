package com.sergeytalyzin.writer.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.sergeytalyzin.writer.activities.HostActivity
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.presenters.WritePresenter
import com.sergeytalyzin.writer.views.WriteView
import kotlinx.android.synthetic.main.fragment_write.*

class WriteFragment : MvpAppCompatFragment(), WriteView {

    @InjectPresenter
    lateinit var writePresenter: WritePresenter

    @ProvidePresenter
    fun provideWritePresenter(): WritePresenter {
        return WritePresenter(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.sergeytalyzin.writer.R.layout.fragment_write, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        writePresenter.draftId = arguments?.getString("draftId")!!

        if( writePresenter.draftId != "" )
            writePresenter.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_draft_write.setOnClickListener {
            writePresenter.addToDraft(Draft(titleWork = work_name_write.text.toString(),
                    descriptionWork = work_description_write.text.toString(), textWork = work_text_write.text.toString()))
        }

        btn_publish_write.setOnClickListener {
            writePresenter.publish(titleWork = work_name_write.text.toString(),
                descriptionWork = work_description_write.text.toString(), textWork = work_text_write.text.toString())
        }

        ic_poster_write.setOnClickListener {
            writePresenter.createIntent()
        }

        btn_delete_poster.setOnClickListener {
            writePresenter.deletePoster()
        }
    }

    override fun startActivityForGerPoster(intent: Intent, title: String, requestCode: Int) {
        startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        writePresenter.workWithPoster(requestCode = requestCode,
            resultCode = resultCode, data = data, contentResolver = activity!!.contentResolver)
    }

    override fun setDataInFragment(draft: Draft) {
        work_name_write.setText(draft.titleWork, TextView.BufferType.EDITABLE)
        work_description_write.setText(draft.descriptionWork, TextView.BufferType.EDITABLE)
        work_text_write.setText(draft.textWork, TextView.BufferType.EDITABLE)
    }

    override fun showLoadingPhoto() {
        ic_poster_write.visibility = View.GONE
        loading_poster_write.visibility = View.VISIBLE
    }

    override fun showEndLoadingPhoto() {
        loading_poster_write.visibility = View.GONE
        ic_poster_write.visibility = View.VISIBLE
    }

    override fun showLoadingAdd() {
        loading_add_write.visibility = View.VISIBLE
        buttons_write.visibility = View.GONE
    }

    override fun showEndLoadingAddDraft() {
        buttons_write.visibility = View.VISIBLE
        loading_add_write.visibility = View.GONE
    }

    override fun showLoadingGetDraft() {
        buttons_write.visibility = View.GONE
        wrapper_data_in_write.visibility = View.GONE
        loading_get_write.visibility = View.VISIBLE
    }

    override fun showEndLoadingGetDraft() {
        buttons_write.visibility = View.VISIBLE
        wrapper_data_in_write.visibility = View.VISIBLE
        loading_get_write.visibility = View.GONE
    }

    override fun showError(errorStr: Int) {
        Toast.makeText(context, resources.getText(errorStr), Toast.LENGTH_LONG).show()
    }

    override fun showPoster(poster: Bitmap) {
        btn_delete_poster.visibility = View.VISIBLE
        ic_poster_write.visibility = View.GONE
        poster_write.visibility = View.VISIBLE
        poster_write.setImageBitmap(poster)
    }

    override fun deletePoster() {
        btn_delete_poster.visibility = View.GONE
        ic_poster_write.visibility = View.VISIBLE
        poster_write.visibility = View.GONE
        poster_write.setImageDrawable(null)
    }

    override fun startProfileFragment() {
        (activity as HostActivity).onBackPressed()
    }

    override fun startListDraftsFragment() {
        (activity as HostActivity).changeFragment(ListDraftsFragment().newInstance(), false)
    }

    fun newInstance(draftId: String): WriteFragment {
        val args = Bundle()
        args.putString("draftId", draftId)
        val fragment = WriteFragment()
        fragment.arguments = args
        return fragment
    }
}
