package com.example.todo_android.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.R
import com.example.todo_android.viewmodel.Todo.TodoViewModel
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.yearMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


@Composable
fun CalendarTitle(yearMonth: YearMonth) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                vertical = 15.dp,
                horizontal = 30.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.size(22.dp),
            painter = painterResource(
                id = R.drawable.headertextemogi
            ),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = if (yearMonth.monthValue.toString().length == 2) {
                "${yearMonth.year}. ${yearMonth.monthValue}"
            } else {
                "${yearMonth.year}. 0${yearMonth.monthValue}"
            },
            fontSize = 22.sp,
            lineHeight = 28.6.sp,
            fontWeight = FontWeight(700),
            color = Color(0xFF000000)
        )
    }
}

@Composable
fun CalendarHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                start = 10.dp,
                end = 10.dp,
            )
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),
                lineHeight = 17.sp,
                fontWeight = FontWeight(700)
            )
        }
    }
}

@Composable
fun CalendarContent(
    vm: TodoViewModel,
    selectedDate: LocalDate,
    weekState: WeekCalendarState,
    monthState: CalendarState,
    isMonthMode: Boolean,
    onSelectedDate: (LocalDate) -> Unit
) {
    AnimatedContent(
        targetState = isMonthMode,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { 0 },
                animationSpec = tween(easing = FastOutLinearInEasing)
            ) togetherWith slideOutVertically(
                targetOffsetY = { 0 },
                animationSpec = tween(easing = LinearOutSlowInEasing)
            )
        },
        content = {
            if (isMonthMode) {
                var start = selectedDate.yearMonth.atDay(1)
                var end = selectedDate.yearMonth.atEndOfMonth()

                HorizontalCalendar(
                    modifier = Modifier
                        .background(color = Color.White),
                    state = monthState,
                    dayContent = { day ->
                        Day(
                            day = day.date,
                            isSelected = selectedDate == day.date,
                            isToday = day.date == LocalDate.now(),
                            isPastDay = day.date < start || day.date > end,
                            onClick = {
                                onSelectedDate(day.date)

                                vm.setTodoYear(day.date.year)
                                vm.setTodoMonth(day.date.monthValue)
                                vm.setTodoDay(day.date.dayOfMonth)
                            },
                        )
                    },
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp
                    )
                )
            } else {
                var start = selectedDate.yearMonth.atDay(1)
                var end = selectedDate.yearMonth.atEndOfMonth()

                WeekCalendar(
                    modifier = Modifier
                        .background(color = Color.White),
                    state = weekState,
                    dayContent = { day ->
                        Day(
                            day = day.date,
                            isSelected = selectedDate == day.date,
                            isToday = day.date == LocalDate.now(),
                            isPastDay = day.date < start || day.date > end,
                            onClick = {
                                onSelectedDate(day.date)

                                vm.setTodoYear(day.date.year)
                                vm.setTodoMonth(day.date.monthValue)
                                vm.setTodoDay(day.date.dayOfMonth)
                            }
                        )
                    },
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp
                    )
                )
            }
        }
    )
}

@Composable
fun Day(
    day: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: (LocalDate) -> Unit,
    isPastDay: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .size(36.dp)
            .padding(10.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) {
                    Color(0xFFFFDAB9)
                } else if (isToday) {
                    Color(0xffE9E9E9)
                } else {
                    Color.Transparent
                },
                CircleShape
            )
            .clickable(
                onClick = {
                    onClick(day)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.alpha(
                if (isPastDay) {
                    0.5f
                } else {
                    1f
                }
            ),
            text = day.dayOfMonth.toString(),
            fontSize = 14.sp,
            color = when (day.dayOfWeek) {
                DayOfWeek.SUNDAY -> Color(0xFFF86B6B)
                else -> Color(
                    0xFF424242
                )
            },
            lineHeight = 17.sp,
            fontWeight = FontWeight(700)
        )
    }
}