package com.example.todo_android.screen


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
import com.example.todo_android.R
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.navigation.NAV_ROUTE


fun goLogin(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun goRegister(route: NAV_ROUTE, routeAction: RouteAction) {
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
            onClick = { goRegister(NAV_ROUTE.REGISTER, routeAction) }
        ) {
            Text(text = stringResource(id = R.string.register), color = Color.Black)
        }
    }
}

//@ExperimentalMaterial3Api
//@Composable
//@Preview
//fun MainScreenPreview() {
//    MainScreen()
//}


//    LoginScreen()
//    AuthEmailScreen()
//    AuthCodeScreen()
//    RegisterScreen()