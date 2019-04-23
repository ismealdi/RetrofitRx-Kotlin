package com.ismealdi.amrest.util

import android.content.Context
import android.util.Log
import com.ismealdi.amrest.App
import com.ismealdi.amrest.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


/**
 * Created by Al
 * on 22/04/19 | 12:56
 */

class Networks {
    private val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("AmHttp", "AmHttp: , $it") })
    
    fun bridge(context: Context) : Retrofit{

        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(120, TimeUnit.SECONDS)
        
        logging.level = HttpLoggingInterceptor.Level.BODY
        
        httpClient.addInterceptor { chain ->
                val ongoing = chain.request().newBuilder()
                ongoing.addHeader("Accept", "application/json;versions=1")

                val token = Preferences(context).getToken()

                if (token.isNotEmpty()) {
                    ongoing.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(ongoing.build())


            }
                
        httpClient.addInterceptor(logging)


        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient.build())
            .build()
    }
}