package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.User

class ReadProvider {

    private var database = FirebaseDatabase.getInstance().reference

    fun addViewForWork(authorId: String, workId: String, views: Int) {

        database.child("posts").child(authorId).child(workId).child("views").setValue(views)
    }

    fun getWork(authorId: String, workId: String, data: (work: Draft) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("posts").child(authorId).child(workId)

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                data(dataSnapshot.getValue(Draft::class.java)!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }

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
}