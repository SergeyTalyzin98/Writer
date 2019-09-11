package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.User

class HostProvider {

    fun getUser(userId: String, data: (user: User) -> Unit, error: (databaseError: DatabaseError) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("users").child(userId)

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