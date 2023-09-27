package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.useTips()
            Toast.makeText(this,getString(R.string.tipsToast,quizViewModel.getTips()),
                Toast.LENGTH_LONG).show()
            if(!quizViewModel.havingTips()) binding.cheatButton.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            if(quizViewModel.isLastQuestion()){
                binding.nextButton.visibility = View.INVISIBLE
                Toast.makeText(this,quizViewModel.testResult().toString()
                    +"%",Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            quizViewModel.moveToNext()
            updateQuestion()
            binding.trueButton.visibility = View.VISIBLE
            binding.falseButton.visibility = View.VISIBLE
            quizViewModel.isCheater = false

        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)


        }


        updateQuestion()

    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {

        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> {
                quizViewModel.addPoint()
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                quizViewModel.addPoint()
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

    }


}
