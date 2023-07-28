package com.example.todo_android.Screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.R

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
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(modifier = Modifier.padding(vertical = 36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    checked = checkAlarmState.value,
                    onCheckedChange = {
                        checkAlarmState.value = it
                    },
                    colors = androidx.compose.material.SwitchDefaults.colors(
                        uncheckedTrackColor = Color(0xffD4D4D4),
                        checkedThumbColor = Color(0xffFFC56D),
                        checkedTrackColor = Color(0xFFFFDAB9)
                    ),
                    enabled = false
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 21.dp),
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
                    modifier = Modifier
                        .align(Alignment.Center),
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