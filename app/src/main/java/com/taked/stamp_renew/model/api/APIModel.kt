package com.taked.stamp_renew.model.api

data class UserRequest (
    val name: String,
    val device: String,
    val version: String,
)

data class UserResponse (
    val uuid: String,
)

data class ImageRequest(
    val quiz: Int,
    val beacon: Collection<Int>
)

data class ImageResponse(
    val id: Int,
    val quiz: Int,
    val url: String,
)