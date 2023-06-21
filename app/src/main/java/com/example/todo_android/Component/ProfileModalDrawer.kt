package com.example.todo_android.Component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.LogoutRequest
import com.example.todo_android.Response.ProfileResponse.LogoutResponse
import com.example.todo_android.Util.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Logout(
    token: String, response: (LogoutResponse?) -> Unit
) {
    var logoutResponse: LogoutResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
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
    bottomScaffoldState: BottomSheetScaffoldState,
    routeAction: RouteAction,
) {

    val nickname: String = MyApplication.prefs.getData("nickname", "")
    val email: String = MyApplication.prefs.getData("email", "")

    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        showLogOutDialog(onDismissRequest = { openDialog = false }, routeAction)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 17.dp, end = 17.dp, top = 73.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 21.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.size(41.dp),
                painter = painterResource(id = R.drawable.defaultprofile),
                contentDescription = "profile",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = nickname,
                    fontSize = 14.sp,
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

            IconButton(modifier = Modifier.background(Color.White), onClick = {
                scope.launch {
                    bottomScaffoldState.drawerState.close()
                }
                routeAction.navTo(NAV_ROUTE.PROFILE)
            }) {
                Icon(
                    modifier = Modifier
                        .background(Color.White)
                        .size(24.dp)
                        .padding(start = 5.dp),
                    painter = painterResource(id = R.drawable.ic_pen),
                    contentDescription = null
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 21.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 20.dp),
            text = "환경 설정",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 17.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 22.dp)
                .clickable {
                    routeAction.navTo(NAV_ROUTE.CHANGEPASSWORD)
                }, verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null
            )

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "비밀번호 변경",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, bottom = 22.dp)
                .clickable {}, verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null
            )

            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "알림 설정",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp),
                text = "그룹 설정",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )

            IconButton(onClick = {
                routeAction.navTo(NAV_ROUTE.SELECTCATEGORY)
            }) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = null
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                onClick = {},
                content = {})
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 320.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )

        Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xffe9e9e9), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .clickable {
                    openDialog = true
                }, verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = R.drawable.logout),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                text = "로그아웃",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
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
                    modifier = Modifier.padding(top = 35.dp, bottom = 12.dp),
                    text = "로그아웃",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    modifier = Modifier.padding(bottom = 38.dp),
                    text = "로그아웃 하시겠습니까?",
                    fontSize = 12.sp,
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