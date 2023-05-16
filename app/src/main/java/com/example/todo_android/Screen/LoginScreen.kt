package com.example.todo_android.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.Data.Profile.Login
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.LoginRequest
import com.example.todo_android.Response.ProfileResponse.LoginResponse
import com.example.todo_android.Response.TodoResponse.ReadTodoResponse
import com.example.todo_android.Util.MyApplication
import com.example.todo_android.ui.theme.nextButtonColor
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
fun sendLogin(
    email: String,
    password: String,
    routeAction: RouteAction,
    response: (LoginResponse?) -> Unit,
) {

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
                    MyApplication.prefs.setData("email", loginResponse?.email.toString())
                    MyApplication.prefs.setData("nickname", loginResponse?.nickname.toString())
                    MyApplication.prefs.setData("image", loginResponse?.image.toString())

                    Log.d("LOGIN", "resultCode : " + loginResponse?.resultCode)
                    Log.d("LOGIN", "token : " + loginResponse?.token)
                    Log.d("LOGIN", "nickname : " + loginResponse?.nickname)
                    Log.d("LOGIN", "email : " + loginResponse?.email)
                    Log.d("LOGIN", "image : " + loginResponse?.image)
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    var openDialog by remember { mutableStateOf(false) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 41.dp, end = 41.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {

        Image(
            modifier = Modifier.size(117.dp),
            painter = painterResource(id = R.drawable.apptitle),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Image(
            modifier = Modifier
                .width(94.dp)
                .height(28.dp),
            painter = painterResource(id = R.drawable.appname),
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
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.InpurtEmail),
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9),
                    fontWeight = FontWeight.Medium
                )
            },
            trailingIcon = {
                if (email.isNotEmpty()) {
                    IconButton(onClick = {
                        email = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
            }
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
            onValueChange = {
                password = it
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.InpurtPassword),
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9),
                    fontWeight = FontWeight.Medium
                )
            },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    IconButton(onClick =
                    {
                        passwordVisible = !passwordVisible
                    })
                    {
                        Icon(
                            painter = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                checked = !checked
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Image(
                painter = onCheck,
                contentDescription = null)

            Spacer(modifier = Modifier.width(3.dp))

            Text(
                modifier = Modifier
                    .wrapContentWidth(),
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
            onClick =
            {
                if(email == "" || password == ""){
                    openDialog = true
                }else{
                    sendLogin(email, password, routeAction, response = {
                        openDialog = true
                    })
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
            Text(
                text = stringResource(id = R.string.Search),
                fontSize = 14.sp,
                color = Color(0xFF999999),
                modifier = Modifier.padding(end = 10.dp)
                    .clickable {
                    routeAction.navTo(NAV_ROUTE.SEARCHPASSWORD)
                }
            )

            Text(
                text = stringResource(id = R.string.Register),
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 7.dp)
                    .clickable {
                        goAuthEmail(NAV_ROUTE.AUTHEMAIL, routeAction)
                    }
            )
        }
    }
}

@Composable
fun FailureLoginDialog(onDismissRequest: () -> Unit) {

    Dialog(
        onDismissRequest = { onDismissRequest }) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .width(290.dp)
                    .height(140.dp)
                    .padding(
                        start = 19.dp,
//                        end = 10.dp,
//                        top = 19.dp,
//                        bottom = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "로그인 실패",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth().padding(bottom = 12.dp),
                    text = "이메일 혹은 비밀번호를 다시 확인해 주세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )

//                Spacer(modifier = Modifier.height(35.dp))

                Button(
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .padding(start = 14.dp, top = 7.dp, end = 14.dp, bottom = 7.dp)
                        .align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(nextButtonColor),
                ) {
                    Text(
                        text = "확인",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}