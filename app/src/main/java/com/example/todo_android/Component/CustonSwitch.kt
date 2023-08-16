package com.example.todo_android.Component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout

@ExperimentalMotionApi
@Composable
fun MonthWeekToggleSwitch(
    width: Int,
    height: Int,
    animateState: MutableState<Boolean>) {


    val progressState by animateFloatAsState(
        targetValue =  if(animateState.value) {
            1f
        } else{
            0f
        }
    )

    MotionLayout(
        start = startConstraintsSet(parentWidth = width, parentHeight = height),
        end = endConstraintsSet(parentWidth = width, parentHeight = height),
        progress = progressState
    ) {
        Box(
            modifier = Modifier
                .width(115.dp)
                .height(35.dp)
                .layoutId("ToggleSwitchBackground")
                .background(Color(0xffe9e9e9))
                .clip(RoundedCornerShape(7.dp))
        )

        Box(
            modifier = Modifier
                .layoutId("Switch")
                .width(55.dp)
                .height(25.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(7.dp))
        )


        Box(
            modifier = Modifier.layoutId("월간"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "월간",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier.layoutId("주간"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "주간",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

//    Row(
//        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//        MotionLayout(
//            start = startConstraintsSet(parentWidth = width, parentHeight = height),
//            end = endConstraintsSet(parentWidth = width, parentHeight = height),
//            progress = progressState
//        ) {
//            Box(
//                modifier = Modifier
//                    .width(115.dp)
//                    .height(35.dp)
//                    .layoutId("ToggleSwitchBackground")
//                    .background(Color(0xffe9e9e9))
//                    .clip(RoundedCornerShape(7.dp))
//            )
//
//            Box(
//                modifier = Modifier
//                    .layoutId("Switch")
//                    .width(55.dp)
//                    .height(25.dp)
//                    .background(Color.White)
//                    .clip(RoundedCornerShape(7.dp))
//            )
//
//
//            Box(
//                modifier = Modifier.layoutId("월간"),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "월간",
//                    color = Color.Black,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Box(
//                modifier = Modifier.layoutId("주간"),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "주간",
//                    color = Color.Black,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
}

//for Month State
private fun startConstraintsSet(
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

        //ToggleSwitch: left side default
        constrain(ToggleSwitch) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value((parentHeight * 0.5).dp)
        }

        //text_month(월간): left side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value((parentHeight * 0.5).dp)
        }

        //text_week(주간): left side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value((parentHeight * 0.5).dp)
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
            height = Dimension.value((parentHeight * 0.5).dp)
        }

        //text_month(월간): right side default
        constrain(text_month) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value((parentHeight * 0.5).dp)
        }

        //text_week(주간): right side default
        constrain(text_week) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            width = Dimension.value((parentWidth * 0.5).dp)
            height = Dimension.value((parentHeight * 0.5).dp)
        }
    }
}









//Box(
//modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
//) {
//    Row(
//        modifier = Modifier
//            .width(120.dp)
//            .height(35.dp)
//            .clip(shape = RoundedCornerShape(7.dp))
//            .background(Color(0xffe9e9ed))
//            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
//    ) {
//        states.forEach { text ->
//            Text(text = text,
//                fontSize = 10.sp,
//                lineHeight = 14.sp,
//                color = if (text == selectedOption) {
//                    Color.Black
//                } else {
//                    Color.Gray
//                },
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(5.dp))
//                    .clickable {
//                        onSelectionChange(text)
//                        selectCalendar = (text == states[1])
//                    }
//                    .background(
//                        if (text == selectedOption) {
//                            Color.White
//                        } else {
//                            Color(0xffe9e9ed)
//                        }
//                    )
//                    .padding(
//                        vertical = 6.dp,
//                        horizontal = (18.5).dp,
//                    ))
//        }
//    }
//}
