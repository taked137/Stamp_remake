package com.taked.stamp_renew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RegisterViewModel {
    companion object {
        const val TEXT_TITLE = "ユーザ登録"
        const val TEXT_INPUT = "ユーザ名を入力してください"
        const val TEXT_WARNING = "4文字以上で入力してください"
        const val TEXT_BUTTON = "登録する"
    }

    val titleText: LiveData<String>
        get() = MutableLiveData(TEXT_TITLE)
    val inputText: LiveData<String>
        get() = MutableLiveData(TEXT_INPUT)
    val warningText: LiveData<String>
        get() = MutableLiveData(TEXT_WARNING)


    private val _isTextEnabled: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().also { mutableLiveData ->
            mutableLiveData.value = true
        }
    val isTextEnabled: LiveData<Boolean>
        get() = _isTextEnabled

    fun updateText(isInvalid: Boolean) {
        _isTextEnabled.value = isInvalid
    }


    private val _isButtonEnabled: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().also { mutableLiveData ->
            mutableLiveData.value = false
        }
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    val buttonText: LiveData<String>
        get() = MutableLiveData(TEXT_BUTTON)

    fun updateButton(isBlank: Boolean) {
        _isButtonEnabled.value = !isBlank
    }
}