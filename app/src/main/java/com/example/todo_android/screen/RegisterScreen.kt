package com.example.todo_android.screen

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
import com.example.todo_android.Request.RegisterRequest
import com.example.todo_android.Response.RegisterResponse
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.navigation.NAV_ROUTE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Register(email: String, nickname: String, password1: String, password2: String, routeAction: RouteAction) {

    var registerResponse: RegisterResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var registerRequest: RegisterRequest = retrofit.create(RegisterRequest::class.java)

    registerRequest.requestRegister(com.example.todo_android.Data.Register(email, nickname, password1, password2)).enqueue(object : Callback<RegisterResponse> {
        override fun onResponse(
            call: Call<RegisterResponse>,
            response: Response<RegisterResponse>,
        ) {
            TODO("Not yet implemented")
        }

        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            TODO("Not yet implemented")
        }

    })
}

fun goMain2(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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