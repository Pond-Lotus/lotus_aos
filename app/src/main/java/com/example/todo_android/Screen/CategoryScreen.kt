package com.example.todo_android.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.R
import com.example.todo_android.Response.CategoryResponse.ReadCategoryResponse

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun CategoryScreen(routeAction: RouteAction) {

    var categoryList = remember { mutableStateListOf<ReadCategoryResponse>() }

    LaunchedEffect(
        key1 = Unit,
        block = {
            readCategory(response = {
                categoryList.clear()
//                for (entry in it?.data ?: emptyMap()) {
//                    val readCategoryResponse = ReadCategoryResponse(
//                        resultCode = it?.resultCode ?: 0,
//                        data = mapOf(entry.key to entry.value)
//                    )
//                    categoryList.add(readCategoryResponse)
//                }
                for(i in it!!.data) {
                    categoryList.add()
                }
            })
        }
    )

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), topBar = {
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
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 28.dp, end = 28.dp)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 41.dp))

            CategoryItemList(categoryList)
        }
    }
}

@Composable
fun CategoryItem(Category: ReadCategoryResponse) {
    Card(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.size(23.dp),
                colors = ButtonDefaults.buttonColors(
                    when(Category.data.toList().toString()) {
                        "1" -> Color(0xffFFB4B4)
                        "2" -> Color(0xffFFDCA8)
                        "3" -> Color(0xffB1E0CF)
                        "4" -> Color(0xffB7D7F5)
                        "5" -> Color(0xffFFB8EB)
                        "6" -> Color(0xffB6B1EC)
                        else -> Color.Black
                    }),
                onClick = {},
                content = {}
            )

            Text(
                text = Category.data.toList().toString(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(start = 16.dp, end = 239.dp)
            )

            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.selectgroup),
                    contentDescription = null
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 19.dp, bottom = 19.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp
        )
    }
}


@Composable
fun CategoryItemList(Category: List<ReadCategoryResponse>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items = Category, key = { item -> item }) { item ->
            CategoryItem(Category = item)
        }
    }
}