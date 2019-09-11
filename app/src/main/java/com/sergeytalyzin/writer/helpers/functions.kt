package com.sergeytalyzin.writer.helpers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sergeytalyzin.writer.models.DataForItemWork
import com.sergeytalyzin.writer.models.Draft
import com.sergeytalyzin.writer.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

fun parseDate(time: Long) : String {

    val date = Calendar.getInstance()
    date.time = Date(time)
    var dayOfMonth = ""
    var month = ""

    if(date.get(Calendar.DAY_OF_MONTH) < 10)
        dayOfMonth = "0"

    if(date.get(Calendar.MONTH) < 10)
        month = "0"

    return "$dayOfMonth${date.get(Calendar.DAY_OF_MONTH)}.$month${date.get(Calendar.MONTH)+1}.${date.get(Calendar.YEAR)-2000}"
}