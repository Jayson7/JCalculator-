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
import net.objecthunter.exp4j.ExpressionBuilder  // Library for evaluating expressions

// Corrected: listOf, not ListOf
val buttonList = listOf(
    "C", "(", ")", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "AC", "0", ".", "="
)

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    // Remember the input expression the user types
    var input by remember { mutableStateOf("") }

    // Remember the output result
    var result by remember { mutableStateOf("") }

    // The whole screen area
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Arrange everything vertically
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            // Display the current input (e.g., "123+456")
            Text(
                text = input,
                style = TextStyle(
                    fontSize = 30.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Display the result below the input (e.g., "579")
            Text(
                text = result,
                style = TextStyle(
                    fontSize = 60.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            // Space between result and buttons
            Spacer(modifier = Modifier.height(10.dp))

            // Display the buttons in a grid (4 columns)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Break the buttons into rows of 4
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
                                    .aspectRatio(1f) // Make buttons square
                                    .background(Color.LightGray)
                                    .clickable {
                                        // Handle button clicks
                                        when (label) {
                                            "C" -> {
                                                // Clear only the last character
                                                if (input.isNotEmpty()) {
                                                    input = input.dropLast(1)
                                                }
                                            }
                                            "AC" -> {
                                                // Clear everything
                                                input = ""
                                                result = ""
                                            }
                                            "=" -> {
                                                try {
                                                    // Evaluate the expression
                                                    val evaluated = ExpressionBuilder(input).build().evaluate()
                                                    result = evaluated.toString()
                                                } catch (e: Exception) {
                                                    // If invalid, show "Error"
                                                    result = "Error"
                                                }
                                            }
                                            else -> {
                                                // Otherwise add the clicked button to the input
                                                input += label
                                            }
                                        }
                                    }
                            ) {
                                // Display the button text
                                Text(
                                    text = label,
                                    style = TextStyle(fontSize = 24.sp),
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
