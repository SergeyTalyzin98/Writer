package com.sergeytalyzin.writer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sergeytalyzin.writer.R
import com.sergeytalyzin.writer.helpers.parseDate
import com.sergeytalyzin.writer.models.DataForItemWork
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface ClickCallback {
    fun clickUser(authorId: String)
    fun clickWork(authorId: String, workId: String)
}

class WorksAdapter: RecyclerView.Adapter<WorksAdapter.WorksViewHolder>() {

    private val data = mutableListOf<DataForItemWork>()
    private lateinit var delegate: ClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorksViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)

        return WorksViewHolder(delegate, view)
    }

    override fun onBindViewHolder(holder: WorksViewHolder, position: Int) {

        GlobalScope.launch(Dispatchers.Main) {
            holder.bind(data[(data.size-1) - position])
        }
    }

    override fun getItemCount() = data.size

    fun setListDrafts(list: List<DataForItemWork>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun attachDelegate(delegate: ClickCallback) {
        this.delegate = delegate
    }

    class WorksViewHolder(private val delegate: ClickCallback, itemView: View): RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name_item_work)
        private val avatar = itemView.findViewById<ImageView>(R.id.avatar_item_work)
        private val titleWork = itemView.findViewById<TextView>(R.id.title_item_work)
        private val poster = itemView.findViewById<ImageView>(R.id.poster_item_work)
        private val description = itemView.findViewById<TextView>(R.id.description_item_work)
        private val dateItemWork = itemView.findViewById<TextView>(R.id.date_item_work)
        private val viewsCountItemWork = itemView.findViewById<TextView>(R.id.views_count_item_work)
        private val dataAboutUserItemWork = itemView.findViewById<ConstraintLayout>(R.id.data_about_user_item_work)
        private val dataAboutWorkItemWork = itemView.findViewById<ConstraintLayout>(R.id.data_about_work_item_work)

        fun bind(data: DataForItemWork) {

            dataAboutUserItemWork.setOnClickListener {
                it.isPressed = true
                delegate.clickUser(authorId = data.work.authorId!!)
            }

            dataAboutWorkItemWork.setOnClickListener {
                it.isPressed = true
                delegate.clickWork(workId = data.workId, authorId = data.work.authorId!!)
            }

            Picasso.with(itemView.context).load(data.avatarAuthor).into(avatar)

            if (data.work.posterDownloadUrl != "") {
                Picasso.with(itemView.context).load(data.work.posterDownloadUrl).into(poster)
            }
            else
                poster.visibility = View.GONE

            name.text = data.nameAuthor
            titleWork.text = data.work.titleWork
            description.text = data.work.descriptionWork
            viewsCountItemWork.text = data.work.views.toString()
            dateItemWork.text = parseDate(data.work.date!!)
        }
    }
}