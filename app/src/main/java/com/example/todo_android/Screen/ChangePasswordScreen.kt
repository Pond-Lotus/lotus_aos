package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.Data.Modify.ChangePassword
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ModifyRequest.ChangePasswordRequest
import com.example.todo_android.Response.ModifyResponse.ChangePasswordResponse
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.buttonColor
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
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

    val buttonColor = if (password1 != "" && password2 != "" && password3 != "") {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    val textColor = if (password1 != "" && password2 != "" && password3 != "") {
        Color.Black
    } else{
        Color(0xFF9E9E9E)
    }

    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        showChangePasswordDialog(onDismissRequest = { openDialog = false }, routeAction)
    }

    val passwordVisualTransformation = PasswordVisualTransformation()

    var scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .height(45.dp)
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color(0x26000000), // 기존에 사용 중이셨던 보더 컬러를 선택하세요.
                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx() // 보더 두께를 원하는 값으로 설정하세요.
                    )
                }) {
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
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 20.dp, end = 20.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(vertical = 35.dp))

            Text(
                text = "현재 비밀번호",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(Color.White),
                border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = password1,
                        onValueChange = {
                            password1 = it
                            showErrorPassword1 = false
                        },
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Black,
                            lineHeight = 31.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                    )
                }
            }

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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(Color.White),
                border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = password2,
                        onValueChange = {
                            password2 = it
                            showMatchPassword2 = !it.matches(passwordPattern)
                        },
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Black,
                            lineHeight = 31.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                    )
                }
            }

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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(Color.White),
                border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = password3,
                        onValueChange = {
                            password3 = it
                            showMatchPassword3 = (it != password2)
                        },
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Black,
                            lineHeight = 31.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                    )
                }
            }

            if (showMatchPassword3) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "새 비밀번호가 일치하지 않습니다.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color(0xffFF9D4D)
                )
            }

            Spacer(modifier = Modifier.padding(bottom = 40.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(buttonColor),
                onClick = {
                    if (isButtonClickable == true) {
                        scope.launch {
                            changePassword(token, password1, password3, response = {
                                when (it?.resultCode) {
                                    "200" -> {
                                        openDialog = true
                                    }
                                    "500" -> {
                                        showErrorPassword1 = true
                                    }
                                }
                            })
                        }
                    }
                },
                enabled = isButtonClickable,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "변경 완료",
                    color = textColor,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun showChangePasswordDialog(onDismissRequest: () -> Unit, routeAction: RouteAction) {
    Dialog(onDismissRequest = { onDismissRequest }) {
        Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 28.dp, bottom = 11.dp),
                    text = "변경 완료",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "새로운 비밀번호로", fontSize = 15.sp, fontWeight = FontWeight.Light
                )

                Text(
                    modifier = Modifier.padding(bottom = 28.dp),
                    text = "다시 로그인해 주세요.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material.TextButton(
                        modifier = Modifier
                            .background(buttonColor)
                            .weight(1f), onClick = {
                            onDismissRequest()
                            routeAction.navTo(NAV_ROUTE.LOGIN)
                        }) {
                        Text(text = "확인", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}