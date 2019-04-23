package com.ismealdi.amrest.model.schema

import com.google.gson.annotations.SerializedName

/**
 * Created by Al
 * on 22/04/19 | 12:56
 */
data class User (
    var id: Int = 0,
    var name: String = "",
    var email: String = "",
    @SerializedName("api_token") var token: String = ""
)