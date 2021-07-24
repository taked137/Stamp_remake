package com.taked.stamp_renew.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.jvm.internal.Intrinsics

class ApiModel {
    companion object {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}