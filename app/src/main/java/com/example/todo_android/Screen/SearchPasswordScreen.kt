package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.SearchEmailRequest
import com.example.todo_android.Response.ProfileResponse.SearchEmailResponse
import com.example.todo_android.ui.theme.buttonColor
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun SearchPassword(
    email: String, routeAction: RouteAction, response: (SearchEmailResponse?) -> Unit
) {

    var authEmailResponse: SearchEmailResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var searchEmailRequest: SearchEmailRequest = retrofit.create(SearchEmailRequest::class.java)

    searchEmailRequest.requestEmail(email).enqueue(object : Callback<SearchEmailResponse> {

        //성공할 경우
        override fun onResponse(
            call: Call<SearchEmailResponse>,
            response: Response<SearchEmailResponse>,
        ) {
            authEmailResponse = response.body()

            when (authEmailResponse?.resultCode) {
                "200" -> {
                    response(authEmailResponse)
                    Log.d("SEARCHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                }
                "500" -> {
                    response(authEmailResponse)
                    Log.d("SEARCHEMAIL", "resultCode : " + authEmailResponse?.resultCode)
                }
            }
        }

        //실패할 경우
        override fun onFailure(call: Call<SearchEmailResponse>, t: Throwable) {
            Log.e("authEmail", t.message.toString())
        }
    })
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun SearchPasswordScreen(routeAction: RouteAction) {

    var email by remember {
        mutableStateOf("")
    }

    val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

    var showErrorText by remember { mutableStateOf(false) }

    var isButtonClickable by remember { mutableStateOf(false) }

    if (emailPattern.matches(email)) {
        isButtonClickable = true
    } else {
        isButtonClickable = false
    }

    var openDialog by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    if (openDialog) {
        showDialog(onDismissRequest = { openDialog = false }, routeAction)
    }


    val buttonColor = if (emailPattern.matches(email)) {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    val textColor = if (emailPattern.matches(email)) {
        Color.Black
    } else {
        Color(0xFF9E9E9E)
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .imePadding(), topBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color(0x26000000), // 기존에 사용 중이셨던 보더 컬러를 선택하세요.
                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx() // 보더 두께를 원하는 값으로 설정하세요.
                    )
                }) {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "비밀번호 찾기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    routeAction.goBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            })
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 90.dp, bottom = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        modifier = Modifier.size(17.dp),
                        painter = painterResource(id = R.drawable.sms),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp),
                        text = "안내드려요",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )
                }



                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "가입한 이메일 주소를 입력해주세요.",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    text = "해당 이메일로 비밀번호 재설정을 위한 링크를 보내드립니다.",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 27.dp),
                    color = Color(0xffE9E9E9),
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    text = "이메일",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, top = 13.dp, bottom = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                showErrorText = false
                            },
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Normal,
                                color = Color.Black,
                                lineHeight = 31.sp
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        )
                    }
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
                            .height(38.dp)
                    ) {
                        if (showErrorText) {
                            Text(
                                modifier = Modifier.align(Alignment.TopStart),
                                text = "유효한 이메일이 아닙니다.",
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xffFF9D4D)
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    onClick = {
                        if (isButtonClickable) {
                            scope.launch {
                                SearchPassword(email, routeAction, response = {
                                    when (it?.resultCode) {
                                        "200" -> {
                                            openDialog = true
                                        }
                                        "500" -> {
                                            showErrorText = true
                                        }
                                    }
                                })
                            }
                        }
                    },
                    enabled = isButtonClickable,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "비밀번호 찾기",
                        color = textColor,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun showDialog(onDismissRequest: () -> Unit, routeAction: RouteAction) {
    Dialog(onDismissRequest = { onDismissRequest }) {
        Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 28.dp, bottom = 11.dp),
                    text = "메일 발송 완료",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "재설정한 비밀번호로", fontSize = 15.sp, fontWeight = FontWeight.Light
                )

                Text(
                    modifier = Modifier.padding(bottom = 28.dp),
                    text = "로그인 해주세요.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material.TextButton(
                        modifier = Modifier
                            .background(buttonColor)
                            .weight(1f), onClick = {
                            onDismissRequest()
                            routeAction.navTo(NAV_ROUTE.LOGIN)
                        }) {
                        Text(text = "로그인", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}