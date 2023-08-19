package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Profile.AuthCode
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.AuthCodeRequest
import com.example.todo_android.Response.ProfileResponse.AuthCodeResponse
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

fun authCode(
    authEmail: String, code: String, routeAction: RouteAction, response: (AuthCodeResponse?) -> Unit
) {

    var authCodeResponse: AuthCodeResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http://34.22.73.14:8000/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var authCodeRequest: AuthCodeRequest = retrofit.create(AuthCodeRequest::class.java)

    authCodeRequest.requestCode(AuthCode(authEmail, code)).enqueue(object : Callback<AuthCodeResponse> {

            //실패할 경우
            override fun onFailure(call: Call<AuthCodeResponse>, t: Throwable) {
                Log.e("authCode", t.message.toString())
            }

            //성공할 경우
            override fun onResponse(
                call: Call<AuthCodeResponse>,
                response: Response<AuthCodeResponse>,
            ) {
                authCodeResponse = response.body()

                when (authCodeResponse?.resultCode) {
                    "200" -> {
                        Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                        Log.d("AUTHCODE", "프로필 작성 화면으로 갑니다.")
                        routeAction.navTo(NAV_ROUTE.REGISTER)
                    }
                    "500" -> {
                        response(authCodeResponse)
                        Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                    }
                }
            }
        })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun AuthCodeScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }
    var authEmail = MyApplication.prefs.getData("email", "")
    var code by remember { mutableStateOf("") }

    var showErrorText by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()

    var isButtonClickable by remember { mutableStateOf(false) }

    val ButtonColor = if (code != "") {
        Color(0xFFFFDAB9).copy(alpha = 1f)
    } else {
        Color(0xFFFFDAB9).copy(alpha = 0.5f)
    }

    if (code != "") {
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
            shape = CircleShape, onClick = {
                if (isButtonClickable) {
                    scope.launch {
                        authCode(authEmail, code, routeAction, response = {
                            showErrorText = true
                        })
                    }
                }
            }, containerColor = ButtonColor
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null)
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
                    text = stringResource(id = R.string.SecondPageNumber),
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.FirstAuthCodeText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
                )
                Text(
                    text = stringResource(id = R.string.SecondAuthCodeText),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Black,
                )
            }

            Spacer(modifier = Modifier.height(38.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.ShowCodeText),
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff808080)
                )

                Spacer(modifier = Modifier.height(10.dp))

                BasicTextField(modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = code,
                    onValueChange = { input ->
                        if (input.length <= 6) {
                            code = input
                            showErrorText = false
                        }
                    },
                    decorationBox = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            code.forEachIndexed { index, c ->
                                EachTextFieldContainer(
                                    text = c.toString(), isFocused = index == code.lastIndex
                                )
                            }
                            repeat(6 - code.length) {
                                EachTextFieldContainer(
                                    text = ' '.toString(),
                                    isFocused = false,
                                )
                            }
                        }
                    })

                Spacer(modifier = Modifier.height(8.dp))

                if (showErrorText) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.CantUseCode),
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

@Composable
fun EachTextFieldContainer(
    text: String,
    isFocused: Boolean,
) {
    Box(
        modifier = Modifier.width(41.dp).height(53.dp)
            .background(Color(0xffe9e9e9), shape = RoundedCornerShape(10.dp)).run {
                if (isFocused) {
                    border(
                        width = 3.dp, color = Color(0xffFFBE3C7), shape = RoundedCornerShape(10.dp)
                    )
                } else {
                    this
                }
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, fontSize = 22.sp, fontWeight = FontWeight.Medium
        )
    }
}