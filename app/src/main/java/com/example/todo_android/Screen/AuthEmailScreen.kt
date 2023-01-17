package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.AuthEmailRequest
import com.example.todo_android.Response.AuthEmailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goAuthCode(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun authEmail(email: String, routeAction: RouteAction) {

    var authEmailResponse: AuthEmailResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var authEmailRequest: AuthEmailRequest = retrofit.create(AuthEmailRequest::class.java)

    authEmailRequest.requestEmail(email).enqueue(object : Callback<AuthEmailResponse> {

        //실패할 경우
        override fun onFailure(call: Call<AuthEmailResponse>, t: Throwable) {
            Log.e("authEmail", t.message.toString())
        }

        //성공할 경우
        override fun onResponse(
            call: Call<AuthEmailResponse>,
            response: Response<AuthEmailResponse>,
        ) {
            authEmailResponse = response.body()

            when (authEmailResponse?.resultCode) {
                "200" -> {
                    Log.d("AUTHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                    Log.d("AUTHEMAIL", "이메일 인증 코드 입력 화면으로 갑니다.")
                    goAuthCode(NAV_ROUTE.AUTHCODE, routeAction)
                }
                "500" -> {
                    Log.d("AUTHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                }
            }
        }
    })
}


@ExperimentalMaterial3Api
@Composable
fun AuthEmailScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier.width(300.dp),
            value = email,
            colors = TextFieldDefaults.textFieldColors(
                Color(0xff9E9E9E),
                disabledLabelColor = Color(0xff9E9E9E),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                email = it
            })
        Spacer(modifier = Modifier.height(250.dp))
        Row(
        ) {
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
                onClick = { routeAction.goBack() }
            ) {
                Text(
                    text = "< 이전",
                    color = Color.Black)
            }
            Spacer(modifier = Modifier.width(150.dp))
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
                onClick = { authEmail(email, routeAction) }
            ) {
                Text(
                    text = "다음 >",
                    color = Color.Black)
            }
        }
    }
}