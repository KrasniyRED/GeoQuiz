package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentIndex = 0
    private var testPoints = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index",currentIndex)
        outState.putInt("testPoints",testPoints)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            currentIndex  = savedInstanceState.getInt("index")
            testPoints  = savedInstanceState.getInt("testPoints")
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            binding.trueButton.visibility = View.INVISIBLE
            binding.falseButton.visibility = View.INVISIBLE
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            binding.trueButton.visibility = View.INVISIBLE
            binding.falseButton.visibility = View.INVISIBLE
        }

        binding.nextButton.setOnClickListener {
            if(isLastQuestion()){
                binding.nextButton.visibility = View.INVISIBLE
                Toast.makeText(this,testResult(testPoints).toString()
                    +"%",Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            binding.trueButton.visibility = View.VISIBLE
            binding.falseButton.visibility = View.VISIBLE
        }

        updateQuestion()

    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            testPoints+=1
            R.string.correct_toast

        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

    }
    private fun isLastQuestion(): Boolean{
        return (currentIndex + 1) == questionBank.size
    }
    private  fun testResult(points : Int): Double{
        return (((points.toDouble() / questionBank.size) * 100)
        *100).roundToInt() / 100.0
    }

}
