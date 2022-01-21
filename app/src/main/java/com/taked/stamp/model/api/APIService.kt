package com.taked.stamp.model.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

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

    @GET("info/title")
    suspend fun infotitle(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): InfoTitleResponse

    @GET("info/content/{num}")
    suspend fun infocontent(@Path("num") num: Int): InfoContentResponse

    @GET("map/checkpoint")
    suspend fun map(): MapResponse

    @GET("event")
    suspend fun event(): EventResponse

    @GET("event/schedule")
    suspend fun schedule(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ScheduleResponse
}

@Singleton
class APIClient @Inject constructor() {
    companion object {
        const val BASE_URL = "http://13.113.250.233:1323/"
    }

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
        val response = BeaconResponse(1, 2, "https://hogehoge")
        return delegate.returningResponse(response).beacon(uuid, request)
    }

    override suspend fun image(uuid: String, num: Int): ImageResponse {
        val response = ImageResponse("https://fugafuga")
        return delegate.returningResponse(response).image(uuid, num)
    }

    override suspend fun infotitle(limit: Int, offset: Int): InfoTitleResponse {
        val response =
            InfoTitleResponse(listOf(Message(1, "message01"), Message(2, "message02")))
        return delegate.returningResponse(response).infotitle(limit, offset)
    }

    override suspend fun infocontent(num: Int): InfoContentResponse {
        val response =
            InfoContentResponse(1, "title", "category", "message")
        return delegate.returningResponse(response).infocontent(num)
    }

    override suspend fun judge(uuid: String, request: AnswerRequest): AnswerResponse {
        val response = AnswerResponse(1, request.quiz == 5 && request.answer == "はじっこ")
        return delegate.returningResponse(response).judge(uuid, request)
    }

    override suspend fun register(request: UserRequest): UserResponse {
        val response = UserResponse("uuid")
        return delegate.returningResponse(response).register(request)
    }

    override suspend fun map(): MapResponse {
        val response = MapResponse(listOf(CheckPoint(1, 1.5, 1.6)))
        return delegate.returningResponse(response).map()
    }

    override suspend fun event(): EventResponse {
        val response = EventResponse(listOf("屋台１", "屋台２"))
        return delegate.returningResponse(response).event()
    }

    override suspend fun schedule(limit: Int, offset: Int): ScheduleResponse {
        val response = ScheduleResponse(listOf(TimeEvent(1, mapOf("屋台" to "hello"))))
        return delegate.returningResponse(response).schedule(limit, offset)
    }
}
