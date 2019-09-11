package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListDraftsProvider {

    fun getDraftsByUser(userId: String, data: (user: MutableList<Array<String>>) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("drafts").child(userId)

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val d = mutableListOf<Array<String>>()

                dataSnapshot.children.forEach {
                    d.add(arrayOf(it.key!!, "${it.child("titleWork").value}"))
                }

                data(d)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }
}