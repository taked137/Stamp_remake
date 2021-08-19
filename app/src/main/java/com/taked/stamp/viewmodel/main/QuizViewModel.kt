package com.taked.stamp.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel(private val quizID: Int) : ViewModel() {
    companion object {
        const val TEXT_TITLE = "謎解き"
        const val TEXT_BUTTON = "解答送信"
        const val TEXT_INPUT = "解答を入力してください"
    }

    val titleText: String
        get() = TEXT_TITLE + (quizID + 1)

    private val _isButtonEnabled: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().also { mutableLiveData ->
            mutableLiveData.value = false
        }
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    fun updateButton(isBlank: Boolean) {
        _isButtonEnabled.value = !isBlank
    }
}
