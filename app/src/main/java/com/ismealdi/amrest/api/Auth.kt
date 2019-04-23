package com.ismealdi.amrest.api

import android.content.Context
import com.ismealdi.amrest.model.request.SignInRequest
import com.ismealdi.amrest.model.request.SignUpRequest
import com.ismealdi.amrest.model.response.BaseResponse
import com.ismealdi.amrest.model.schema.User
import com.ismealdi.amrest.util.Networks
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Al
 * on 22/04/19 | 12:54
 */
interface Auth {

    @POST("login")
    @Headers("Content-Type: application/json")
    fun signIn(@Body signInRequest: SignInRequest): Observable<BaseResponse<User>>

    @POST("logout")
    @Headers("Content-Type: application/json")
    fun signOut(): Observable<BaseResponse<String>>

    @POST("register")
    @Headers("Content-Type: application/json")
    fun register(@Body signUpRequest: SignUpRequest): Observable<BaseResponse<User>>

    @GET("users")
    @Headers("Content-Type: application/json")
    fun users(): Observable<BaseResponse<List<User>>>

    companion object {
        fun init(context: Context): Auth {
            return Networks().bridge(context).create(Auth::class.java)
        }
    }
    
}