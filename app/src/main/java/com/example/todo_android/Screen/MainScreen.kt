package com.example.todo_android.Screen


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R


fun goLogin(route: NAV_ROUTE, routeAction: RouteAction) {
    Log.d("MAIN", "로그인 화면으로 갑니다.")
    routeAction.navTo(route)
}

fun goRegister(route: NAV_ROUTE, routeAction: RouteAction) {
    Log.d("MAIN", "이메일 인증 화면으로 갑니다.")
    routeAction.navTo(route)
}


@ExperimentalMaterial3Api
@Composable
fun MainScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { goLogin(NAV_ROUTE.LOGIN, routeAction) }
        ) {
            Text(text = stringResource(id = R.string.login), color = Color.Black)
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { goRegister(NAV_ROUTE.AUTHEMAIL, routeAction) }
        ) {
            Text(text = stringResource(id = R.string.register), color = Color.Black)
        }
    }
}