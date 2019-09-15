package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.FireBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsProvider {

    fun getPosts(data: (works: List<DataForItemWork>) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts")

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()
                var lastIteration = false

                GlobalScope.launch {

                    for((i, w) in dataSnapshot.children.withIndex()) {

                        if(i.toLong()+1 == dataSnapshot.childrenCount)
                            lastIteration = true

                        val work = w.getValue(Draft::class.java)!!
                        val authorId = w.child("authorId").value.toString()

                        FireBaseHelper().getAuthor(authorId = authorId,
                                data = { author ->

                                    works.add(DataForItemWork(
                                            workId = w.key!!, work = work,
                                            avatarAuthor = author.photo_100!!,
                                            nameAuthor = author.name!!)
                                    )

                                    if(lastIteration)
                                        data(works)
                                },
                                error = {})
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        data(works)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }
}