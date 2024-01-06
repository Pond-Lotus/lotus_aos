package com.example.todo_android.composable

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todo_android.R

@Composable
fun AddTodoFloatingButton(
    multiFloatingState: FloatingStateType,
    onMultiFloatingStateChange: (FloatingStateType) -> Unit,
    backgroundColor: Color,
    onButtonClick: (String) -> Unit,
//    focusRequester: FocusRequester,
) {
    val transition = updateTransition(targetState = multiFloatingState, label = null)

    val rotate by animateFloatAsState(
        targetValue = if (multiFloatingState == FloatingStateType.Expanded) 45f else 0f,
        animationSpec = tween(durationMillis = 400) // 여기서 애니메이션 지속 시간을 설정할 수 있습니다.
    )

    val NextButtonArrowTint = when (multiFloatingState) {
        FloatingStateType.Expanded -> {
            Color(0xFFFFFFFF).copy(alpha = 1f)
        }
        FloatingStateType.Collapsed -> {
            Color(0xFF424242).copy(alpha = 1f)
        }
    }

    Column(
//        modifier = Modifier
//            .padding(
//                end = 20.dp
//            ),
        horizontalAlignment = Alignment.End
    ) {

        AnimatedVisibility(
            visible = (multiFloatingState == FloatingStateType.Expanded),
            enter = fadeIn(
                animationSpec = tween(500)
            ) + slideInVertically(
                animationSpec = tween(500),
                initialOffsetY = {
                    it / 8
                }
            ),
            exit = fadeOut(
                animationSpec = tween(500)
            ) + slideOutVertically(
                animationSpec = tween(500),
                targetOffsetY = {
                    it / 8
                }
            )
        ) {
            TodoMenu(
                onMultiFloatingStateChange = onMultiFloatingStateChange,
                onButtonClick = onButtonClick
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .size(65.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = Color(0xff9E9E9E),
                    ambientColor = Color(0xffACACAC)
                ),
            containerColor = backgroundColor,
            shape = CircleShape,
            onClick = {
                onMultiFloatingStateChange(
                    if (transition.currentState == FloatingStateType.Expanded) {
                        FloatingStateType.Collapsed
                    } else {
                        FloatingStateType.Expanded
                    }
                )
            }) {
            Icon(
                modifier = Modifier
                    .rotate(rotate)
                    .size(32.dp)
                    .background(Color.Transparent),
                painter = painterResource(id = R.drawable.todolistaddemogi),
                contentDescription = null,
                tint = NextButtonArrowTint
            )
        }
    }
}