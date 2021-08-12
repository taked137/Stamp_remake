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

        suspend fun judgeBeacon(quizNum: Int, beacons: List<Int>): BeaconResponse? {
            val request = BeaconRequest(quizNum, beacons)

            return try {
                APIClient.instance.beacon(request)
            } catch (e: Exception) {
                null
            }
        }

        suspend fun requestQuiz(quizNum: Int): ImageResponse? =
            try {
                APIClient.instance.image(quizNum)
            } catch (e: Exception) {
                null
            }

    }
}