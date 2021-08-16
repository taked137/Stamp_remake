package com.taked.stamp_renew.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface APIService {
    @POST("user/create")
    suspend fun register(@Body request: UserRequest): UserResponse

    @POST("stamp/beacon")
    suspend fun beacon(@Header("uuid") uuid: String, @Body request: BeaconRequest): BeaconResponse

    @GET("stamp/image/{num}")
    suspend fun image(@Header("uuid") uuid: String, @Path("num") ID: Int): ImageResponse

    @POST("stamp/judge")
    suspend fun judge(@Header("uuid") uuid: String, @Body request: AnswerRequest): AnswerResponse

    @POST("user/goal")
    suspend fun goal(@Header("uuid") uuid: String): GoalResponse
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