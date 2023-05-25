package com.example.todo_android.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Util.MyApplication
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(routeAction: RouteAction) {
    val backgroundColor = listOf(Color(0xffFFDAB9), Color(0xffFF9D4D))

    LaunchedEffect(Unit) {
        delay(3000) // 3초 동안 딜레이 설정

        routeAction.navTo(NAV_ROUTE.LOGIN)

//        if(MyApplication.prefs.getData("email", "").isNotEmpty() && MyApplication.prefs.getData("password1", "").isNotEmpty()){
//            routeAction.navTo(NAV_ROUTE.CALENDAR)
//        } else{
//            // 로그인 화면으로 전환
//            routeAction.navTo(NAV_ROUTE.LOGIN)
//        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = backgroundColor,
                start = Offset.Zero,
                end = Offset.Infinite),
                shape = RectangleShape,
                alpha = 1.0f
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(107.dp),
            painter = painterResource(id = R.drawable.whiteapptitle),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .width(103.dp)
                .height(40.dp),
            painter = painterResource(id = R.drawable.whiteapplogo),
            contentDescription = null
        )
    }
}