package com.example.todo_android.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo_android.Navigation.Action.RouteAction
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarType

@Composable
fun CalendarScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        Kalendar(kalendarType = KalendarType.Oceanic())
        Kalendar(kalendarType = KalendarType.Firey)
    }
}