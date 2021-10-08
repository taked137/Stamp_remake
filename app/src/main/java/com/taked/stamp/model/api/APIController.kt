package com.taked.stamp.model.api

object APIController {
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

    suspend fun getInformation(limit: Int, offset: Int): InfoResponse? =
        try {
            APIClient.instance.info(limit = limit, offset = offset)
        } catch (_: Exception) {
            null
        }

    suspend fun getCheckPoint(): MapResponse? =
        try {
            APIClient.instance.map()
        } catch(e: Exception) {
            null
        }
}
