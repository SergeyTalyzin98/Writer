package com.sergeytalyzin.writer.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FireBaseHelper {

    fun getAuthor(authorId: String, data: (user: User) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("users").child(authorId)

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                data(dataSnapshot.getValue(User::class.java)!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                    error(databaseError)
            }
        })
    }

    fun getWork(workId: String, data: (work: Draft) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts")

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                data(dataSnapshot.child(workId).getValue(Draft::class.java)!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }

    fun getPostsByAuthor(authorId: String, author: User, data: (works: List<DataForItemWork>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts")

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()

                GlobalScope.launch {

                    dataSnapshot.children.forEach {
                        if (it.child("authorId").value.toString() == authorId) {
                            val work = it.getValue(Draft::class.java)!!

                            works.add(DataForItemWork(
                                    workId = it.key!!, work = work,
                                    avatarAuthor = author.photo_100!!,
                                    nameAuthor = author.name!!)
                            )
                        }
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
