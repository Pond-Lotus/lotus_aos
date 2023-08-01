package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.AuthEmailRequest
import com.example.todo_android.Response.ProfileResponse.AuthEmailResponse
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.buttonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goAuthCode(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun authEmail(email: String, routeAction: RouteAction, response: (AuthEmailResponse?) -> Unit) {

    var authEmailResponse: AuthEmailResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit =
        Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

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
                    MyApplication.prefs.setData("email", email)
                    goAuthCode(NAV_ROUTE.AUTHCODE, routeAction)
                }
                "500" -> {
                    response(authEmailResponse)
                    Log.d("AUTHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                }
            }
        }
    })
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun AuthEmailScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }

    val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

    var showErrorText by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()


    Scaffold(modifier = Modifier
        .fillMaxSize()
        .imePadding(), topBar = {
        CenterAlignedTopAppBar(title = {}, navigationIcon = {
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
                .padding(start = 25.dp, end = 25.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.FirstPageNumber),
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xff9E9E9E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.FirstAuthEmailText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(id = R.string.SecondAuthEmailText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowEmailText),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                Spacer(modifier = Modifier.height(10.dp))


                BasicTextField(modifier = Modifier.fillMaxWidth(), value = email, onValueChange = {
                    email = it
                    showErrorText = false
                }, singleLine = true, textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Light
                ), decorationBox = { innerTextField ->
                    if (email.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.ShowEmailPlaceholder),
                            fontSize = 16.sp,
                            lineHeight = 23.sp,
                            fontWeight = FontWeight.Light,
                            color = Color(0xffD3D3D3),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawWithContent {
                                drawContent()
                                drawLine(
                                    color = Color(0xffE9E9E9),
                                    start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                                    end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                        Spacer(modifier = Modifier.padding(vertical = 14.dp))
                        if (emailPattern.matches(email)) {
                            Icon(
                                modifier = Modifier.padding(vertical = 4.dp),
                                painter = painterResource(id = R.drawable.checkemail),
                                contentDescription = null,
                                tint = Color(0xffFF9D4D)
                            )
                        }
                    }

                })

                Spacer(modifier = Modifier.height(8.dp))

                if (showErrorText) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.CantUseEmail),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xffFF9D4D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(250.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(text = "")

                IconButton(modifier = Modifier.size(43.dp),
                    colors = IconButtonDefaults.iconButtonColors(buttonColor),
                    onClick = {
                        if (email != "") {
                            scope.launch {
                                authEmail(email, routeAction, response = {
                                    showErrorText = true
                                })
                            }
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_right_24),
                        contentDescription = null
                    )
                }
            }
        }
    }
}