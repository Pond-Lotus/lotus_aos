package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun GroupScreen(routeAction: RouteAction) {
    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
        CenterAlignedTopAppBar(title = {
            Text(text = "그룹 설정", fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp)
        }, navigationIcon = {
            IconButton(onClick = {
                routeAction.goBack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 28.dp, end = 28.dp)) {

        }
    }
}


@Composable
fun GroupItem() {

    Card(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Button(modifier = Modifier
                    .size(23.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                    onClick = {},
                    content = {})

                Text(
                    text = "그룹 1",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
                )

                androidx.compose.material.IconButton(
                    onClick = {}) {
                    androidx.compose.material.Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.selectgroup),
                        contentDescription = null
                    )
                }
        }
    }
}


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@ExperimentalMaterial3Api
//@Composable
//fun GroupScreen(routeAction: RouteAction) {
//    Scaffold(modifier = Modifier
//        .fillMaxWidth()
//        .imePadding(), topBar = {
//        CenterAlignedTopAppBar(title = {
//            Text(
//                text = "그룹 설정",
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                lineHeight = 24.sp
//            )
//        }, navigationIcon = {
//            IconButton(onClick = {
//                routeAction.goBack()
//            }) {
//                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
//            }
//        })
//    }) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 28.dp, end = 28.dp)
//        ) {
//
//            Spacer(modifier = Modifier.padding(vertical = 41.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 1",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 2",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 3",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 4",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 5",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//            ) {
//                Button(modifier = Modifier
//                    .size(23.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
//                    onClick = {},
//                    content = {})
//
//                Text(
//                    text = "그룹 6",
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 14.sp,
//                    lineHeight = 18.sp,
//                    modifier = Modifier.padding(start = 16.dp, end = 239.dp)
//                )
//
//                androidx.compose.material.IconButton(
//                    onClick = {}) {
//                    androidx.compose.material.Icon(
//                        modifier = Modifier
//                            .size(24.dp),
//                        painter = painterResource(id = R.drawable.selectgroup),
//                        contentDescription = null
//                    )
//                }
//            }
//
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 19.dp, bottom = 19.dp),
//                color = Color(0xffe9e9e9),
//                thickness = 1.dp
//            )
//
//
//        }
//    }
//}