package com.taked.stamp_renew.model.api

import android.util.Log

class APIController {
    companion object {
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