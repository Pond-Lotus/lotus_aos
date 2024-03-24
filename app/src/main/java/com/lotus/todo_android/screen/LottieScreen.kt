package com.lotus.todo_android.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction
import com.lotus.todo_android.navigation.NAV_ROUTE
import com.lotus.todo_android.ui.theme.buttonColor
import com.lotus.todo_android.util.MyApplication

@Composable
fun LottieScreen(routeAction: RouteAction) {

    val nickname = MyApplication.prefs.getData("nickname", "")

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.todori))
    val lottieAnimatable = rememberLottieAnimatable()
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.size(300.dp)) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize()
            )


            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = stringResource(id = R.string.SucessRegister),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E),
                    fontFamily = FontFamily.SansSerif
                )
            )
        }




        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${nickname}님, 환영합니다!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            modifier = Modifier
                .width(135.dp)
                .height(39.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(buttonColor),
            onClick = {
                routeAction.navTo(NAV_ROUTE.LOGIN)
            }) {
            Text(
                color = Color.Black,
                text = stringResource(id = R.string.goLogin),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}