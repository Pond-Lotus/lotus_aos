package com.example.todo_android.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.response.CategoryResponse.CategoryData
import com.example.todo_android.response.TodoResponse.TodoData

@Composable
fun TodoCategoryHeader(
    categoryList: CategoryData? = null,
    items: List<TodoData>? = null,
    categoryColor: Int? = null,
) {

    val color = items?.map { it.color }?.last()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 21.dp,
                end = 21.dp
            ),
        color = Color(0xfff0f0f0)
    ) {

        if (categoryColor != null) {
            when (categoryColor) {
                1 -> {
                    val name = categoryList?._1 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
                2 -> {
                    val name = categoryList?._2 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
                3 -> {
                    val name = categoryList?._3 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
                4 -> {
                    val name = categoryList?._4 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
                5 -> {
                    val name = categoryList?._5 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
                6 -> {
                    val name = categoryList?._6 ?: ""
                    Log.d("nameTest", name)
                    TodoCategoryDetailHeader(color = categoryColor, categoryName = name)
                }
            }
        }

        when (color) {
            1 -> {
                val name = categoryList?._1 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
            2 -> {
                val name = categoryList?._2 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
            3 -> {
                val name = categoryList?._3 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
            4 -> {
                val name = categoryList?._4 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
            5 -> {
                val name = categoryList?._5 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
            6 -> {
                val name = categoryList?._6 ?: ""
                TodoCategoryDetailHeader(color = color, categoryName = name)
            }
        }
    }
}

@Composable
fun TodoCategoryDetailHeader(
    color: Int,
    categoryName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(9.dp)
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
            modifier = Modifier.padding(start = 4.dp),
            text = categoryName,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 17.sp
        )
    }
}