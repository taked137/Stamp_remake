package com.taked.stamp.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StampViewModel(
    private val judgeArray: MutableList<Boolean>, private val clearArray: MutableList<Boolean>
) : ViewModel() {
    companion object {
        const val TEXT_TITLE = "スタンプラリー"
        const val TEXT_BUTTON = "謎解き"
        const val TEXT_INVALID = "問題未取得"
        const val TEXT_VALID = "問題取得済み"
    }

    val titleText = TEXT_TITLE
    val buttonText = TEXT_BUTTON

    private val _judgeInfo = MutableLiveData<List<Boolean>>().also {
        it.value = judgeArray
    }
    val judgeInfo: LiveData<List<Boolean>>
        get() = _judgeInfo

    fun updateJudgeInfo(index: Int) {
        judgeArray[index] = true
        _judgeInfo.value = judgeArray
    }

    private val _clearInfo = MutableLiveData<List<Boolean>>().also {
        it.value = clearArray
    }
    val clearInfo: LiveData<List<Boolean>>
        get() = _clearInfo

    fun updateClearInfo(index: Int) {
        clearArray[index] = true
        _clearInfo.value = clearArray
    }

    fun judgeCleared(): Boolean = _clearInfo.value!!.all { it }

    val imageLiveData = MutableLiveData<Int>()
    val quizLiveData = MutableLiveData<Int>()
    fun onQuizClick(index: Int) {
        if (judgeArray[index]) {
            quizLiveData.value = index
        } else {
            imageLiveData.value = index
        }
    }

    val goalLiveData = MutableLiveData<Boolean>()
    fun onGoalClick() {
        goalLiveData.value = true
    }
}
