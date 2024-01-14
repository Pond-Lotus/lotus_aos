package com.example.todo_android.composable

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.R
import com.example.todo_android.response.CategoryResponse.CategoryData
import com.example.todo_android.response.TodoResponse.TodoData
import com.example.todo_android.ui.theme.deleteBackground
import com.example.todo_android.viewmodel.Todo.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
fun LazyListScope.TodoItem(
    vm: TodoViewModel,
    token: String,
    categoryList: CategoryData,
    categoryTodoList: Map<Int?, List<TodoData>>,
    scope: CoroutineScope,
    bottomScaffoldState: BottomSheetScaffoldState
) {

    categoryTodoList.forEach { _, items ->
        stickyHeader {
            TodoCategoryHeader(
                categoryList = categoryList,
                items = items
            )
        }

        itemsIndexed(
            items = items,
            key = { index: Int, todo -> todo.id!! }
        ) { index, todo ->

            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                scope.launch {
                    vm.deleteTodo(token, todo)
                }
            }

            SwipeToDismiss(
                modifier = Modifier.padding(
                    start = 21.dp,
                    end = 21.dp
                ),
                state = dismissState,
                background = {
                    DeleteBackground()
                },
                directions = setOf(DismissDirection.EndToStart),
                dismissContent = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clickable {
                                // bottomsheet 열기
                                scope.launch {
                                    Log.d("todotest", "$todo")
                                    vm.setBottomSheetDataSet(todo = todo)
                                    vm.setTodoBottomSheetColor(color = todo.color!!)
                                    bottomScaffoldState.bottomSheetState.expand()
                                }
                            },
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 14.dp, top = 13.dp, bottom = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        vm.setTodoDone(todo, todo.done!!)
                                        vm.updateTodo(
                                            token,
                                            todo.id!!,
                                            UpdateTodo(
                                                todo.year!!,
                                                todo.month!!,
                                                todo.day!!,
                                                todo.title!!,
                                                !todo.done!!,
                                                todo.description!!,
                                                todo.color!!,
                                                todo.time!!
                                            )
                                        )
                                    },
                                painter = painterResource(
                                    getCheckboxImageResource(
                                        done = todo.done!!,
                                        color = todo.color!!
                                    )
                                ),
                                contentDescription = "checkboxImage"
                            )

                            Text(
                                modifier = Modifier.padding(start = 6.dp),
                                text = todo.title!!,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Normal,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(deleteBackground)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(text = "삭제", color = Color.White)
    }
}

@Composable
fun BlankTodoItem() {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 21.dp, end = 21.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 18.dp, top = 12.dp, bottom = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "등록된 토도리스트가 없습니다.",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                fontSize = 14.sp,
                fontStyle = FontStyle.Normal,
                color = Color(0xff9e9e9e)
            )
        }
    }
}

@Composable
fun getCheckboxImageResource(
    done: Boolean,
    color: Int
): Int {
    return if (done.equals(false)) {
        R.drawable.defaultcheckbox;
    } else {
        when (color) {
            1 -> R.drawable.redcheckbox;
            2 -> R.drawable.yellowcheckbox;
            3 -> R.drawable.greencheckbox;
            4 -> R.drawable.bluecheckbox;
            5 -> R.drawable.pinkcheckbox;
            6 -> R.drawable.purplecheckbox;
            else -> R.drawable.defaultcheckbox
        }
    }
}