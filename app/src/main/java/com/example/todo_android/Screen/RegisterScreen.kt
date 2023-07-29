package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.RegisterRequest
import com.example.todo_android.Response.ProfileResponse.RegisterResponse
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
import java.io.ByteArrayOutputStream

fun goLottie(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun Register(
    authEmail: String,
    nickname: String,
    password1: String,
    password2: String,
    routeAction: RouteAction,
    context: Context,
) {

    if (!(password1.length >= 8 && password2.length >= 8)) {
        Log.d("FAIL", "비밀번호 8자리 이상이 아닙니다.")
    }

    if (!(password1.equals(password2))) {
        Log.d("FAIL", "비밀번호가 일치하지 않습니다")
    } else {

        var registerResponse: RegisterResponse? = null

        val okHttpClient: OkHttpClient by lazy {
            val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
        }

        var retrofit =
            Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/").client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()

        var registerRequest: RegisterRequest = retrofit.create(RegisterRequest::class.java)

        registerRequest.requestRegister(
            com.example.todo_android.Data.Profile.Register(authEmail, nickname, password2)
        ).enqueue(object : Callback<RegisterResponse> {

            // 실패 했을때
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("REGISTER", t.message.toString())
            }

            // 성공 했을때
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>,
            ) {

                registerResponse = response.body()

                when (registerResponse?.resultCode) {

                    "200" -> {

                        val defaultProfileImageBitmap = BitmapFactory.decodeResource(
                            context.resources, R.drawable.defaultprofile
                        )
                        val outputStream = ByteArrayOutputStream()
                        defaultProfileImageBitmap.compress(
                            Bitmap.CompressFormat.PNG, 100, outputStream
                        )
                        val imageBytes = outputStream.toByteArray()
                        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                        MyApplication.prefs.setData("nickname", nickname)
                        MyApplication.prefs.setData("password1", password1)
                        MyApplication.prefs.setData("password2", password2)
                        MyApplication.prefs.setData("image", encodedImage)

                        goLottie(NAV_ROUTE.LOTTIE, routeAction)
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    val context = LocalContext.current

    var authEmail: String = MyApplication.prefs.getData("email", "")

    val passwordPattern =
        Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,15}\$")

    var showErrorPassword1 by remember { mutableStateOf(false) }
    var showMatchPassword by remember { mutableStateOf(false) }

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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.ThirdPageNumber),
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xff9E9E9E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.FirstRegisterText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(id = R.string.SecondRegisterText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

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

                Spacer(modifier = Modifier.height(8.dp))

                TextField(modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                    value = MyApplication.prefs.getData("email", ""),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xffE9E9E9),
                        disabledLabelColor = Color(0xffE9E9E9),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    readOnly = true,
                    shape = RoundedCornerShape(8.dp),
                    onValueChange = {
                        email = it
                    })

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowNicknameText),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    value = nickname,
                    onValueChange = {
                        nickname = it
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Light
                    ),
                    decorationBox = { innerTextField ->
                        if (nickname.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.ShowNicknamePlaceholder),
                                fontSize = 15.sp,
                                lineHeight = 19.sp,
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
                                        color = Color(0xff9E9E9E),
                                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                            Spacer(modifier = Modifier.padding(vertical = 14.dp))
                        }
                    })


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowPasswordText),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    value = password1,
                    onValueChange = {
                        password1 = it
                        showErrorPassword1 = !it.matches(passwordPattern)
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Light
                    ),
                    decorationBox = { innerTextField ->
                        if (password1.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.ShowPasswordPlaceholder),
                                fontSize = 15.sp,
                                lineHeight = 19.sp,
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
                                        color = Color(0xff9E9E9E),
                                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                            Spacer(modifier = Modifier.padding(vertical = 14.dp))
                        }
                    })

                Spacer(modifier = Modifier.height(8.dp))

                if (showErrorPassword1) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.UnderPassword),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color(0xffFF9D4D)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.CheckPasswordText),
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    value = password2,
                    onValueChange = {
                        password2 = it
                        showMatchPassword = (it != password1)
                    },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Light
                    ),
                    decorationBox = { innerTextField ->
                        if (password2.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.ShowPasswordPlaceholder),
                                fontSize = 15.sp,
                                lineHeight = 19.sp,
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
                                        color = Color(0xff9E9E9E),
                                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                }, verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                            Spacer(modifier = Modifier.padding(vertical = 14.dp))
                        }
                    })

                Spacer(modifier = Modifier.height(8.dp))

                if (showMatchPassword) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.NoMatchPassword),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = Color(0xffFF9D4D)
                    )

                }

                Spacer(modifier = Modifier.height(60.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "")

                    IconButton(modifier = Modifier.size(43.dp),
                        colors = IconButtonDefaults.iconButtonColors(buttonColor),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                Register(
                                    authEmail, nickname, password1, password2, routeAction, context
                                )
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
}