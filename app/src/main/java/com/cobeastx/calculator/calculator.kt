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

val buttonList = listOf(
    "C", "(", ")", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "AC", "0", ".", "="
)

// Define your custom colors
val backgroundColor = Color(0xFF000000)
val displayTextColor = Color(0xFFFFFFFF)
val numberKeyColor = Color(0xFF333333)
val operatorKeyColor = Color(0xFFFF9500)
val functionKeyColor = Color(0xFFA10101)
val equalKeyColor = Color(0xFFFF9500)

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
            // Input text
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

            // Result text
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

            Spacer(modifier = Modifier.height(10.dp))

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
                                            label in listOf("/", "*", "-", "+", "(", ")") -> operatorKeyColor
                                            label in listOf("C", "AC") -> functionKeyColor
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
                                                    val evaluated = ExpressionBuilder(input).build().evaluate()
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
