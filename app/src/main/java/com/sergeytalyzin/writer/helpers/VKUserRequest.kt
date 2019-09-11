package com.sergeytalyzin.writer.helpers

import com.sergeytalyzin.writer.models.User
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKUsersRequest: VKRequest<Map<String, Any>> {
    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", arrayOf("photo_100", "photo_200", "city"))
    }

    override fun parse(r: JSONObject): Map<String, Any> {
        val user = r.getJSONArray("response").getJSONObject(0)
        var city: String

        try{

            city = user.getJSONObject("city").getString("title")

        } catch (e: Exception) {
            city = "Город не указан"
        }

        return mapOf(
            "userId" to  user.getString("id"),
            "user_object" to User(
                first_name = user.getString("first_name"),
                last_name = user.getString("last_name"),
                city = city,
                photo_100 = user.getString("photo_100"),
                photo_200 = user.getString("photo_200")
            )
        )
    }
}