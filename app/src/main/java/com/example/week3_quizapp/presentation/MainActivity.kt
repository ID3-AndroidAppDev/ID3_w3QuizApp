/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.week3_quizapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.week3_quizapp.R
import com.example.week3_quizapp.presentation.theme.Week3_QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

val quizQuestions = listOf(
    QuizQuestion(
        question = "Who create Android?",
        correctAnswer = "Andy Rubin" // No options = open-ended
    ),
    QuizQuestion(
        question = "Which was the first Android dessert name?",
        correctAnswer = "Cupcake" // No options = open-ended
    ),
    QuizQuestion(
        question = "What candy bar is Android 4.4 named after?",
        correctAnswer = "KitKat" // No options = open-ended
    ),
    QuizQuestion(
        question = "Which component manages UI in Android?",
        options = listOf("Service", "BroadcastReceiver", "Activity", "ContentProvider"),
        correctAnswer = "Activity"
    ),
    QuizQuestion(
        question = "Method to start an Activity?",
        options = listOf("startActivity()", "beginActivity()", "launchActivity()", "openActivity()"),
        correctAnswer = "startActivity()"
    ),

    QuizQuestion(
        question = "Which layout arranges elements vertically or horizontally?",
        options = listOf("RelativeLayout", "ConstraintLayout", "LinearLayout", "GridLayout"),
        correctAnswer = "LinearLayout"
    ),
    QuizQuestion(
        question = "Default thread for background tasks?",
        options = listOf("Main thread", "UI thread", "Worker thread", "AsyncTask thread"),
        correctAnswer = "Main thread"
    ),
    QuizQuestion(
        question = "Which component handles data persistence?",
        options = listOf("Room", "RecyclerView", "LiveData", "ViewModel"),
        correctAnswer = "Room"
    ),
    QuizQuestion(
        question = "Purpose of ViewModel?",
        options = listOf("To hold UI-related data", "To handle background tasks", "To store user preferences", "To manage database transactions"),
        correctAnswer = "To hold UI-related data"
    ),
    QuizQuestion(
        question = "What does Gradle do?",
        options = listOf("Code editor", "Build automation tool", "Database management", "UI testing framework"),
        correctAnswer = "Build automation tool"
    ),
    QuizQuestion(
        question = "Which API handles non-blocking background tasks?",
        options = listOf("AsyncTask", "Handler", "IntentService", "Service"),
        correctAnswer = "AsyncTask"
    ),
    QuizQuestion(
        question = "Which component shows a scrollable list?",
        options = listOf("TextView", "ListView", "ImageView", "Button"),
        correctAnswer = "ListView"
    ),
    QuizQuestion(
        question = "Function of AndroidManifest.xml?",
        options = listOf("Database schema", "Permissions & components", "User settings", "Network configs"),
        correctAnswer = "Permissions & components"
    ),
    QuizQuestion(
        question = "Which runs in the background when app is not active?",
        options = listOf("Service", "Activity", "BroadcastReceiver", "ContentProvider"),
        correctAnswer = "Service"
    ),
    QuizQuestion(
        question = "Dependency manager in Android?",
        options = listOf("Maven", "Gradle", "Ant", "Ivy"),
        correctAnswer = "Gradle"
    ),
    QuizQuestion(
        question = "Compose: Create a button?",
        options = listOf("Button()", "Clickable()", "TextButton()", "SubmitButton()"),
        correctAnswer = "Button()"
    ),
    QuizQuestion(
        question = "Manage app state in Compose?",
        options = listOf("LiveData", "StateFlow", "SharedPreferences", "Intent"),
        correctAnswer = "StateFlow"
    ),
    QuizQuestion(
        question = "Function to show text in Compose?",
        options = listOf("TextView()", "TextField()", "Text()", "Label()"),
        correctAnswer = "Text()"
    )
)

@Composable
fun QuizModeSelection(onModeSelected: (QuizMode) -> Unit) {
    val sakuraPink = Color(0xFFFFC1CC) // A soft Sakura pink

    val navChipColors = ChipDefaults.chipColors(
        //backgroundColor = MaterialTheme.colors.secondary,
        backgroundColor = sakuraPink,
        contentColor = MaterialTheme.colors.onSecondary
    )

    ScalingLazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Select Quiz Type",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Chip(
                label = { Text("Multiple Choice") },
                onClick = { onModeSelected(QuizMode.MULTIPLE_CHOICE) },
                colors = navChipColors
            )
        }

        item {
            Chip(
                label = { Text("Open-Ended") },
                onClick = { onModeSelected(QuizMode.OPEN_ENDED) },
                colors = navChipColors
            )
        }
    }
}

