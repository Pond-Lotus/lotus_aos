package com.example.todo_android.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import kotlinx.coroutines.Job

@ExperimentalMaterialApi
@ExperimentalMotionApi
@Composable
fun MonthWeekToggleSwitch(
    width: Int,
    height: Int,
    animateState: MutableState<Boolean>,
    onChangeCalendar: () -> Job
) {
    val progressState by animateFloatAsState(
        targetValue = if (animateState.value) {
            0f
        } else {
            1f
        }
    )

    val toggleSwitchMonthText by animateColorAsState(
        targetValue = if (animateState.value) {
            Color.Black // when on
        } else {
            Color(0xFF9E9E9E) // when off
        },
    )

    val toggleSwitchWeekText by animateColorAsState(
        targetValue = if (animateState.value) {
            Color(0xFF9E9E9E) // when on
        } else {
            Color.Black //when off
        },
    )

    MotionLayout(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(7.dp)),
        start = startConstraintsSet(parentWidth = width, parentHeight = height),
        end = endConstraintsSet(parentWidth = width, parentHeight = height),
        progress = progressState
    ) {
        Box(
            modifier = Modifier
                .width(115.dp)
                .height(35.dp)
                .clip(RoundedCornerShape(7.dp))
                .layoutId("ToggleSwitchBackground")
                .background(Color(0xffe9e9e9))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onChangeCalendar()
                        }
                    )
                }
        )

        Box(
            modifier = Modifier
                .layoutId("ToggleSwitch")
                .padding(2.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White)
        )
        Box(
            modifier = Modifier
                .layoutId("text_month"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "월간",
                color = toggleSwitchMonthText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }

        Box(
            modifier = Modifier
                .layoutId("text_week"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "주간",
                color = toggleSwitchWeekText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

//for Month State
private fun startConstraintsSet(
    parentWidth: Int, parentHeight: Int
): ConstraintSet {
    return ConstraintSet {
        val ToggleSwitchBackground = createRefFor("ToggleSwitchBackground")
        val ToggleSwitch = createRefFor("ToggleSwitch")
        val text_month = createRefFor("text_month")
        val text_week = createRefFor("text_week")

        constrain(ToggleSwitchBackground) {
            width = Dimension.value(parentWidth.dp)
            height = Dimension.value(parentHeight.dp)
        }

        //ToggleSwitch: left side default
        constrain(ToggleSwitch) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_month(월간): left side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_week(주간): left side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }
    }
}

//for Week State
private fun endConstraintsSet(
    parentWidth: Int,
    parentHeight: Int
): ConstraintSet {
    return ConstraintSet {
        val ToggleSwitchBackground = createRefFor("ToggleSwitchBackground")
        val ToggleSwitch = createRefFor("ToggleSwitch")
        val text_month = createRefFor("text_month")
        val text_week = createRefFor("text_week")

        constrain(ToggleSwitchBackground) {
            width = Dimension.value(parentWidth.dp)
            height = Dimension.value(parentHeight.dp)
        }

        //ToggleSwitch: right side default
        constrain(ToggleSwitch) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_month(월간): right side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }

        //text_week(주간): right side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value(parentHeight.dp)
        }
    }
}