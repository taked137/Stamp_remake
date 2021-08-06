package com.taked.stamp_renew.model.api

data class ImageRequest(
    val quiz: Int,
    val beacon: Collection<Int>
)

data class ImageResponse(
    val id: Int,
    val quiz: Int,
    val url: String,
)