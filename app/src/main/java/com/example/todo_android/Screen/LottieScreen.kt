package com.example.todo_android.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.nextButtonColor

fun goMain(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

@Composable
fun LottieScreen(routeAction: RouteAction) {

    val nickname = MyApplication.prefs.getData("nickname", "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(223.dp),
            painter = painterResource(id = R.drawable.lottie),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(id = R.string.SucessRegister),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xff9E9E9E),
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${nickname}???, ???????????????!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.height(33.dp))

        Button(
            modifier = Modifier
                .width(135.dp)
                .height(39.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(nextButtonColor),
            onClick = {
                goMain(NAV_ROUTE.LOGIN, routeAction)
            }) {
            Text(
                color = Color.Black,
                text = stringResource(id = R.string.goLogin)
            )
        }
    }
}