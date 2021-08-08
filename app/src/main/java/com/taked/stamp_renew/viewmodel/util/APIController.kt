package com.taked.stamp_renew.viewmodel.util

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

        suspend fun requestQuiz(quizNum: Int, beacons: List<Int>): ImageResponse? {
            val request = ImageRequest(quizNum, beacons)

            return try {
                APIClient.instance.image(request)
            } catch (e: Exception) {
                null
            }
        }
    }
}