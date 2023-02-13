package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Profile.Login
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.LoginRequest
import com.example.todo_android.Response.ProfileResponse.LoginResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun goCalendar(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun goAuthEmail(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

@ExperimentalMaterial3Api
fun sendLogin(email: String, password: String, routeAction: RouteAction) {

    var loginResponse: LoginResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var loginRequest: LoginRequest = retrofit.create(LoginRequest::class.java)

    loginRequest.requestLogin(Login(email, password)).enqueue(object : Callback<LoginResponse> {

        //실패할 경우
        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.e("LOGIN", t.message.toString())
        }

        //성공할 경우
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            loginResponse = response.body()

            when (loginResponse?.resultCode) {
                "200" -> {
                    goCalendar(NAV_ROUTE.CALENDAR, routeAction)
                    MyApplication.prefs.setData("token", loginResponse?.token.toString())

                    Log.d("LOGIN", "resultCode : " + loginResponse?.resultCode)
                    Log.d("LOGIN", "token : " + loginResponse?.token)
                    Log.d("LOGIN", "nickname : " + loginResponse?.nickname)
                    Log.d("LOGIN", "email : " + loginResponse?.email)
                    Log.d("LOGIN", "image : " + loginResponse?.image)
                    Log.d("LOGIN", "메인 화면으로 갑니다.")
                }
                "500" -> {
                    Log.d("LOGIN", "non_field_errors:[Check Your Email or Password]")
                }
            }
        }
    })
}

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(routeAction: RouteAction) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var checked by remember { mutableStateOf(false) }

        var passwordVisible by remember { mutableStateOf(false) }

        val icon = if (passwordVisible) {
            painterResource(id = R.drawable.openeye)
        } else {
            painterResource(id = R.drawable.closeeye)
        }


        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 30.dp),
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ToDo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
            )
            Text(
                text = "토도토도리",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 31.sp,
            )
            Text(
                text = "어쩌구 저쩌구 어플 카피라이팅",
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 19.sp,
            )
        }

        Spacer(modifier = Modifier.height(72.dp))

        TextField(
            modifier = Modifier
                .width(308.dp)
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
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(
                    text = "이메일 입력",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            },
            trailingIcon =
            {
                IconButton(onClick = {
                    email = ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "ClearIcon"
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            modifier = Modifier
                .width(308.dp)
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
            onValueChange = {
                password = it
            },
            placeholder = {
                Text(
                    text = "비밀번호 입력",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            },
            trailingIcon = {

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick =
                {
                    passwordVisible = !passwordVisible
                })
                {
                    Icon(
                        painter = icon,
                        contentDescription = "EyeIcon"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
                modifier = Modifier.padding(start = 3.dp)
            )

//            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "자동 로그인",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        Button(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(Color(0xffFFBE3C7)),
            onClick =
            {
                sendLogin(email, password, routeAction)
            },
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                text = "로그인",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row() {
            Text(
                text = "이메일 / 비밀번호 찾기",
                fontSize = 14.sp,
                color = Color(0xFF999999),
                modifier = Modifier.padding(end = 10.dp)
            )

            Spacer(modifier = Modifier.width(122.dp))

            Text(
                text = "회원 가입",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 7.dp).clickable {
                    goAuthEmail(NAV_ROUTE.AUTHEMAIL, routeAction)
                }
            )
        }
    }
}