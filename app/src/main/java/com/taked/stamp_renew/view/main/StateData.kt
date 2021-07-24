package com.taked.stamp_renew.view.main

import com.squareup.moshi.Json

data class StateData(
    var hasCleared: Collection<Boolean>,
)

enum class StateKeys (val key: String) {
    PROGRESS("progress"),
    POSITION("position"),
    QUIZ("quiz"),
}