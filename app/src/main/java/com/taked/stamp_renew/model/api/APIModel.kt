package com.taked.stamp_renew.model.api

data class UserRequest (
    val name: String,
    val device: String,
    val version: String,
)
data class UserResponse (
    val uuid: String,
)

data class BeaconRequest(
    val quiz: Int,
    val beacon: Collection<Int>
)
data class BeaconResponse(
    val id: Int,
    val quiz: Int,
    val url: String,
)

data class ImageResponse(
    val url: String
)