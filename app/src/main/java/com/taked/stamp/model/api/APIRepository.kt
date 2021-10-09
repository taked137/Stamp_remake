package com.taked.stamp.model.api

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class APIRepository @Inject constructor(private val apiClient: APIClient) {

    suspend fun registerUser(userName: String, device: String, version: String): UserResponse? {
        val request = UserRequest(userName, device, version)

        return try {
            apiClient.instance.register(request)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun judgeBeacon(uuid: String, quizNum: Int, beacons: List<Int>): BeaconResponse? {
        val request = BeaconRequest(quizNum, beacons)

        return try {
            apiClient.instance.beacon(uuid, request)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun requestQuiz(uuid: String, quizNum: Int): ImageResponse? =
        try {
            apiClient.instance.image(uuid, quizNum + 1)
        } catch (_: Exception) {
            null
        }

    suspend fun judgeAnswer(uuid: String, quizNum: Int, answer: String): AnswerResponse? {
        val request = AnswerRequest(quizNum + 1, answer)

        return try {
            apiClient.instance.judge(uuid, request)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun postGoal(uuid: String): GoalResponse? =
        try {
            apiClient.instance.goal(uuid)
        } catch (_: Exception) {
            null
        }

    suspend fun getInformation(limit: Int, offset: Int): InfoResponse? =
        try {
            apiClient.instance.info(limit = limit, offset = offset)
        } catch (_: Exception) {
            null
        }

    suspend fun getCheckPoint(): MapResponse? =
        try {
            apiClient.instance.map()
        } catch(_: Exception) {
            null
        }
}