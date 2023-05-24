package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Modify.ChangePassword
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.R
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
    originPassword: String,
    newPassword: String,
    response: (ChangePasswordResponse?) -> Unit
) {

    var changePasswordResponse: ChangePasswordResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var changePasswordRequest: ChangePasswordRequest =
        retrofit.create(ChangePasswordRequest::class.java)


    changePasswordRequest.requestChangePassword(token, ChangePassword(originPassword, newPassword))
        .enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>,
            ) {
                changePasswordResponse = response.body()

                when (changePasswordResponse?.resultCode) {

                    "200" -> {
                        response(changePasswordResponse)
                        Log.d(
                            "changePassword",
                            "password1 : ${originPassword} / password2 : ${newPassword}"
                        )
                        Log.d(
                            "changePassword", "resultCode : " + changePasswordResponse?.resultCode
                        )
                    }

                    "500" -> {
                        response(changePasswordResponse)
                        Log.d(
                            "changePassword", "resultCode : " + changePasswordResponse?.resultCode
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Log.e("changePassword", t.message.toString())
            }
        })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ChangePasswordScreen(routeAction: RouteAction) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"


    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var password3 by remember { mutableStateOf("") }

    val passwordPattern =
        Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,15}\$")

    var showErrorPassword1 by remember { mutableStateOf(false) }
    var showMatchPassword2 by remember { mutableStateOf(false) }
    var showMatchPassword3 by remember { mutableStateOf(false) }


    var isButtonClickable by remember { mutableStateOf(false) }

    if (password2.matches(passwordPattern) && password3 == password2) {
        isButtonClickable = true
    } else {
        isButtonClickable = false
    }

    val color = if (password1 != "" && password2 != "" && password3 != "") {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    val passwordVisualTransformation = PasswordVisualTransformation()

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                text = "비밀번호 변경",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
        }, navigationIcon = {
            IconButton(onClick = {
                routeAction.goBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 20.dp, end = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(vertical = 47.dp))

            Text(
                text = "현재 비밀번호",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp)
            )


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 8.dp),
                value = password1,
                onValueChange = {
                    password1 = it
                    showErrorPassword1 = false
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xffffffff),
                    disabledLabelColor = Color(0xffffffff),
                    focusedIndicatorColor = Color(0xffD0D0D0),
                    unfocusedIndicatorColor = Color(0xffD0D0D0)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = passwordVisualTransformation,
                singleLine = true
            )

            if (showErrorPassword1) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = "현재 비밀번호와 일치하지 않습니다.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xffFF9D4D)
                )
            }

            Text(
                text = "새 비밀번호",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 8.dp),
                value = password2,
                onValueChange = {
                    password2 = it
                    showMatchPassword2 = !it.matches(passwordPattern)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xffffffff),
                    disabledLabelColor = Color(0xffffffff),
                    focusedIndicatorColor = Color(0xffD0D0D0),
                    unfocusedIndicatorColor = Color(0xffD0D0D0)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = passwordVisualTransformation,
                singleLine = true
            )

            if (showMatchPassword2) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = stringResource(id = R.string.UnderPassword),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xffFF9D4D)
                )
            }


            Text(
                text = "새 비밀번호 확인",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 8.dp),
                value = password3,
                onValueChange = {
                    password3 = it
                    showMatchPassword3 = (it != password2)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xffffffff),
                    disabledLabelColor = Color(0xffffffff),
                    focusedIndicatorColor = Color(0xffD0D0D0),
                    unfocusedIndicatorColor = Color(0xffD0D0D0)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = passwordVisualTransformation,
                singleLine = true
            )

            if (showMatchPassword3) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "새 비밀번호가 일치하지 않습니다.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xffFF9D4D)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 40.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(color),
                onClick = {
                    if (isButtonClickable == true) {
                        changePassword(token, password1, password3, response = {
                            if(it?.resultCode == "200"){
                                routeAction.goBack()
                            } else{
                                showErrorPassword1 = true
                            }
                        })
                    }
                },
                enabled = isButtonClickable,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "변경 완료",
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}