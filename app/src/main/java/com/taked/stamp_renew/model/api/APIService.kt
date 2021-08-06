package com.taked.stamp_renew.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @POST("stamp/image")
    suspend fun image(@Body request: ImageRequest): ImageResponse
}

class APIClient {
    companion object {
        private const val BASE_URL = "http://13.113.250.233:1323/"

        val instance: APIService by lazy {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            retrofit.create(APIService::class.java)
        }

    }
}