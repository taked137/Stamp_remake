package com.taked.stamp.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

interface APIService {
    @POST("user/create")
    suspend fun register(@Body request: UserRequest): UserResponse

    @POST("stamp/beacon")
    suspend fun beacon(@Header("uuid") uuid: String, @Body request: BeaconRequest): BeaconResponse

    @GET("stamp/image/{num}")
    suspend fun image(@Header("uuid") uuid: String, @Path("num") num: Int): ImageResponse

    @POST("stamp/judge")
    suspend fun judge(@Header("uuid") uuid: String, @Body request: AnswerRequest): AnswerResponse

    @POST("user/goal")
    suspend fun goal(@Header("uuid") uuid: String): GoalResponse

    @GET("info")
    suspend fun info(@Query("limit") limit: Int, @Query("offset") offset: Int): InfoResponse
}

object APIClient {
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

    fun mockInstance(): APIService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val behavior = NetworkBehavior.create()
        behavior.setDelay(100, TimeUnit.MILLISECONDS)
        behavior.setFailurePercent(0)
        behavior.setErrorPercent(0)

        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(behavior)
            .build()

        val delegate = mockRetrofit.create(APIService::class.java)
        return MockAPIService(delegate)
    }
}

class MockAPIService(private val delegate: BehaviorDelegate<APIService>) : APIService {

    override suspend fun goal(uuid: String): GoalResponse {
        val response = GoalResponse(uuid.isNotBlank())
        return delegate.returningResponse(response).goal(uuid)
    }

    override suspend fun beacon(uuid: String, request: BeaconRequest): BeaconResponse {
        val response = BeaconResponse(
            1,
            2,
            "https://3.bp.blogspot.com/-xldWJgF1gGk/W8hD-3zNpFI/AAAAAAABPhM/itwOYZLlTNMyN7EKL2yv2-rxzAYoLYASQCLcBGAs/s800/pet_dog_birthday_cake.png"
        )
        return delegate.returningResponse(response).beacon(uuid, request)
    }

    override suspend fun image(uuid: String, num: Int): ImageResponse {
        val response =
            ImageResponse("https://1.bp.blogspot.com/-HoR5t6KAVjg/YLbYUIhzIrI/AAAAAAABdzg/0C2xix-0wdgRGXpxSjril_5L2OyWVvweACNcBGAsYHQ/s868/vaccine_yoyaku_smartphone_no_oldman.png")
        return delegate.returningResponse(response).image(uuid, num)
    }

    override suspend fun info(limit: Int, offset: Int): InfoResponse {
        val response =
            InfoResponse(listOf(Message("message01"), Message("message02")))
        return delegate.returningResponse(response).info(limit, offset)
    }

    override suspend fun judge(uuid: String, request: AnswerRequest): AnswerResponse {
        val response = AnswerResponse(1, request.quiz == 5 && request.answer == "はじっこ")
        return delegate.returningResponse(response).judge(uuid, request)
    }

    override suspend fun register(request: UserRequest): UserResponse {
        val response = UserResponse("uuid")
        return delegate.returningResponse(response).register(request)
    }
}
