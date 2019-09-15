package com.sergeytalyzin.writer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
        val name: String? = "",
        var city: String? = "",
        val photo_100: String? = "",
        val photo_200: String? = ""
)