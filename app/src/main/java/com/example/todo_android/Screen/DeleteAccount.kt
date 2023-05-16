package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ProfileRequest.DeleteAccountRequest
import com.example.todo_android.Response.ProfileResponse.DeleteAccountResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun deleteAccount(token: String, routeAction: RouteAction){
    var deleteAccountResponse: DeleteAccountResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var deleteAccountRequest: DeleteAccountRequest = retrofit.create(DeleteAccountRequest::class.java)

    deleteAccountRequest.requestDeleteTodo(token).enqueue(object : Callback<DeleteAccountResponse> {
        override fun onResponse(
            call: Call<DeleteAccountResponse>,
            response: Response<DeleteAccountResponse>,
        ) {
            deleteAccountResponse = response.body()

            Log.d("deleteAccount", "data : " + deleteAccountResponse?.resultCode)
            routeAction.navTo(NAV_ROUTE.LOGIN)
        }

        override fun onFailure(call: Call<DeleteAccountResponse>, t: Throwable) {
            Log.e("deleteAccount", t.message.toString())
        }

    })

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun DeleteAccount(routeAction: RouteAction) {

    var checked by remember { mutableStateOf(false) }

    var isButtonClickable by remember { mutableStateOf(false) }

    val onCheck = if (checked) {
        isButtonClickable = true
        painterResource(id = R.drawable.dncheck)
    } else {
        isButtonClickable = false
        painterResource(id = R.drawable.dcheck)
    }

    val color = if (checked) {
        Color(0xffFFDAB9)
    } else {
        Color(0xffE9E9E9)
    }

    val nickname: String = MyApplication.prefs.getData("nickname", "")
    val email: String = MyApplication.prefs.getData("email", "")

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "계정 탈퇴", fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp)
        }, navigationIcon = {
            IconButton(onClick = {
                routeAction.goBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 37.dp, end = 37.dp)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 41.dp))

            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 7.dp),
                text = "계정 정보",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp)

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(63.dp),
                colors = CardDefaults.cardColors(Color(0xffF4F4F4)),
                shape = RoundedCornerShape(10.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 11.dp, bottom = 11.dp, start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(modifier = Modifier.size(41.dp),
                        painter = painterResource(id = R.drawable.defaultprofile),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop)

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start) {
                        Text(text = nickname,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 21.sp)
                        Text(text = email,
                            color = Color(0xff9E9E9E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp)
                    }
                }
            }

            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
                text = "탈퇴 전 안내드려요",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 21.sp,
                color = Color(0xffFF9D4D))

            Spacer(modifier = Modifier.padding(vertical = 7.dp))

            Text(text = "계정 탈퇴 시 모든 정보와 데이터가 삭제됩니다.", fontSize = 14.sp, lineHeight = 21.sp)
            Text(text = "복구 및 백업이 불가능하오니, 신중히 생각해 주세요.", fontSize = 14.sp, lineHeight = 21.sp)

            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
                color = Color(0xffE9E9E9),
                thickness = 1.dp)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 19.dp, bottom = 41.dp)
                .clickable {
                    checked = !checked
                    isButtonClickable = !isButtonClickable
                },
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(14.dp),
                    painter = onCheck,
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 9.dp),
                    text = "안내사항을 모두 확인하였으며, 탈퇴를 진행합니다.",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    lineHeight = 13.sp
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(color),
                onClick = {
                          if(isButtonClickable == true){
                              deleteAccount(token, routeAction)
                          }
                },
                enabled = isButtonClickable,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "계정 탈퇴하기",
                    color = Color.Black,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}