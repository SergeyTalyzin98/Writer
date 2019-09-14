package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataAboutUser
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.FireBaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileProvider {

    private val fireBaseHelper = FireBaseHelper()

    fun getPostsByUser(data: (works: List<DataForItemWork>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts").child(DataAboutUser.getId())

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()

                GlobalScope.launch(Dispatchers.Main) {

                    dataSnapshot.children.forEach {
                        val work = it.getValue(Draft::class.java)!!

                        works.add(
                                DataForItemWork(
                                        workId = it.key!!, work = work,
                                        avatarAuthor = DataAboutUser.getPhoto100(),
                                        nameAuthor = DataAboutUser.getName(), authorId = DataAboutUser.getId()
                                )
                        )
                    }
                    data(works)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }

    fun getPostsFromIRead(answer: (works: List<DataForItemWork>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("iRead").child(DataAboutUser.getId())

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()
                var ans1 = false
                var ans2 = false

                for((indexAuthor, authorId) in dataSnapshot.children.withIndex()) {

                    if(indexAuthor.toLong()+1 == dataSnapshot.childrenCount)
                        ans1 = true

                    fireBaseHelper.getAuthor(data = { author ->

                        for((indexWork, workId) in authorId.children.withIndex()) {

                            if(indexWork.toLong()+1 == dataSnapshot.childrenCount)
                                ans2 = true

                            fireBaseHelper.getWork(data = { work ->

                                works.add(DataForItemWork(
                                        workId = workId.key!!, work = work,
                                        avatarAuthor = author.photo_100!!,
                                        nameAuthor = "${author.first_name} ${author.last_name}",
                                        authorId = authorId.key!!))

                                if(ans1 && ans2)
                                    answer(works)

                            }, error = {}, authorId = authorId.key!!, workId = workId.key!!)
                        }
                    }, error = {}, authorId = authorId.key!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }
}

