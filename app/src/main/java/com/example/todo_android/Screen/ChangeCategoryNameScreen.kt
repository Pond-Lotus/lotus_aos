package com.example.todo_android.Screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Request.CategoryRequest.UpdateCategoryRequest
import com.example.todo_android.Response.CategoryResponse.UpdateCategoryResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun updateCategory(
    token: String,
    categoryName: String?,
    routeAction: RouteAction,
    response: (UpdateCategoryResponse?) -> Unit
){
    var updateCategoryResponse: UpdateCategoryResponse? = null

    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var updateCategoryRequest: UpdateCategoryRequest = retrofit.create(UpdateCategoryRequest::class.java)

    //updateCategoryRequest.requestUpdateCategory(token, categoryName)
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ChangeCategoryNameScreen(routeAction: RouteAction, categoryName: String?, categoryColor: Int?) {

    var categoryName by remember { mutableStateOf(categoryName ?: "") }

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

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
        }, actions = {
            TextButton(onClick = {

            }) {
                Text(
                    text = "완료",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E)
                )
            }
        })
    }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 28.dp, end = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.padding(vertical = 110.dp))

                Button(modifier = Modifier.size(28.dp),
//                    colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                    colors = ButtonDefaults.buttonColors(Color(categoryColor!!)),
                    onClick = {},
                    content = {})

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(start = 10.dp),
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                    },
                    placeholder = {
                        Text(
                            text = "그룹 이름 지정",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            lineHeight = 24.sp
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xffF3F3F3),
                        disabledLabelColor = Color(0xffF3F3F3),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }
}