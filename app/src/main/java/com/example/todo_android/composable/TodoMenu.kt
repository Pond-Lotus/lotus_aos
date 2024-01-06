package com.example.todo_android.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodoMenu(
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    onButtonClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .width(155.dp)
            .height(110.dp)
            .padding(bottom = 10.dp)
            .shadow(
                shape = RoundedCornerShape(20.dp),
                elevation = 5.dp,
                spotColor = Color(0xff9E9E9E),
                ambientColor = Color(0xffACACAC)
            )
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffFFB4B4))
                        .clickable {
                            onButtonClick("1")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffFFDCA8))
                        .clickable {
                            onButtonClick("2")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffB1E0CF))
                        .clickable {
                            onButtonClick("3")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 7.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffB7D7F5))
                        .clickable {
                            onButtonClick("4")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffFFB8EB))
                        .clickable {
                            onButtonClick("5")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )

                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffB6B1EC))
                        .clickable {
                            onButtonClick("6")
                            onMultiFloatingStateChange(FloatingStateType.Collapsed)
                        }
                )
            }
        }
    }
}