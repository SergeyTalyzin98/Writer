package com.sergeytalyzin.writer.helpers

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKIdRequest(uids: IntArray = intArrayOf()) : VKRequest<String>("users.get") {

    init {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
    }

    override fun parse(r: JSONObject): String {

        val user = r.getJSONArray("response").getJSONObject(0)

        return user.getLong("id").toString()
    }
}