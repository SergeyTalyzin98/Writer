package com.sergeytalyzin.writer.helpers

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKIdRequest: VKRequest<String> {
    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            addParam("user_ids", uids.joinToString(","))
        }
    }

    override fun parse(r: JSONObject): String {

        val user = r.getJSONArray("response").getJSONObject(0)

        return user.getLong("id").toString()
    }
}