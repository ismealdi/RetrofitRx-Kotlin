package com.ismealdi.amrest.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Al
 * on 22/04/19 | 12:56
 */
data class SignUpRequest (
    var name: String,
    var email: String,
    var password: String,
    @SerializedName("password_confirmation") var confirm: String
)
