package com.example.week3quizapp
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3quizapp.ui.theme.Week3QuizAppTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            Week3QuizAppTheme {
                Surface {
                    LayoutQuizApp()
                }
            }
        }
    }
}

val listQuizQuestions = listOf<String>(
    "Who create Android?",
    "Which was the first Android dessert name?",
    "What is the name of the Android app marketplace?",
    "What Google-branded phone did the company launch in 2013?",
    "What candy bar is Android 4.4 named after?",
    "End of the quiz!"
)

val listQuizAnswers = listOf<String>(
    "Andy Rubin",
    "Cupcake",
    "Google Play",
    "Nexus 5",
    "KitKat"
)

var stateNextQuestion = false
var counterChangeList = 0
var textResultSubmitButton: Triple<String, String, String> =
    Triple(
        "Correct! \uD83D\uDE01",
        "Try again! \uD83D\uDE35\u200D\uD83D\uDCAB",
        "Thank you for playing! \uD83C\uDFC1 \uD83C\uDF87"
    )

@Composable
fun LayoutQuizApp() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val textApp = "Let's play Android Quiz!"
        Text(
            textApp,
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Magenta
        )

        var textQuestion by rememberSaveable {
            mutableStateOf("")
        }
        textQuestion = listQuizQuestions[counterChangeList]
        Text(
            text = textQuestion,
            fontWeight = FontWeight.SemiBold,
        )

        var answerInput by rememberSaveable {
            mutableStateOf("")
        }
        TextField(
            value = answerInput,
            onValueChange = {
                answerInput = it
            },
            Modifier.padding(top = 8.dp),
            singleLine = true
        )

        val textButtonSubmit = "Submit"
        var textResult by remember {
            mutableStateOf("")
        }
        var textResultColor by rememberSaveable {
            mutableStateOf(value = android.R.color.holo_blue_dark)
        }

        Button(
            onClick = {
                val resultAnswer = checkAnswer(answerInput)
                textResult = resultAnswer.first
                textResultColor = changeColorTextResult(textResult)
                stateNextQuestion = resultAnswer.second
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = textButtonSubmit,
                fontSize = 20.sp
            )
        }

        Text(
            text = textResult,
            color = colorResource(id = textResultColor.toInt()),
            fontSize = 18.sp

        )

        val textButtonNext = "Next"
        Button(
            onClick = {
                val resultNextButton = goToNextQuestion()
                textQuestion = resultNextButton.first
                answerInput = ""
                textResult = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = textButtonNext,
                fontSize = 20.sp
            )
        }
    }
}

fun changeColorTextResult(result: String): Int {
    val color = if (result == textResultSubmitButton.first) {
        android.R.color.holo_blue_dark
    } else if (result == textResultSubmitButton.second) {
        android.R.color.holo_red_dark
    } else {
        android.R.color.holo_purple
    }
    return color
}

fun goToNextQuestion(): Pair<String, Int> {
    if (stateNextQuestion) {
        if (counterChangeList < listQuizQuestions.size - 1) {
            counterChangeList = counterChangeList + 1
            stateNextQuestion = false
        } else {
            stateNextQuestion = false
        }
    }
    return Pair(
        listQuizQuestions[counterChangeList],
        counterChangeList
    )
}

fun checkAnswer(userInput: String): Pair<String, Boolean> {
    var textResult = textResultSubmitButton.second
    var stateNextQuestion = false
    if (userInput.isNotBlank() && (counterChangeList < listQuizQuestions.size - 1)) {
        if (userInput.trimEnd().trimStart() == listQuizAnswers[counterChangeList] ||
            userInput.trimEnd().trimStart()
                .toLowerCase(Locale.ROOT) == listQuizAnswers[counterChangeList].toLowerCase(
                Locale.ROOT
            )
        ) {
            textResult = textResultSubmitButton.first
            stateNextQuestion = true
        } else {
            textResult = textResultSubmitButton.second
        }
    }
    if (listQuizQuestions[counterChangeList] == listQuizQuestions[listQuizQuestions.size - 1]) {
        textResult = textResultSubmitButton.third
        stateNextQuestion = false
    }
    return Pair(
        textResult, stateNextQuestion
    )
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    Week3QuizAppTheme {
        Surface {
            LayoutQuizApp()
        }
    }
}