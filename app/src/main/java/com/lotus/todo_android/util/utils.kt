package com.lotus.todo_android.util

import androidx.compose.runtime.*
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.core.Week
import kotlinx.coroutines.flow.filter
import java.time.Month
import java.time.YearMonth


@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) {
        mutableStateOf(state.firstVisibleWeek)
    }

    LaunchedEffect(state) {
        snapshotFlow {
            state.isScrollInProgress
        }.filter { scrolling -> !scrolling }.collect {
            visibleWeek.value = state.firstVisibleWeek
        }
    }

    return visibleWeek.value
}

@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): com.kizitonwose.calendar.core.CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

private fun Month.displayText(short: Boolean = true): String {
    val style = if (short) java.time.format.TextStyle.SHORT else java.time.format.TextStyle.FULL
    return getDisplayName(style, java.util.Locale.KOREAN)
}