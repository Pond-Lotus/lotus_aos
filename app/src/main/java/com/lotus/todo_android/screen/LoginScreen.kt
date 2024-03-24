package com.lotus.todo_android.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lotus.todo_android.Data.Profile.Login
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction
import com.lotus.todo_android.navigation.NAV_ROUTE
import com.lotus.todo_android.request.ProfileRequest.LoginRequest
import com.lotus.todo_android.response.ProfileResponse.LoginResponse
import com.lotus.todo_android.util.MyApplication
import com.lotus.todo_android.viewmodel.Profile.LoginViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalMaterial3Api
fun sendLogin(
    email: String,
    password: String,
    fcm_token: String,
    routeAction: RouteAction,
    response: (LoginResponse?) -> Unit,
) {

    var loginResponse: LoginResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit =
        Retrofit.Builder().baseUrl(" https://team-lotus.kr/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    var loginRequest: LoginRequest = retrofit.create(LoginRequest::class.java)

    loginRequest.requestLogin(com.lotus.todo_android.Data.Profile.Login(email, password, fcm_token)).enqueue(object : Callback<LoginResponse> {

        //실패할 경우
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.e("LOGIN", t.message.toString())
        }

        //성공할 경우
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            loginResponse = response.body()

            when (loginResponse?.resultCode) {
                "200" -> {
                    routeAction.navTo(NAV_ROUTE.CALENDAR)
                    MyApplication.prefs.setData("token", loginResponse?.token.toString())
                    MyApplication.prefs.setData("nickname", loginResponse?.nickname.toString())
                    MyApplication.prefs.setData("image", loginResponse?.image.toString())

                    Log.d("LOGIN", "resultCode : " + loginResponse?.resultCode)
                    Log.d("LOGIN", "token : " + loginResponse?.token)
                    //     Log.d("LOGIN", "nickname : " + loginResponse?.nickname)
                    Log.d("LOGIN", "email : " + loginResponse?.email)
                    Log.d("LOGIN", "image : " + loginResponse?.image)
                    MyApplication.prefs.setData("email", email)
                    Log.d("LOGIN", "메인 화면으로 갑니다.")
                }
                "500" -> {
                    response(loginResponse)
                    Log.d("LOGIN", "non_field_errors:[Check Your Email or Password]")
                }
            }
        }
    })
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(routeAction: RouteAction) {

    val vm: LoginViewModel = viewModel()
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val fcm_token = MyApplication.prefs.getData("fcm_token", "")

    var checked by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    var openDialog by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    if (openDialog) {
        FailureLoginDialog(onDismissRequest = { openDialog = false })
    }

    val icon = if (passwordVisible) {
        painterResource(id = R.drawable.openeye)
    } else {
        painterResource(id = R.drawable.closeeye)
    }

    val onCheck = if (checked) {
        painterResource(id = R.drawable.check)
    } else {
        painterResource(id = R.drawable.nocheck)
    }

    if (checked) {
//        MyApplication.prefs.setData("email", email)
        MyApplication.prefs.setData("password1", password)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 41.dp, end = 41.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.size(90.dp),
            painter = painterResource(id = R.drawable.appmainlogo),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Image(
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
                .padding(top = 8.dp),
            painter = painterResource(id = R.drawable.apptitle),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(61.dp))


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            value = email,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = vm::inputEmail,
            placeholder = {
                Text(
                    text = "이메일 입력",
                    style = LocalTextStyle.current.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {
                Image(
                    modifier = Modifier.clickable {
                        vm.inputEmail("")
                    },
                    painter = painterResource(id = R.drawable.deleteemail),
                    contentDescription = null
                )
            },
            textStyle = LocalTextStyle.current.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            ),
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            value = password,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(18.dp),
            onValueChange = vm::inputPassword,
            placeholder = {
                Text(
                    text = "비밀번호 입력",
                    style = LocalTextStyle.current.copy(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(painter = icon, contentDescription = null, tint = Color(0xffC9C9C9))
                }
            },
            textStyle = LocalTextStyle.current.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    checked = !checked
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Image(painter = onCheck, contentDescription = null)

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = stringResource(id = R.string.AuthLogin),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFDAB9)),
            onClick = {
                if (email == "" || password == "") {
                    openDialog = true
                } else {
                    scope.launch {
                        sendLogin(
                            email,
                            password,
                            fcm_token,
                            routeAction
                        ) {
                            openDialog = true
                        }
                    }
                }
            },
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = stringResource(id = R.string.Login),
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(id = R.string.Search),
                fontSize = 14.sp,
                color = Color(0xFF999999),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        routeAction.navTo(NAV_ROUTE.SEARCHPASSWORD)
                    })

            Text(text = stringResource(id = R.string.Register),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 7.dp)
                    .clickable {
                        routeAction.navTo(NAV_ROUTE.AUTHEMAIL)
                    })
        }
    }
}

@Composable
fun FailureLoginDialog(onDismissRequest: () -> Unit) {

    Dialog(onDismissRequest = { onDismissRequest }) {
        androidx.compose.material3.Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 28.dp, bottom = 10.dp),
                    text = "로그인 실패",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "이메일 혹은 비밀번호를",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Text(
                    modifier = Modifier.padding(bottom = 28.dp),
                    text = "다시 확인해 주세요.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material.TextButton(modifier = Modifier
                        .background(Color(0xffE9E9E9))
                        .weight(1f),
                        onClick = {
                            onDismissRequest()
                        }) {
                        Text(text = "확인", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}