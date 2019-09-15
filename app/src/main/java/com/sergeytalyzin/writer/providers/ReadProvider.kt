package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataAboutUser

class ReadProvider {

    private var database = FirebaseDatabase.getInstance().reference

    fun addViewForWork(workId: String, views: Int) {

        database.child("posts").child(workId).child("views").setValue(views)
    }

    fun checkIRead(workId: String, answer: (answer: Boolean) -> Unit) {

        val database = FirebaseDatabase.getInstance().reference.child("iRead").child(DataAboutUser.getId())

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var ans = false

                dataSnapshot.children.forEach {

                    if(workId == it.key) ans = true
                }
                answer(ans)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                error(databaseError)
            }
        })
    }

    fun workWithIRead(workId: String, add: Boolean) {
        val database = FirebaseDatabase.getInstance().reference
                .child("iRead").child(DataAboutUser.getId())

        if(add)
            database.child(workId).setValue("")
        else
            database.child(workId).removeValue()

    }
}