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
    val id: Int,
    val message: String
)

data class InfoTitleResponse(
    val result: List<Message>
)

data class InfoContentResponse(
    val id: Int,
    val title: String,
    val category: String,
    val message: String
)

data class CheckPoint(
    val num: Int,
    val latitude: Double,
    val longitude: Double
)
data class MapResponse(
    val checkpoint: List<CheckPoint>
)

data class EventResponse(
    val events: List<String>
)

data class TimeEvent(
    val time: Int,
    val events: Map<String, String>
)
data class ScheduleResponse(
    val result: List<TimeEvent>
)
