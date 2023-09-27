package com.bignerdranch.android.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt


const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val CURRENT_TESTPOINTS_KEY = "CURRENT_TESTPOINTS_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CURRENT_TIPS_KEY = "CURRENT_TIPS_KEY"


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var testPoints
        get() = savedStateHandle.get(CURRENT_TESTPOINTS_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_TESTPOINTS_KEY, value)

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)


    private var currentIndex
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    private var avalaibleTips
        get() = savedStateHandle.get(CURRENT_TIPS_KEY) ?: 3
        set(value) = savedStateHandle.set(CURRENT_TIPS_KEY, value)


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun isLastQuestion(): Boolean{
    return (currentIndex + 1) == questionBank.size
    }

    fun testResult(): Double{
    return (((testPoints.toDouble() / questionBank.size) * 100)
    *100).roundToInt() / 100.0
    }

    fun addPoint(){
        ++testPoints
    }

    fun useTips(){
        --avalaibleTips
    }

    fun havingTips():Boolean{
        return avalaibleTips>0
    }

    fun getTips():Int{
        return avalaibleTips
    }

}

