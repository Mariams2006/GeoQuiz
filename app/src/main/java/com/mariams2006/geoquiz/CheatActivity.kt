package com.mariams2006.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheatActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView
    private lateinit var backButton: Button

    private var answerIsTrue = false
    private var answerWasShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        questionTextView =
            findViewById(R.id.cheatQuestionTextView)

        showAnswerButton =
            findViewById(R.id.showAnswerButton)

        answerTextView =
            findViewById(R.id.answerTextView)

        backButton =
            findViewById(R.id.backButton)

        val question =
            intent.getStringExtra(EXTRA_QUESTION)
                ?: "Question unavailable"

        answerIsTrue =
            intent.getBooleanExtra(
                EXTRA_ANSWER_IS_TRUE,
                false
            )

        questionTextView.text = question

        showAnswerButton.setOnClickListener {

            if (answerIsTrue) {
                answerTextView.text = "Answer: TRUE"
            } else {
                answerTextView.text = "Answer: FALSE"
            }

            answerWasShown = true

            setAnswerShownResult()
        }

        backButton.setOnClickListener {

            setAnswerShownResult()

            finish()
        }
    }

    private fun setAnswerShownResult() {

        val data = Intent().apply {

            putExtra(
                EXTRA_ANSWER_SHOWN,
                answerWasShown
            )
        }

        setResult(
            Activity.RESULT_OK,
            data
        )
    }

    companion object {

        const val EXTRA_QUESTION =
            "com.mariams2006.geoquiz.question"

        const val EXTRA_ANSWER_IS_TRUE =
            "com.mariams2006.geoquiz.answer_is_true"

        const val EXTRA_ANSWER_SHOWN =
            "com.mariams2006.geoquiz.answer_shown"
    }
}