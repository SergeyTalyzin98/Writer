package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataAboutUser
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.models.FireBaseHelper

class ProfileProvider {

    private val fireBaseHelper = FireBaseHelper()

    fun getPostsFromIRead(answer: (works: List<DataForItemWork>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("iRead").child(DataAboutUser.getId())

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()
                var lastIteration = false

                for((index, workId) in dataSnapshot.children.withIndex()) {

                    if(index.toLong()+1 == dataSnapshot.childrenCount)
                        lastIteration = true

                    fireBaseHelper.getWork(workId = workId.key!!, data = { work ->

                        fireBaseHelper.getAuthor(authorId = work.authorId!!, data = { author ->

                            works.add(DataForItemWork(
                                    workId = workId.key!!, work = work,
                                    avatarAuthor = author.photo_100!!,
                                    nameAuthor = author.name!!)
                            )

                            if(lastIteration) answer(works)

                        }, error = {})

                    }, error = {})
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }
}

