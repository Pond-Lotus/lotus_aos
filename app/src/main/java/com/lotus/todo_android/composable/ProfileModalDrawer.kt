package com.lotus.todo_android.composable

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction
import com.lotus.todo_android.navigation.NAV_ROUTE
import com.lotus.todo_android.request.ProfileRequest.LogoutRequest
import com.lotus.todo_android.response.ProfileResponse.LogoutResponse
import com.lotus.todo_android.util.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Logout(
    token: String, response: (LogoutResponse?) -> Unit
) {
    var logoutResponse: LogoutResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl(" https://team-lotus.kr/ ").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var logoutRequest: LogoutRequest = retrofit.create(LogoutRequest::class.java)

    logoutRequest.requestLogout(token).enqueue(object : Callback<LogoutResponse> {
        override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
            logoutResponse = response.body()

            when (logoutResponse?.resultCode) {
                200 -> {
                    Log.d("logout", "data : " + logoutResponse?.resultCode)
                    MyApplication.prefs.setData("email", "")
                    MyApplication.prefs.setData("password1", "")
                    response(logoutResponse)
                }
                500 -> {
                    Log.d("logout", "data : " + logoutResponse?.resultCode)
                }
            }
        }

        override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
            Log.e("logout", t.message.toString())
        }
    })
}

@ExperimentalMaterialApi
@Composable
fun ProfileModalDrawer(
    scope: CoroutineScope,
    routeAction: RouteAction,
) {

    val nickname: String = MyApplication.prefs.getData("nickname", "")
    val email: String = MyApplication.prefs.getData("email", "")
    val context = LocalContext.current

    var openDialog by remember { mutableStateOf(false) }

    val imageResource = if (MyApplication.prefs.getData("image", "") == "null") {
        BitmapFactory.decodeResource(context.resources, R.drawable.defaultprofile).asImageBitmap()
    } else {
        val base64EncodedImage = MyApplication.prefs.getData("image", "")
        val DecodedBytes = Base64.decode(base64EncodedImage, Base64.DEFAULT)
        val ImageBitmap = BitmapFactory.decodeByteArray(DecodedBytes, 0, DecodedBytes.size)

        ImageBitmap.asImageBitmap()
    }


    if (openDialog) {
        showLogOutDialog(onDismissRequest = { openDialog = false }, routeAction)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 22.dp, end = 22.dp, top = 70.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 21.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(41.dp)
                    .clip(CircleShape),
                bitmap = imageResource,
                contentDescription = "profile",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = nickname,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp
                )
                Text(
                    text = email,
                    color = Color(0xff9E9E9E),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }

            Image(
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        scope.launch {
                            routeAction.navTo(NAV_ROUTE.PROFILE)
                        }
                    },
                painter = painterResource(id = R.drawable.ic_pen),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xFF727272))
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 18.dp),
            text = "환경 설정",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 17.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 18.dp)
                .clickable {
                    scope.launch {
                        routeAction.navTo(NAV_ROUTE.CHANGEPASSWORD)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null
            )

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "비밀번호 변경",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 17.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 22.dp)
                .clickable {
                    scope.launch {
                        routeAction.navTo(NAV_ROUTE.SETALARM)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.notification),
                contentDescription = null
            )

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "알림 설정",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 17.sp
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 22.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )

        Column(modifier = Modifier
            .wrapContentSize()
            .clickable {
                scope.launch {
                    routeAction.navTo(NAV_ROUTE.SELECTCATEGORY)
                }
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp),
                    text = "그룹 설정",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 17.sp
                )

                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color(0xFF727272))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                    onClick = {},
                    content = {})

                Spacer(modifier = Modifier.weight(1f))

                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                    onClick = {},
                    content = {})

                Spacer(modifier = Modifier.weight(1f))

                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                    onClick = {},
                    content = {})

                Spacer(modifier = Modifier.weight(1f))

                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                    onClick = {},
                    content = {})

                Spacer(modifier = Modifier.weight(1f))

                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                    onClick = {},
                    content = {})

                Spacer(modifier = Modifier.weight(1f))

                Button(modifier = Modifier.size(width = 26.dp, height = 26.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                    onClick = {},
                    content = {})
            }

            Divider(
                modifier = Modifier.fillMaxWidth(), color = Color(0xffe9e9e9), thickness = 1.dp
            )
        }



        Spacer(modifier = Modifier.weight(1f))

        Divider(
            modifier = Modifier.fillMaxWidth(), color = Color(0xffe9e9e9), thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp, bottom = 21.dp)
                .clickable {
                    openDialog = true
                }, verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.logout),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                text = "로그아웃",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun showLogOutDialog(onDismissRequest: () -> Unit, routeAction: RouteAction) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    Dialog(onDismissRequest = { onDismissRequest }) {
        androidx.compose.material3.Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(265.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(top = 35.dp, bottom = 10.dp),
                    text = "로그아웃",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    modifier = Modifier.padding(bottom = 38.dp),
                    text = "로그아웃 하시겠습니까?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(
                        modifier = Modifier
                            .background(Color(0xffE9E9E9))
                            .weight(1f),
                        onClick = {
                            onDismissRequest()
                        }) {
                        Text(text = "취소", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        modifier = Modifier
                            .background(Color(0xffFFDAB9))
                            .weight(1f),
                        onClick = {
                            Logout(token, response = {
                                onDismissRequest()
                                routeAction.navTo(NAV_ROUTE.LOGIN)
                            })
                        }) {
                        Text(text = "로그아웃", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}