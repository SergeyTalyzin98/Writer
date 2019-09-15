package com.sergeytalyzin.writer.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Draft(val titleWork: String? = "",
                 var posterDownloadUrl: String? = "",
                 val descriptionWork: String? = "",
                 val textWork: String? = "",
                 val date: Long? = 0L,
                 val views: Int? = 0,
                 val authorId: String? = ""
)