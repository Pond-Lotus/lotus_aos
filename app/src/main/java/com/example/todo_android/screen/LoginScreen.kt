package com.example.todo_android.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_android.R
import com.example.todo_android.task.Action.LoginRequest
import com.example.todo_android.task.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun sendLogin(email: String, password: String) {

    var loginResponse: LoginResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var loginRequest: LoginRequest = retrofit.create(LoginRequest::class.java)

    loginRequest.requestLogin(email, password).enqueue(object : Callback<LoginResponse> {

        //실패할 경우
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.e("Login", t.message.toString())
        }

        //성공할 경우
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            loginResponse = response.body()
            Log.d("LOGIN", "resultCode : " + loginResponse?.resultCode)
            Log.d("LOGIN", "token : " + loginResponse?.token)
        }
    })
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }


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
            value = password,
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
                password = it
            })
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick = { sendLogin(email, password) }
        ) {
            Text(text = stringResource(id = R.string.login), color = Color.Black)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun LoginScreenPreivew() {
    LoginScreen()
}
