package com.lotus.todo_android.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun SetAlarmScreen(routeAction: RouteAction) {

    var checkAlarmState = remember {
        mutableStateOf(false)
    }


    Scaffold(modifier = Modifier
        .fillMaxSize()
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
                    text = "알림 설정",
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
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "알림 켜기 / 끄기",
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9E9E9E)
                    )

                    androidx.compose.material.Switch(
                        modifier = Modifier.offset(x = 7.dp),
                        checked = checkAlarmState.value,
                        onCheckedChange = {
                            checkAlarmState.value = it
                        },
                        colors = androidx.compose.material.SwitchDefaults.colors(
                            uncheckedTrackColor = Color(0xffDDDBDB),
                            checkedThumbColor = Color(0xffFFC56D),
                            checkedTrackColor = Color(0xFFFFDAB9)
                        ),
                        enabled = true
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    color = Color(0xffe9e9e9),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(size = 18.dp))
                        .background(Color(0xffEDEDED))
                        .align(Alignment.Start),
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(11.dp),
                            painter = painterResource(id = R.drawable.notialarm),
                            contentDescription = null
                        )

                        Text(
                            text = " 안내사항",
                            fontSize = 11.sp,
                            lineHeight = 16.22.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF9E9E9E),
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = "· 현재 알림 설정은 제공되지 않습니다. (추후 업데이트 예정)",
                    fontSize = 13.sp,
                    lineHeight = 19.17.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFC1C1C1),
                )

                Spacer(modifier = Modifier.padding(vertical = 3.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = "· 안드로이드 설정 > 알림 > 앱 설정 에서 설정해 주시기 바랍니다.",
                    fontSize = 13.sp,
                    lineHeight = 19.17.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFC1C1C1),
                )
            }
        }
    }
}