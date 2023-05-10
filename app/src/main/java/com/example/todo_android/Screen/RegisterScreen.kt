package com.example.todo_android.Screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.RegisterRequest
import com.example.todo_android.Response.ProfileResponse.RegisterResponse
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.backButtonColor
import com.example.todo_android.ui.theme.nextButtonColor
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

        var retrofit = Retrofit.Builder()
            .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var registerRequest: RegisterRequest = retrofit.create(RegisterRequest::class.java)

        registerRequest.requestRegister(
            com.example.todo_android.Data.Profile.Register(authEmail, nickname, password2))
            .enqueue(object : Callback<RegisterResponse> {

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

                            val defaultProfileImageBitmap =
                                BitmapFactory.decodeResource(context.resources,
                                    R.drawable.defaultprofile)
                            val outputStream = ByteArrayOutputStream()
                            defaultProfileImageBitmap.compress(Bitmap.CompressFormat.PNG,
                                100,
                                outputStream)
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

@ExperimentalMaterial3Api
@Composable
fun RegisterScreen(routeAction: RouteAction) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 25.dp, end = 25.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        var email by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }
        var password1 by remember { mutableStateOf("") }
        var password2 by remember { mutableStateOf("") }
        val context = LocalContext.current

        var authEmail: String = MyApplication.prefs.getData("email", "")

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

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.ShowNicknameText),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff808080)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = nickname,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xff4D000000),
                    unfocusedIndicatorColor = Color(0xff4D000000)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(20.dp),
                onValueChange = {
                    nickname = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ShowNicknamePlaceholder),
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xffD3D3D3),
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.ShowPasswordText),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff808080)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password1,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xff4D000000),
                    unfocusedIndicatorColor = Color(0xff4D000000)
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(20.dp),
                onValueChange = {
                    password1 = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ShowPasswordPlaceholder),
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xffD3D3D3),
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.UnderPassword),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xffE47979)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.CheckPasswordText),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff808080)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password2,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xff4D000000),
                    unfocusedIndicatorColor = Color(0xff4D000000)
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(20.dp),
                onValueChange = {
                    password2 = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ShowPasswordPlaceholder),
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xffD3D3D3),
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.NoMatchPassword),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = Color(0xffE47979)
            )

            Spacer(modifier = Modifier.height(60.dp))

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
                    onClick = {
                        Register(authEmail,
                            nickname,
                            password1,
                            password2,
                            routeAction,
                            context)
                    },
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
}