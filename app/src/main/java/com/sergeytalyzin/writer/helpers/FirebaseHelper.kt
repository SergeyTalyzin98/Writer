package com.sergeytalyzin.writer.helpers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.models.Draft
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseHelper {


    fun getPostsByUser(data: (works: List<DataForItemWork>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts").child(User.getId())

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val works = mutableListOf<DataForItemWork>()

                GlobalScope.launch(Dispatchers.Main) {

                    dataSnapshot.children.forEach {
                        val work = it.getValue(Draft::class.java)!!

                        works.add(
                            DataForItemWork(
                                workId = it.key!!, work = work,
                                avatarAuthor = User.getPhoto100(), nameAuthor = User.getName(), authorId = User.getId()
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
}