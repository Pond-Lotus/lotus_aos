package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.AuthEmailRequest
import com.example.todo_android.Response.ProfileResponse.AuthEmailResponse
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.backButtonColor
import com.example.todo_android.ui.theme.nextButtonColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goAuthCode(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun authEmail(email: String, routeAction: RouteAction) {

    var authEmailResponse: AuthEmailResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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
                    Log.d("AUTHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                }
            }
        }
    })
}


@ExperimentalMaterial3Api
@Composable
fun AuthEmailScreen(routeAction: RouteAction) {
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

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.ShowEmailText),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff808080)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xff4D000000),
                    unfocusedIndicatorColor = Color(0xff4D000000)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(20.dp),
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ShowEmailPlaceholder),
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xffD3D3D3),
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.CantUseEmail),
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
                    .height(38.dp),
                colors = ButtonDefaults.buttonColors(backButtonColor),
                onClick = { routeAction.goBack() },
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "< 이전",
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
                onClick = { authEmail(email, routeAction) },
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "다음 >",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp
                )
            }
        }
    }
}