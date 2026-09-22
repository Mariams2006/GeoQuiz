package com.mariams2006.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var progressTextView: TextView
    private lateinit var scoreTextView: TextView

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button

    private val questions = arrayOf(
        "Canberra is the capital of Australia.",
        "The Pacific Ocean is larger than the Atlantic Ocean.",
        "Mount Kilimanjaro is located in Kenya.",
        "Brazil is the largest country in South America.",
        "The Nile River flows through Egypt."
    )

    private val answers = arrayOf(
        true,
        true,
        false,
        true,
        true
    )

    private var currentQuestion = 0
    private var score = 0
    private var answered = false
    private var cheated = false

    private val cheatActivityLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                cheated =
                    result.data?.getBooleanExtra(
                        CheatActivity.EXTRA_ANSWER_SHOWN,
                        false
                    ) ?: false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        progressTextView = findViewById(R.id.progressTextView)
        scoreTextView = findViewById(R.id.scoreTextView)

        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButton)
        nextButton = findViewById(R.id.nextButton)
        cheatButton = findViewById(R.id.cheatButton)

        if (savedInstanceState != null) {
            currentQuestion =
                savedInstanceState.getInt("currentQuestion", 0)

            score =
                savedInstanceState.getInt("score", 0)

            answered =
                savedInstanceState.getBoolean("answered", false)

            cheated =
                savedInstanceState.getBoolean("cheated", false)
        }

        updateQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {

            if (currentQuestion < questions.size - 1) {

                currentQuestion++
                answered = false
                cheated = false

                updateQuestion()

            } else {

                Toast.makeText(
                    this,
                    "Quiz complete! Your score is $score out of ${questions.size}",
                    Toast.LENGTH_LONG
                ).show()

                scoreTextView.text =
                    "Final Score: $score / ${questions.size}"
            }
        }

        cheatButton.setOnClickListener {

            val intent = Intent(
                this,
                CheatActivity::class.java
            ).apply {

                putExtra(
                    CheatActivity.EXTRA_QUESTION,
                    questions[currentQuestion]
                )

                putExtra(
                    CheatActivity.EXTRA_ANSWER_IS_TRUE,
                    answers[currentQuestion]
                )
            }

            cheatActivityLauncher.launch(intent)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {

        if (answered) {

            Toast.makeText(
                this,
                "You already answered this question.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val correctAnswer = answers[currentQuestion]

        if (cheated) {

            Toast.makeText(
                this,
                "Cheating is wrong!",
                Toast.LENGTH_SHORT
            ).show()

        } else if (userAnswer == correctAnswer) {

            score++

            Toast.makeText(
                this,
                "Correct!",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                this,
                "Incorrect!",
                Toast.LENGTH_SHORT
            ).show()
        }

        answered = true
        scoreTextView.text = "Score: $score"
    }

    private fun updateQuestion() {

        questionTextView.text =
            questions[currentQuestion]

        progressTextView.text =
            "Question ${currentQuestion + 1} of ${questions.size}"

        scoreTextView.text =
            "Score: $score"

        if (currentQuestion == questions.size - 1) {
            nextButton.text = "FINISH"
        } else {
            nextButton.text = "NEXT"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(
            "currentQuestion",
            currentQuestion
        )

        outState.putInt(
            "score",
            score
        )

        outState.putBoolean(
            "answered",
            answered
        )

        outState.putBoolean(
            "cheated",
            cheated
        )
    }
}