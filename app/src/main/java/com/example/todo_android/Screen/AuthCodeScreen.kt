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
import com.example.todo_android.Data.AuthCode
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.AuthCodeRequest
import com.example.todo_android.Response.AuthCodeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun goProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun authCode(email: String, code: String, routeAction: RouteAction) {

    var authCodeResponse: AuthCodeResponse? = null


    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var authCodeRequest: AuthCodeRequest = retrofit.create(AuthCodeRequest::class.java)

    authCodeRequest.requestCode(AuthCode(email, code)).enqueue(object : Callback<AuthCodeResponse> {

        //실패할 경우
        override fun onFailure(call: Call<AuthCodeResponse>, t: Throwable) {
            Log.e("authCode", t.message.toString())
        }

        //성공할 경우
        override fun onResponse(call: Call<AuthCodeResponse>, response: Response<AuthCodeResponse>) {
            authCodeResponse = response.body()

            when (authCodeResponse?.resultCode) {
                "200" -> {
                    Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                    Log.d("AUTHCODE", "프로필 작성 화면으로 갑니다.")
                    goProfile(NAV_ROUTE.REGISTER, routeAction)
                }
                "500" -> {
                    Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                }
            }
        }
    })
}

@ExperimentalMaterial3Api
@Composable
fun AuthCodeScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var email by remember { mutableStateOf("test001@test.com") }
        var code by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier.width(300.dp),
            value = code,
            colors = TextFieldDefaults.textFieldColors(
                Color(0xff9E9E9E),
                disabledLabelColor = Color(0xff9E9E9E),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                code = it
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
                onClick = { authCode(email, code, routeAction) }
            ) {
                Text(
                    text = "다음 >",
                    color = Color.Black)
            }
        }
    }
}