package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Modify.ChangePassword
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Request.ModifyRequest.ChangePasswordRequest
import com.example.todo_android.Response.ModifyResponse.ChangePasswordResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun changePassword(
    token: String,
    originPass: String,
    password2: String,
    password3: String,
    routeAction: RouteAction,
) {

    if (!(password2.length >= 8 && password3.length >= 8)) {
        Log.d("changePassword", "비밀번호 8자리 이상이 아닙니다.")
    }

    if (!(password2.equals(password3))) {
        Log.d("changePassword", "비밀번호가 일치하지 않습니다")
    }

    var changePasswordResponse: ChangePasswordResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var changePasswordRequest: ChangePasswordRequest =
        retrofit.create(ChangePasswordRequest::class.java)


    changePasswordRequest.requestChangePassword(token, ChangePassword(originPass, password3))
        .enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>,
            ) {
                changePasswordResponse = response.body()

                when (changePasswordResponse?.resultCode) {

                    "200" -> {
                        MyApplication.prefs.setData("password1", password3)
                        routeAction.goBack()
                        Log.d("changePassword", "password1 : ${originPass} / password2 : ${password3}")
                        Log.d("changePassword", "resultCode : " + changePasswordResponse?.resultCode)
                    }

                    "500" -> {
                        Log.d("changePassword",
                            "resultCode : " + changePasswordResponse?.resultCode)
                    }

                }
            }
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Log.e("changePassword", t.message.toString())
            }

        })
}

@ExperimentalMaterial3Api
@Composable
fun ChangePasswordScreen(routeAction: RouteAction) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"


    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var password3 by remember { mutableStateOf("") }

    var originPass: String = MyApplication.prefs.getData("password1", password1)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBar(
            title = {
                Text(text = "비밀번호 변경")
            },
            navigationIcon = {
                IconButton(
                    onClick = { routeAction.goBack() }
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "goback")
                }
            }
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "현재 비밀번호")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = MyApplication.prefs.getData("password1", password1),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                password1 = it
            },
            placeholder = {
                Text(
                    text = "비밀번호",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))


        Text(text = "새 비밀번호")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = password2,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                password2 = it
            },
            placeholder = {
                Text(
                    text = "비밀번호",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = " 새 비밀번호 확인")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = password3,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                password3 = it
            },
            placeholder = {
                Text(
                    text = "비밀번호 확인",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            modifier = Modifier
                .width(322.dp)
                .height(50.dp)
                .background(Color(0xffFBE3C7)),
            shape = RoundedCornerShape(8.dp),
            onClick = { changePassword(token, originPass, password2, password3, routeAction) })
        {
            Text(text = "변경 완료")
        }
    }
}