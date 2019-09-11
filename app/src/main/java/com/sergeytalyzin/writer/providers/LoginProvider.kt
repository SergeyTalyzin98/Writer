package com.sergeytalyzin.writer.providers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.User
import com.sergeytalyzin.writer.presenters.LoginPresenter


class LoginProvider {

    private var database = FirebaseDatabase.getInstance().reference

    private fun writeNewUser(userId: String, user: User) {
        database.child("users").child(userId).setValue(user)
    }

    fun checkUserInFB(userId: String, user: User, loginPresenter: LoginPresenter) {

        database.ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child("users").child(userId).value != null) {

                    // данные в бд есть, переходим в HostActivity
                    loginPresenter.startHostActivity()
                }
                else {
                    // Данных нет, записывыаем...
                    writeNewUser(userId, user)

                    // Проверям снова
                    loginPresenter.checkUserInFB(userId = userId)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                loginPresenter.errorCheckDataInFB()
            }
        })
    }
}