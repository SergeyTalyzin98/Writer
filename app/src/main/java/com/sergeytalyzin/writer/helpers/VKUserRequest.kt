package com.sergeytalyzin.writer.helpers

import com.sergeytalyzin.writer.models.User
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKUserRequest(uids: IntArray = intArrayOf()) : VKRequest<Map<String, Any>>("users.get") {
    init {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", arrayOf("photo_100", "photo_200", "city"))
    }

    override fun parse(r: JSONObject): Map<String, Any> {
        val user = r.getJSONArray("response").getJSONObject(0)

        val city = try{
            user.getJSONObject("city").getString("title")
        } catch (e: Exception) {
            "Город не указан"
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