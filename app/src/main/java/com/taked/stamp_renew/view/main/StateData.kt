package com.taked.stamp_renew.view.main

import com.squareup.moshi.Json

data class StateData(
    var hasCleared: Collection<Boolean>,
)

enum class ActivityState (val value: Int) {
    REGISTER(1),
    GAME(2),
    CLEAR(3),
}