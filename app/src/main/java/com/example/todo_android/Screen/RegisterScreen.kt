package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
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
            val httpLoInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
        }

        var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
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

                        routeAction.navTo(NAV_ROUTE.LOTTIE)
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

    var scope = rememberCoroutineScope()

    var isButtonClickable by remember { mutableStateOf(false) }

    val ButtonColor =
        if (nickname != "" && passwordPattern.matches(password1) && password1.equals(password2)) {
            Color(0xFFFFDAB9).copy(alpha = 1f)
        } else {
            Color(0xFFFFDAB9).copy(alpha = 0.5f)
        }

    val NextButtonArrowTint = if (nickname != "" && passwordPattern.matches(password1) && password1.equals(password2)) {
        1f
    } else {
        0.5f
    }

    if (nickname != "" && passwordPattern.matches(password1) && password1.equals(password2)) {
        isButtonClickable = true
    } else {
        isButtonClickable = false
    }

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
    }, floatingActionButton = {

        FloatingActionButton(
            modifier = Modifier.size(60.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            shape = CircleShape,
            onClick = {
                if (isButtonClickable) {
                    scope.launch {
                        Register(
                            authEmail, nickname, password1, password2, routeAction, context
                        )
                    }
                }
            },
            containerColor = ButtonColor
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.nextbuttonarrow),
                contentDescription = null,
                alpha = NextButtonArrowTint
            )
        }
    }, floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(start = 25.dp, end = 25.dp, top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.ThirdPageNumber),
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.FirstRegisterText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = stringResource(id = R.string.SecondRegisterText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
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
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color(0xffE9E9E9))
                ) {
                    Text(
                        text = MyApplication.prefs.getData("email", ""),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(alignment = Alignment.CenterStart)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowNicknameText),
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color(0xFFBFBFBF),
                            start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                            end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }, value = nickname, onValueChange = {
                    nickname = it
                }, singleLine = true, maxLines = 1, textStyle = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                        value = nickname,
                        innerTextField = it,
                        singleLine = true,
                        enabled = true,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            start = 0.dp, bottom = 0.dp
                        ),
                        visualTransformation = VisualTransformation.None,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.ShowNicknamePlaceholder),
                                fontSize = 14.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(0xffD3D3D3),
                            )
                        })
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowPasswordText),
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color(0xFFBFBFBF),
                            start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                            end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }, value = password1, onValueChange = {
                    password1 = it
                    showErrorPassword1 = !it.matches(passwordPattern)
                }, singleLine = true, maxLines = 1, textStyle = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                        value = password1,
                        innerTextField = it,
                        singleLine = true,
                        enabled = true,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            start = 0.dp, bottom = 0.dp
                        ),
                        visualTransformation = VisualTransformation.None,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.ShowPasswordPlaceholder),
                                fontSize = 14.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(0xffD3D3D3),
                            )
                        })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // TextField 아래의 레이아웃을 수정
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .height(28.dp)
                    ) {
                        if (showErrorPassword1) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopStart),
                                text = stringResource(id = R.string.UnderPassword),
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xffFF9D4D)
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.CheckPasswordText),
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color(0xFFBFBFBF),
                            start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                            end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }, value = password2, onValueChange = {
                    password2 = it
                    showMatchPassword = (it != password1)
                }, singleLine = true, maxLines = 1, textStyle = LocalTextStyle.current.copy(
                    fontSize = 18.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                        value = password2,
                        innerTextField = it,
                        singleLine = true,
                        enabled = true,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            start = 0.dp, bottom = 0.dp
                        ),
                        visualTransformation = VisualTransformation.None,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.ShowPasswordPlaceholder),
                                fontSize = 14.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(0xffD3D3D3),
                            )
                        })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // TextField 아래의 레이아웃을 수정
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .height(28.dp)
                    ) {
                        if (showMatchPassword) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.TopStart),
                                text = stringResource(id = R.string.UnderPassword),
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xffFF9D4D)
                            )
                        }
                    }
                }
            }
        }
    }
}