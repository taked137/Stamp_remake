package com.taked.stamp.viewmodel.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TitleViewModel : ViewModel() {
    companion object {
        const val TEXT_LICENSE = "ライセンス"
        const val TEXT_TITLE = "名工大\nスタンプラリー"
        const val TEXT_BUTTON = "tap to start"
    }

    val titleText: LiveData<String>
        get() = MutableLiveData(TEXT_TITLE)
    val licenseText: LiveData<String>
        get() = MutableLiveData(TEXT_LICENSE)


//    private val _isTextEnabled: MutableLiveData<Boolean> =
//        MutableLiveData<Boolean>().also { mutableLiveData ->
//            mutableLiveData.value = true
//        }
//    val isTextEnabled: LiveData<Boolean>
//        get() = _isTextEnabled
//
//    fun updateText(isInvalid: Boolean) {
//        _isTextEnabled.value = isInvalid
//    }

    val buttonText: LiveData<String>
        get() = MutableLiveData(TEXT_BUTTON)

    private val _isButtonClicked: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().also { mutableLiveData ->
            mutableLiveData.value = false
        }
    val isButtonClicked: LiveData<Boolean>
        get() = _isButtonClicked
}
