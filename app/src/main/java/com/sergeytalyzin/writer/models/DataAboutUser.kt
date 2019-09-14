package com.sergeytalyzin.writer.models

object DataAboutUser {
    private var created = false
    private lateinit var id: String
    private lateinit var name: String
    private lateinit var city: String
    private lateinit var photo_100: String
    private lateinit var photo_200: String

    fun create(id: String, name: String, city: String, photo_100: String, photo_200: String) {

        if(!created) {

            DataAboutUser.id = id
            DataAboutUser.name = name
            DataAboutUser.city = city
            DataAboutUser.photo_100 = photo_100
            DataAboutUser.photo_200 = photo_200
            created = true
        }
    }

    fun getId() = id
    fun getName() = name
    fun getCity() = city
    fun getPhoto100() = photo_100
    fun getPhoto200() = photo_200
    fun getCreated() = created
}