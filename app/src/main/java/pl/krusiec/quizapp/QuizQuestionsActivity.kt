package pl.krusiec.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Question>? = null
    private var selectedOptionPosition: Int = 0
    private var correctAnswers: Int = 0
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        userName = intent.getStringExtra(Constants.USER_NAME)
        questionsList = Constants.getQuestions()
        setQuestion()

        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    private fun setQuestion() {
        val question = questionsList!![currentPosition - 1]

        defaultOptionsView()

        if (currentPosition == questionsList!!.size) {
            btnSubmit.text = "FINISH"
        } else {
            btnSubmit.text = "SUBMIT"
        }

        progressBar.progress = currentPosition
        tvProgress.text = "$currentPosition/${progressBar.max}"
        tvQuestion.text = question.question
        ivImage.setImageResource(question.image)
        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvOptionOne -> selectedOptionView(tvOptionOne, 1)
            R.id.tvOptionTwo -> selectedOptionView(tvOptionTwo, 2)
            R.id.tvOptionThree -> selectedOptionView(tvOptionThree, 3)
            R.id.tvOptionFour -> selectedOptionView(tvOptionFour, 4)
            R.id.btnSubmit -> {
                if (selectedOptionPosition == 0) {
                    currentPosition++

                    when {
                        currentPosition <= questionsList!!.size -> setQuestion()
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, userName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = questionsList?.get(currentPosition - 1)
                    if (question!!.correctAnswer != selectedOptionPosition) {
                        answerView(selectedOptionPosition, R.drawable.wrong_option_border_bg)
                    } else {
                        correctAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if (currentPosition == questionsList!!.size) {
                        btnSubmit.text = "FINISH"
                    } else {
                        btnSubmit.text = "GO TO NEXT QUESTION"
                    }
                    selectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)
            2 -> tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)
            3 -> tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)
            4 -> tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNumber

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }
}
