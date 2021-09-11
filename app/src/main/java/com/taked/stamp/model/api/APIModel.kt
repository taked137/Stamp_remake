package com.taked.stamp.model.api

data class UserRequest(
    val name: String,
    val device: String,
    val version: String,
)

data class UserResponse(
    val uuid: String,
)

data class BeaconRequest(
    val quiz: Int,
    val beacon: Collection<Int>,
)

data class BeaconResponse(
    val id: Int,
    val quiz: Int,
    val url: String,
)

data class ImageResponse(
    val url: String,
)

data class AnswerRequest(
    val quiz: Int,
    val answer: String,
)

data class AnswerResponse(
    val quiz: Int,
    val correct: Boolean,
)

data class GoalResponse(
    val accept: Boolean,
)

data class Message(
    val message: String
)

data class TestResponse(
    val result: List<Message>
)