@Composable
fun MultipleChoiceQuiz(questions: List<QuizQuestion>, onHome: () -> Unit) {
    val answerChipColors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )

    val navChipColors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary
    )

    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var answered by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    val question = questions[currentIndex]

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = if (showResult) "Score: $score / ${questions.size}"
                else "Q${currentIndex + 1}: ${question.question}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (!showResult) {
            question.options?.forEachIndexed { index, option ->
                val labelPrefix = ('A' + index) + ". "
                item {
                    Chip(
                        label = { Text(labelPrefix + option) },
                        onClick = {
                            if (!answered) {
                                isAnswerCorrect = option.equals(question.correctAnswer, ignoreCase = true)
                                if (isAnswerCorrect == true) score++
                                answered = true
                            }
                        },
                        enabled = !answered,
                        modifier = Modifier.padding(vertical = 2.dp),
                        colors = answerChipColors
                    )
                }
            }

            // Feedback after answering
            if (answered) {
                item {
                    val feedbackText = if (isAnswerCorrect == true) {
                        "✅ Correct!"
                    } else {
                        "❌ Incorrect!\nAnswer: ${question.correctAnswer}"
                    }

                    val feedbackColor = if (isAnswerCorrect == true) Color(0xFF4CAF50) else Color(0xFFE57373)

                    Text(
                        text = feedbackText,
                        color = feedbackColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }

            item {
                Chip(
                    label = { Text("Next") },
                    onClick = {
                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                            answered = false
                            isAnswerCorrect = null
                        } else {
                            showResult = true
                        }
                    },
                    enabled = answered,
                    modifier = Modifier.padding(top = 6.dp),
                    colors = navChipColors
                )
            }
        }

        item {
            Chip(
                label = { Text("Home") },
                onClick = onHome,
                modifier = Modifier.padding(top = 4.dp),
                colors = navChipColors
            )
        }
    }
}

@Composable
fun OpenEndedQuiz(questions: List<QuizQuestion>, onHome: () -> Unit) {
    val answerChipColors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )

    val navChipColors = ChipDefaults.chipColors(
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary
    )

    var currentIndex by remember { mutableStateOf(0) }
    var answer by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    val question = questions[currentIndex]

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = if (showResult) "Quiz Complete!\nScore: $score / ${questions.size}"
                else "Q${currentIndex + 1}: ${question.question}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        if (!showResult) {
            item {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 6.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.body1
                )
            }

            item {
                Chip(
                    label = { Text("Submit") },
                    onClick = {
                        if (!submitted && answer.isNotBlank()) {
                            isAnswerCorrect = answer.equals(question.correctAnswer, ignoreCase = true)
                            if (isAnswerCorrect == true) {
                                score++
                            }
                            submitted = true
                        }
                    },
                    enabled = !submitted,
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = answerChipColors
                )
            }

            // Feedback after submission
            if (submitted) {
                item {
                    val feedbackText = if (isAnswerCorrect == true) {
                        "✅ Correct!"
                    } else {
                        "❌ Incorrect!\nAnswer: ${question.correctAnswer}"
                    }

                    val feedbackColor = if (isAnswerCorrect == true) Color(0xFF4CAF50) else Color(0xFFE57373)

                    Text(
                        text = feedbackText,
                        color = feedbackColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                }
            }

            item {
                Chip(
                    label = { Text("Next") },
                    onClick = {
                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                            answer = ""
                            submitted = false
                            isAnswerCorrect = null
                        } else {
                            showResult = true
                        }
                    },
                    enabled = submitted,
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = navChipColors
                )
            }
        }

        item {
            Chip(
                label = { Text("Home") },
                onClick = onHome,
                modifier = Modifier.padding(top = 8.dp),
                colors = navChipColors
            )
        }
    }
}



@Composable
fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun WearApp() {
    var quizMode by remember { mutableStateOf<QuizMode?>(null) }

    Week3_QuizAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()

            when (quizMode) {
                null -> QuizModeSelection(onModeSelected = { quizMode = it })

                QuizMode.MULTIPLE_CHOICE -> MultipleChoiceQuiz(
                    questions = quizQuestions.filter { it.options != null },
                    onHome = { quizMode = null }
                )

                QuizMode.OPEN_ENDED -> OpenEndedQuiz(
                    questions = quizQuestions.filter { it.options == null },
                    onHome = { quizMode = null }
                )
            }
        }
    }
}

enum class QuizMode {
    MULTIPLE_CHOICE,
    OPEN_ENDED
}

data class QuizQuestion(
    val question: String,
    val options: List<String>? = null, // Null means it's open-ended
    val correctAnswer: String
)

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}