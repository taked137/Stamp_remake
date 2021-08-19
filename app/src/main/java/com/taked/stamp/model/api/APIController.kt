package com.taked.stamp.model.api

import com.taked.stamp.model.api.APIClient
import com.taked.stamp.model.api.BeaconRequest
import com.taked.stamp.model.api.UserRequest
import com.taked.stamp.model.api.UserResponse
import com.taked.stamp.model.api.BeaconResponse
import com.taked.stamp.model.api.AnswerResponse
import com.taked.stamp.model.api.AnswerRequest
import com.taked.stamp.model.api.GoalResponse
import com.taked.stamp.model.api.ImageResponse

sealed class APIController {
    companion object {
        suspend fun registerUser(userName: String, device: String, version: String): UserResponse? {
            val request = UserRequest(userName, device, version)

            return try {
                APIClient.instance.register(request)
            } catch (_: Exception) {
                null
            }
        }

        suspend fun judgeBeacon(uuid: String, quizNum: Int, beacons: List<Int>): BeaconResponse? {
            val request = BeaconRequest(quizNum, beacons)

            return try {
                APIClient.instance.beacon(uuid, request)
            } catch (_: Exception) {
                null
            }
        }

        suspend fun requestQuiz(uuid: String, quizNum: Int): ImageResponse? =
            try {
                APIClient.instance.image(uuid, quizNum + 1)
            } catch (_: Exception) {
                null
            }

        suspend fun judgeAnswer(uuid: String, quizNum: Int, answer: String): AnswerResponse? {
            val request = AnswerRequest(quizNum + 1, answer)

            return try {
                APIClient.instance.judge(uuid, request)
            } catch (_: Exception) {
                null
            }
        }

        suspend fun postGoal(uuid: String): GoalResponse? =
            try {
                APIClient.instance.goal(uuid)
            } catch (_: Exception) {
                null
            }
    }
}
