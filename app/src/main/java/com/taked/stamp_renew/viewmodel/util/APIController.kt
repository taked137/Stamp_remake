package com.taked.stamp_renew.viewmodel.util

import android.util.Log
import com.taked.stamp_renew.model.api.*

class APIController {
    companion object {
        suspend fun registerUser(userName: String, device: String, version: String): UserResponse? {
            val request = UserRequest(userName, device, version)

            return try {
                APIClient.instance.register(request)
            } catch (e: Exception) {
                null
            }
        }

        suspend fun judgeBeacon(uuid: String, quizNum: Int, beacons: List<Int>): BeaconResponse? {
            val request = BeaconRequest(quizNum, beacons)

            return try {
                APIClient.instance.beacon(uuid, request)
            } catch (e: Exception) {
                null
            }
        }

        suspend fun requestQuiz(uuid: String, quizNum: Int): ImageResponse? =
            try {
                APIClient.instance.image(uuid, quizNum + 1)
            } catch (e: Exception) {
                null
            }

        suspend fun judgeAnswer(uuid: String, quizNum: Int, answer: String): AnswerResponse? {
            val request = AnswerRequest(quizNum + 1, answer)

            return try {
                APIClient.instance.judge(uuid, request)
            } catch (e: Exception) {
                null
            }
        }

    }
}