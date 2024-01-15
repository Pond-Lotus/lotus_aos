package com.example.todo_android.composable

import android.graphics.BlurMaskFilter
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
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
                color = Color(0x33000000).copy(alpha = 0.1f),
                blurRadius = 15.dp
            )
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.White)
//            .shadow(
//                shape = RoundedCornerShape(20.dp),
//                elevation = 5.dp,
//                spotColor = Color(0xff9E9E9E),
//                ambientColor = Color(0xffACACAC)
//            )
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

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
)