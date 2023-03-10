package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.todo_android.ui.theme.backButtonColor
import com.example.todo_android.ui.theme.nextButtonColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun goProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun authCode(authEmail: String, code: String, routeAction: RouteAction) {

    var authCodeResponse: AuthCodeResponse? = null


    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var authCodeRequest: AuthCodeRequest = retrofit.create(AuthCodeRequest::class.java)

    authCodeRequest.requestCode(AuthCode(authEmail, code))
        .enqueue(object : Callback<AuthCodeResponse> {

            //????????? ??????
            override fun onFailure(call: Call<AuthCodeResponse>, t: Throwable) {
                Log.e("authCode", t.message.toString())
            }

            //????????? ??????
            override fun onResponse(
                call: Call<AuthCodeResponse>,
                response: Response<AuthCodeResponse>,
            ) {
                authCodeResponse = response.body()

                when (authCodeResponse?.resultCode) {
                    "200" -> {
                        Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                        Log.d("AUTHCODE", "????????? ?????? ???????????? ?????????.")
                        goProfile(NAV_ROUTE.REGISTER, routeAction)
                    }
                    "500" -> {
                        Log.d("AUTHCODE", "resultCode : " + authCodeResponse?.resultCode)
                    }
                }
            }
        })
}

@ExperimentalMaterial3Api
@Composable
fun AuthCodeScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 25.dp, end = 25.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var email by remember { mutableStateOf("") }
        var authEmail = MyApplication.prefs.getData("email", email)
        var code by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.SecondPageNumber),
                fontSize = 20.sp,
                lineHeight = 29.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xff9E9E9E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.FirstAuthCodeText),
                fontSize = 28.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = R.string.SecondAuthCodeText),
                fontSize = 28.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.ShowCodeText),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff808080)
            )

            Spacer(modifier = Modifier.height(10.dp))

            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = code,
                onValueChange = { input ->
                    if (input.length <= 6) {
                        code = input
                    }
                },
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween)
                    {
                        code.forEachIndexed { index, c ->
                            EachTextFieldContainer(
                                text = c.toString(),
                                isFocused = index == code.lastIndex
                            )
                        }
                        repeat(6 - code.length) {
                            EachTextFieldContainer(
                                text = ' '.toString(),
                                isFocused = false,
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(13.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.CantUseCode),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xffE47979)
            )
        }

        Spacer(modifier = Modifier.height(250.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .height(38.dp)
                    .background(Color.White),
                colors = ButtonDefaults.buttonColors(backButtonColor),
                onClick = { routeAction.goBack() },
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "< ??????",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp
                )
            }

            Button(
                modifier = Modifier
                    .width(90.dp)
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(nextButtonColor),
                onClick = { authCode(authEmail, code, routeAction) },
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "?????? >",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp
                )
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
        modifier = Modifier
            .width(41.dp)
            .height(53.dp)
            .background(Color(0xffe9e9e9), shape = RoundedCornerShape(10.dp))
            .run {
                if (isFocused) {
                    border(
                        width = 3.dp,
                        color = Color(0xffFFBE3C7),
                        shape = RoundedCornerShape(10.dp)
                    )
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}