package com.cobeastx.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import kotlin.math.*

val buttonList = listOf(
    "sin", "cos", "tan", "√",
    "C", "(", ")", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "-",
    "1", "2", "3", "+",
    "AC", "0", ".", "="
)

// Colors
val backgroundColor = Color(0xFF000000)     // Black
val displayTextColor = Color(0xFFFFFFFF)     // White
val numberKeyColor = Color(0xFF333333)       // Dark gray
val operatorKeyColor = Color(0xFFFF9500)     // Orange
val functionKeyColor = Color(0xFFAA0000)     // Light gray
val equalKeyColor = Color(0xFFFF9500)        // Orange

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            // Display input
            Text(
                text = input,
                style = TextStyle(
                    fontSize = 30.sp,
                    textAlign = TextAlign.End,
                    color = displayTextColor
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Display result
            Text(
                text = result,
                style = TextStyle(
                    fontSize = 60.sp,
                    textAlign = TextAlign.End,
                    color = displayTextColor
                ),
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the bottom

            // Buttons grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                buttonList.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { label ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        when {
                                            label == "=" -> equalKeyColor
                                            label in listOf("/", "*", "-", "+", "(", ")", "√") -> operatorKeyColor
                                            label in listOf("C", "AC", "sin", "cos", "tan") -> functionKeyColor
                                            else -> numberKeyColor
                                        }
                                    )
                                    .clickable {
                                        when (label) {
                                            "C" -> {
                                                if (input.isNotEmpty()) {
                                                    input = input.dropLast(1)
                                                }
                                            }
                                            "AC" -> {
                                                input = ""
                                                result = ""
                                            }
                                            "=" -> {
                                                try {
                                                    val expr = input
                                                        .replace("√", "sqrt")
                                                        .replace("%", "*0.01")

                                                    val sqrtFunction = object : Function("sqrt", 1) {
                                                        override fun apply(vararg args: Double): Double {
                                                            return sqrt(args[0])
                                                        }
                                                    }

                                                    val sinFunction = object : Function("sin", 1) {
                                                        override fun apply(vararg args: Double): Double {
                                                            return sin(Math.toRadians(args[0]))
                                                        }
                                                    }

                                                    val cosFunction = object : Function("cos", 1) {
                                                        override fun apply(vararg args: Double): Double {
                                                            return cos(Math.toRadians(args[0]))
                                                        }
                                                    }

                                                    val tanFunction = object : Function("tan", 1) {
                                                        override fun apply(vararg args: Double): Double {
                                                            return tan(Math.toRadians(args[0]))
                                                        }
                                                    }

                                                    val evaluated = ExpressionBuilder(expr)
                                                        .function(sqrtFunction)
                                                        .function(sinFunction)
                                                        .function(cosFunction)
                                                        .function(tanFunction)
                                                        .build()
                                                        .evaluate()

                                                    result = evaluated.toString()
                                                } catch (e: Exception) {
                                                    result = "Error"
                                                }
                                            }
                                            else -> {
                                                input += label
                                            }
                                        }
                                    }
                            ) {
                                Text(
                                    text = label,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        color = displayTextColor
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
