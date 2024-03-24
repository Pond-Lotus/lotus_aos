package com.lotus.todo_android.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction
import com.lotus.todo_android.navigation.NAV_ROUTE
import com.lotus.todo_android.util.MyApplication
import com.lotus.todo_android.viewmodel.Todo.TodoViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CategoryScreen(routeAction: RouteAction) {

    val vm: TodoViewModel = hiltViewModel()
    val categoryList by vm.categoryList.collectAsState()
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    LaunchedEffect(key1 = categoryList, block = {
        vm.readCategory(token)
    })

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
                    text = "그룹 설정",
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
                .padding(start = 28.dp, end = 28.dp)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 28.dp))

            CategoryItem(
                color = 1,
                name = categoryList._1 ?: "",
                routeAction = routeAction
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp),
                color = Color(0xffe9e9e9),
                thickness = 1.dp
            )

            CategoryItem(
                color = 2,
                name = categoryList._2 ?: "",
                routeAction = routeAction
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp),
                color = Color(0xffe9e9e9),
                thickness = 1.dp
            )

            CategoryItem(
                color = 3,
                name = categoryList._3 ?: "",
                routeAction = routeAction
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp),
                color = Color(0xffe9e9e9),
                thickness = 1.dp
            )

            CategoryItem(
                color = 4,
                name = categoryList._4 ?: "",
                routeAction = routeAction
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp),
                color = Color(0xffe9e9e9),
                thickness = 1.dp
            )

            CategoryItem(
                color = 5,
                name = categoryList._5 ?: "",
                routeAction = routeAction
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp, bottom = 7.dp),
                color = Color(0xffe9e9e9),
                thickness = 1.dp
            )

            CategoryItem(
                color = 6,
                name = categoryList._6 ?: "",
                routeAction = routeAction
            )
        }
    }
}


@Composable
fun CategoryItem(
    color: Int,
    name: String,
    routeAction: RouteAction,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {

        TodoCategoryDetailData(
            color = color,
            categoryName = name,
            routeAction = routeAction
        )

    }
}

@Composable
fun TodoCategoryDetailData(
    color: Int,
    categoryName: String,
    routeAction: RouteAction
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(23.dp)
                .clip(shape = CircleShape)
                .background(
                    color = when (color) {
                        1 -> Color(0xffFFB4B4)
                        2 -> Color(0xffFFDCA8)
                        3 -> Color(0xffB1E0CF)
                        4 -> Color(0xffB7D7F5)
                        5 -> Color(0xffFFB8EB)
                        6 -> Color(0xffB6B1EC)
                        else -> Color.Black
                    }
                )
        )

        Text(
            text = categoryName,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 16.dp),
            maxLines = 1
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier
                .size(21.dp)
                .clickable {
                    routeAction.customNavto(NAV_ROUTE.CHANGECATEGORY, color, categoryName)
                },
            painter = painterResource(id = R.drawable.selectgroup),
            contentDescription = null
        )
    }
}