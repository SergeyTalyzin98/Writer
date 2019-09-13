package com.sergeytalyzin.writer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sergeytalyzin.writer.R

interface ClickDraft {
    fun click(draftId: String)
}

class DraftsAdapter: RecyclerView.Adapter<DraftsAdapter.ProfileViewHolder>() {

    private val data = mutableListOf<Array<String>>()
    private lateinit var delegate: ClickDraft

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_darfts_list, parent, false)

        return ProfileViewHolder(delegate, view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun setListDrafts(list: MutableList<Array<String>>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun attachDelegate(delegate: ClickDraft) {
        this.delegate = delegate
    }

    class ProfileViewHolder(private val delegate: ClickDraft, itemView: View): RecyclerView.ViewHolder(itemView) {

        private val wrapperItem = itemView.findViewById<ConstraintLayout>(R.id.wrapper_item_drafts_list)
        private val titleWork = itemView.findViewById<TextView>(R.id.titleWorkDraftsList)

        fun bind(data: Array<String>) {
            titleWork.text = data[1]

            wrapperItem.setOnClickListener {
                delegate.click(data[0])
            }
        }
    }
}