package com.example.todo_android.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Util.MyApplication

fun goMain(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

@Composable
fun LottieScreen(routeAction: RouteAction) {

    val nickname = MyApplication.prefs.getData("nickname", "")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .width(223.dp)
                .width(223.dp),
            painter = painterResource(id = R.drawable.lottie),
            contentDescription = "lottieImage")

        Text(
            text = "회원가입 완료!",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xff9E9E9E),
                fontFamily = FontFamily.SansSerif
            )
        )
        Text(
            text = "${nickname} 님, 환영합니다!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        Button(
            modifier = Modifier
                .width(135.dp)
                .height(39.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = {
                goMain(NAV_ROUTE.LOGIN, routeAction)
            }) {
            Text(text = "로그인 하기")
        }
    }
}