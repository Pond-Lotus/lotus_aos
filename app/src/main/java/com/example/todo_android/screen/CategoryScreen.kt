//package com.example.todo_android.screen
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawWithContent
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.todo_android.R
//import com.example.todo_android.navigation.Action.RouteAction
//import com.example.todo_android.navigation.NAV_ROUTE
//import com.example.todo_android.response.CategoryResponse.CategoryData
//import com.example.todo_android.response.TodoResponse.TodoData
//import com.example.todo_android.viewmodel.Todo.TodoViewModel
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@ExperimentalMaterial3Api
//@Composable
//fun CategoryScreen(routeAction: RouteAction) {
//
//    val vm: TodoViewModel = hiltViewModel()
//    val categoryList by vm.categoryList.collectAsState()
//    val categoryTodoList by vm.categoryTodoList.collectAsState()
//    var scope = rememberCoroutineScope()
//
//    Scaffold(modifier = Modifier
//        .fillMaxWidth()
//        .imePadding(), topBar = {
//        Box(
//            Modifier
//                .fillMaxWidth()
//                .height(45.dp)
//                .drawWithContent {
//                    drawContent()
//                    drawLine(
//                        color = Color(0x26000000), // 기존에 사용 중이셨던 보더 컬러를 선택하세요.
//                        start = Offset(x = 0f, y = size.height - 1.dp.toPx()),
//                        end = Offset(x = size.width, y = size.height - 1.dp.toPx()),
//                        strokeWidth = 1.dp.toPx() // 보더 두께를 원하는 값으로 설정하세요.
//                    )
//                }) {
//            CenterAlignedTopAppBar(title = {
//                Text(
//                    text = "그룹 설정",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    lineHeight = 24.sp
//                )
//            }, navigationIcon = {
//                IconButton(onClick = {
//                    routeAction.goBack()
//                }) {
//                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
//                }
//            })
//        }
//    }) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(start = 28.dp, end = 28.dp)
//        ) {
//
//            Spacer(modifier = Modifier.padding(vertical = 28.dp))
//
//            categoryTodoList.forEach { _, items ->
//                CategoryItem(
//                    categoryList = categoryList,
//                    items = items,
//                    routeAction = routeAction
//                )
//            }
//
//
////            CategoryItemList(categoryList, routeAction)
//        }
//    }
//}
//
//@Composable
//fun CategoryItem(
//    categoryList: List<CategoryData>,
//    items: List<TodoData>,
//    routeAction: RouteAction
//) {
//
//    val color = items.map { it.color }.last()
//
//    Card(
//        modifier = Modifier.fillMaxSize(),
//        colors = CardDefaults.cardColors(Color.Transparent)
//    ) {
//        when (color) {
//            1 -> {
//                val name = categoryList.map { it._1 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//            2 -> {
//                val name = categoryList.map { it._2 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//            3 -> {
//                val name = categoryList.map { it._3 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//            4 -> {
//                val name = categoryList.map { it._4 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//            5 -> {
//                val name = categoryList.map { it._5 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//            6 -> {
//                val name = categoryList.map { it._6 }.last()
//                TodoCategoryDetailData(
//                    color = color,
//                    categoryName = name!!,
//                    routeAction = routeAction
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun TodoCategoryDetailData(color: Int, categoryName: String, routeAction: RouteAction) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 16.dp, bottom = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Box(
//            modifier = Modifier
//                .size(23.dp)
//                .clip(shape = CircleShape)
//                .background(
//                    color = when (color) {
//                        1 -> Color(0xffFFB4B4)
//                        2 -> Color(0xffFFDCA8)
//                        3 -> Color(0xffB1E0CF)
//                        4 -> Color(0xffB7D7F5)
//                        5 -> Color(0xffFFB8EB)
//                        6 -> Color(0xffB6B1EC)
//                        else -> Color.Black
//                    }
//                )
//        ) {
//            Text(
//                text = categoryName,
//                fontWeight = FontWeight.Medium,
//                fontSize = 15.sp,
//                lineHeight = 18.sp,
//                modifier = Modifier.padding(start = 16.dp),
//                maxLines = 1
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Image(
//                modifier = Modifier
//                    .size(21.dp)
//                    .clickable {
//                        routeAction.customNavto(NAV_ROUTE.CHANGECATEGORY)
//                    },
//                painter = painterResource(id = R.drawable.selectgroup),
//                contentDescription = null
//            )
//        }
//    }
//}
//
////@Composable
////fun CategoryItem(Category: ReadCategoryResponse, routeAction: RouteAction) {
////
////    val colors = when (Category.data.keys.first()) {
////        "1" -> Color(0xffFFB4B4)
////        "2" -> Color(0xffFFDCA8)
////        "3" -> Color(0xffB1E0CF)
////        "4" -> Color(0xffB7D7F5)
////        "5" -> Color(0xffFFB8EB)
////        "6" -> Color(0xffB6B1EC)
////        else -> Color.Black
////    }
////
////    Card(
////        modifier = Modifier.fillMaxSize(),
////        colors = CardDefaults.cardColors(Color.Transparent)
////    ) {
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(top = 16.dp, bottom = 16.dp),
////            verticalAlignment = Alignment.CenterVertically,
////            horizontalArrangement = Arrangement.SpaceBetween
////        ) {
////            Button(
////                modifier = Modifier.size(23.dp),
////                onClick = {},
////                content = {},
////                colors = ButtonDefaults.buttonColors(colors)
////            )
////
////            Text(
////                text = Category.data.values.first().toString(), // 수정된 부분
////                fontWeight = FontWeight.Medium,
////                fontSize = 15.sp,
////                lineHeight = 18.sp,
////                modifier = Modifier.padding(start = 16.dp),
////                maxLines = 1
////            )
////
////            Spacer(modifier = Modifier.weight(1f))
////
////            Image(
////                modifier = Modifier
////                    .size(21.dp)
////                    .clickable {
////                        routeAction.customNavto(
////                            NAV_ROUTE.CHANGECATEGORY,
////                            Category.data.values
////                                .first()
////                                .toString(),
////                            Category.data.keys
////                                .first()
////                                .toString(),
////                            colors.toArgb()
////                        )
////                    },
////                painter = painterResource(id = R.drawable.selectgroup),
////                contentDescription = null
////            )
////        }
////    }
////}
////
////
////@Composable
////fun CategoryItemList(categoryList: List<ReadCategoryResponse>, routeAction: RouteAction) {
////
////    LazyColumn {
////        itemsIndexed(items = categoryList) { index, item ->
////            CategoryItem(Category = item, routeAction)
////
////            // 마지막 아이템이 아닌 경우만 Divider를 추가합니다.
////            if (index < categoryList.size - 1) {
////                Divider(
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(top = 7.dp, bottom = 7.dp),
////                    color = Color(0xffe9e9e9),
////                    thickness = 1.dp
////                )
////            }
////        }
////    }
////}