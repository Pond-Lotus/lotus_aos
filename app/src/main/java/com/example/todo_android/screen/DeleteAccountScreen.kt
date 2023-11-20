package com.example.todo_android.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.R
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.navigation.NAV_ROUTE
import com.example.todo_android.request.ProfileRequest.DeleteAccountRequest
import com.example.todo_android.response.ProfileResponse.DeleteAccountResponse
import com.example.todo_android.util.MyApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun deleteAccount(token: String, response: (DeleteAccountResponse?) -> Unit) {
    var deleteAccountResponse: DeleteAccountResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var deleteAccountRequest: DeleteAccountRequest =
        retrofit.create(DeleteAccountRequest::class.java)

    deleteAccountRequest.requestDeleteTodo(token).enqueue(object : Callback<DeleteAccountResponse> {
        override fun onResponse(
            call: Call<DeleteAccountResponse>,
            response: Response<DeleteAccountResponse>,
        ) {
            deleteAccountResponse = response.body()
            response(deleteAccountResponse)
            Log.d("deleteAccount", "data : " + deleteAccountResponse?.resultCode)
        }

        override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
            Log.e("deleteAccount", t.message.toString())
        }

    })

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun DeleteAccountScreen(routeAction: RouteAction) {
    val nickname: String = MyApplication.prefs.getData("nickname", "")
    val email: String = MyApplication.prefs.getData("email", "")

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var openDialog by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(false) }

    var isButtonClickable by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    val onCheck = if (checked) {
        isButtonClickable = true
        painterResource(id = R.drawable.dncheck)
    } else {
        isButtonClickable = false
        painterResource(id = R.drawable.dcheck)
    }

    val buttonColor = if (checked) {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    val buttonTextColor = if (checked) {
        Color.Black
    } else{
        Color(0xFF9E9E9E)
    }

    val textColor = if (checked) {
        Color.Black
    } else{
        Color(0xFF9E9E9E)
    }


    if (openDialog) {
        showDeleteDialog(onDismissRequest = {
            openDialog = false
        }, routeAction)
    }

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .height(45.dp)
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
                    text = "계정 탈퇴",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }, navigationIcon = {
                IconButton(onClick = {
//                    routeAction.navTo(NAV_ROUTE.PROFILE)
                    routeAction.goBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            })
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 37.dp, end = 37.dp)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 41.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp),
                text = "계정 정보",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp),
                colors = CardDefaults.cardColors(Color(0xffF4F4F4)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp, bottom = 11.dp, start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(41.dp),
                        painter = painterResource(id = R.drawable.defaultprofile),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = nickname,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = email,
                            color = Color(0xff9E9E9E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.info_circle),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp),
                    text = "탈퇴 전 안내드려요",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 21.sp,
                    color = Color(0xffFF9D4D)
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 7.dp))

            Text(text = "계정 탈퇴 시 모든 정보와 데이터가 삭제됩니다.", fontSize = 14.sp, lineHeight = 21.sp)
            Text(text = "복구 및 백업이 불가능하오니, 신중히 생각해 주세요.", fontSize = 14.sp, lineHeight = 21.sp)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                color = Color(0xffE9E9E9),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 19.dp, bottom = 41.dp)
                    .clickable {
                        checked = !checked
                        isButtonClickable = !isButtonClickable
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = onCheck,
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp, bottom = 1.dp),
                    text = "안내사항을 모두 확인하였으며, 탈퇴를 진행합니다.",
                    fontSize = 13.sp,
                    color = textColor
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(buttonColor),
                onClick = {
                    if (isButtonClickable == true) {
                        openDialog = true
                    }
                },
                enabled = isButtonClickable,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "계정 탈퇴하기",
                    color = buttonTextColor,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun showDeleteDialog(onDismissRequest: () -> Unit, routeAction: RouteAction) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    Dialog(onDismissRequest = { onDismissRequest }) {
        Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 28.dp, bottom = 11.dp),
                    text = "정말 떠나시나요? \uD83D\uDE22",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    modifier = Modifier.padding(bottom = 28.dp),
                    text = "다음에 또 만나길 기대할게요.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material.TextButton(modifier = Modifier
                        .background(
                            Color(
                                0xffE9E9E9
                            )
                        )
                        .weight(1f), onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = "취소", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    androidx.compose.material.TextButton(modifier = Modifier
                        .background(
                            Color(
                                0xffFFDAB9
                            )
                        )
                        .weight(1f), onClick = {
                        deleteAccount(token, response = {
                            MyApplication.prefs.setData("email", "")
                            MyApplication.prefs.setData("password1", "")
                            routeAction.navTo(NAV_ROUTE.LOGIN)
                        })
                        onDismissRequest()
                    }) {
                        Text(text = "확인", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}