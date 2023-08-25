package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.AuthEmailRequest
import com.example.todo_android.Response.ProfileResponse.AuthEmailResponse
import com.example.todo_android.Util.MyApplication
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun authEmail(email: String, routeAction: RouteAction, response: (AuthEmailResponse?) -> Unit) {

    var authEmailResponse: AuthEmailResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
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
                    routeAction.navTo(NAV_ROUTE.AUTHCODE)
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

    var isButtonClickable by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    val ButtonColor = if (emailPattern.matches(email)) {
        Color(0xFFFFDAB9).copy(alpha = 1f)
    } else {
        Color(0xFFFFDAB9).copy(alpha = 0.5f)
    }

    val NextButtonArrowTint = if (emailPattern.matches(email)) {
        1f
    } else {
        0.5f
    }

    if (emailPattern.matches(email)) {
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
                        authEmail(email, routeAction, response = {
                            showErrorText = true
                        })
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
                .background(Color.White)
                .padding(start = 25.dp, end = 25.dp, top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.FirstPageNumber),
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.FirstAuthEmailText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = stringResource(id = R.string.SecondAuthEmailText),
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
                    }, value = email, onValueChange = {
                    email = it
                    showErrorText = false
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
                        value = email,
                        innerTextField = it,
                        singleLine = true,
                        enabled = true,
                        interactionSource = remember { MutableInteractionSource() },
                        contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                            start = 0.dp, top = 0.dp, bottom = 0.dp
                        ),
                        visualTransformation = VisualTransformation.None,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.ShowEmailPlaceholder),
                                fontSize = 18.sp,
                                lineHeight = 23.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(0xffD3D3D3),
                            )
                        },
                        trailingIcon = {
                            if (emailPattern.matches(email)) {
                                Icon(
                                    modifier = Modifier.offset(x = 12.dp),
                                    painter = painterResource(id = R.drawable.checkemail),
                                    contentDescription = null,
                                    tint = Color(0xffFF9D4D)
                                )
                            }
                        })
                }

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
        }
    }
}