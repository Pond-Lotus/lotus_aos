package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

fun SearchPassword(email: String, routeAction: RouteAction ,response: (SearchEmailResponse?) -> Unit){

    var authEmailResponse: SearchEmailResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("`http://34.22.73.14:8000/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var searchEmailRequest: SearchEmailRequest = retrofit.create(SearchEmailRequest::class.java)

    searchEmailRequest.requestEmail(email).enqueue(object : Callback<SearchEmailResponse> {

        //성공할 경우
        override fun onResponse(
            call: Call<SearchEmailResponse>,
            response: Response<SearchEmailResponse>,
        ) {
            authEmailResponse = response.body()

            when(authEmailResponse?.resultCode){
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


    val color = if (emailPattern.matches(email)) {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "비밀번호 찾기",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp)
        }, navigationIcon = {
            IconButton(onClick = {
                routeAction.goBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)) {

            Spacer(modifier = Modifier.padding(vertical = 41.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 13.dp), verticalAlignment = Alignment.CenterVertically) {

                Image(painter = painterResource(id = R.drawable.sms), contentDescription = null)

                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                    text = "안내드려요",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp)
            }



            Text(modifier = Modifier.fillMaxWidth(),
                text = "가입한 이메일 주소를 입력해주세요.",
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                lineHeight = 18.sp)
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 27.dp),
                text = "해당 이메일로 비밀번호 재설정을 위한 링크를 보내드립니다.",
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                lineHeight = 18.sp)

            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 27.dp),
                color = Color(0xffE9E9E9),
                thickness = 1.dp)

            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 7.dp),
                text = "이메일",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp)

            BasicTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
                value = email,
                onValueChange = {
                    email = it
                    showErrorText = false },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD0D0D0),
                                shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(horizontal = 2.dp, vertical = 12.dp), // inner padding
                    ) {
                        innerTextField()
                    }
                })

            if (showErrorText) {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                    text = "유효한 이메일이 아닙니다.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xffFF9D4D))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(color),
                onClick = {
                          if(isButtonClickable == true){
                              scope.launch {
                                  SearchPassword(email, routeAction, response = {
                                      if(it?.resultCode == "200"){
                                          openDialog = true
                                      } else{
                                          showErrorText = true
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
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun showDialog(onDismissRequest: () -> Unit, routeAction: RouteAction) {
    Dialog(onDismissRequest = { onDismissRequest }) {
        Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(modifier = Modifier.padding(top = 27.dp, bottom = 11.dp),
                    text = "메일 발송 완료",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold)

                Text(
                    text = "재설정한 비밀번호로",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light)

                Text(modifier = Modifier.padding(bottom = 29.dp),
                    text = "로그인 해주세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light)

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround) {
                    androidx.compose.material.TextButton(modifier = Modifier
                        .background(buttonColor)
                        .weight(1f),
                        onClick = {
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