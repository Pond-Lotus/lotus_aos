package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.ProfileRequest.RegisterRequest
import com.example.todo_android.Response.ProfileResponse.RegisterResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goMain2(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun Register(
    email: String, nickname: String, password1: String, password2: String, routeAction: RouteAction,
){

    if (!(password1.length >= 8 && password2.length >= 8)) {
        Log.d("FAIL", "비밀번호 8자리 이상이 아닙니다.")
    }

    if (!(password1.equals(password2))) {
        Log.d("FAIL", "비밀번호가 일치하지 않습니다")
    }
    else {

        var registerResponse: RegisterResponse? = null

        var retrofit = Retrofit.Builder()
            .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var registerRequest: RegisterRequest = retrofit.create(RegisterRequest::class.java)

        registerRequest.requestRegister(
            com.example.todo_android.Data.Profile.Register(
                email, nickname, password2))
            .enqueue(object : Callback<RegisterResponse> {

                // 실패 했을때
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("", t.message.toString())
                }

                // 성공 했을때
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>,
                ) {

                    registerResponse = response.body()

                    when (registerResponse?.resultCode) {
                        "200" -> {
                            goMain2(NAV_ROUTE.MAIN, routeAction)
                            Log.d("REGISTER", "메인 화면으로 갑니다.")
                            Log.d("REGISTER", "resultCode : " + registerResponse?.resultCode)
                        }
                        "500" -> {
                            Log.d("REGISTER", "resultCode : " + registerResponse?.resultCode)
                        }
                    }
                }
            })
    }
}

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        var email by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var password1 by remember { mutableStateOf("") }
        var password2 by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier.width(300.dp).focusable(false),
            value = MyApplication.prefs.getData("email", email),
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
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            modifier = Modifier.width(300.dp),
            value = nickname,
            colors = TextFieldDefaults.textFieldColors(
                Color(0xff9E9E9E),
                disabledLabelColor = Color(0xffE9E9E9),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
//            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                nickname = it
            })

        Spacer(modifier = Modifier.height(60.dp))

        TextField(
            modifier = Modifier.width(300.dp),
            value = password1,
            colors = TextFieldDefaults.textFieldColors(
                Color(0xff9E9E9E),
                disabledLabelColor = Color(0xffE9E9E9),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                password1 = it
            })

        Spacer(modifier = Modifier.height(60.dp))

        TextField(
            modifier = Modifier.width(300.dp),
            value = password2,
            colors = TextFieldDefaults.textFieldColors(
                Color(0xff9E9E9E),
                disabledLabelColor = Color(0xffE9E9E9),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                password2 = it
            })

        Spacer(modifier = Modifier.height(60.dp))

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
                onClick = { Register(email, nickname, password1, password2, routeAction) }
            ) {
                Text(
                    text = "다음 >",
                    color = Color.Black)
            }
        }
    }
}