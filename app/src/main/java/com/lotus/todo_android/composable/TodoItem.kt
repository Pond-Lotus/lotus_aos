package com.lotus.todo_android.composable

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.lotus.todo_android.Data.Todo.CreateTodo
import com.lotus.todo_android.Data.Todo.UpdateTodo
import com.lotus.todo_android.R
import com.lotus.todo_android.response.CategoryResponse.CategoryData
import com.lotus.todo_android.response.TodoResponse.TodoData
import com.lotus.todo_android.ui.theme.deleteBackground
import com.lotus.todo_android.viewmodel.Todo.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@ExperimentalMaterial3Api
fun LazyListScope.TodoItem(
    vm: TodoViewModel,
    token: String,
    categoryList: CategoryData,
    categoryTodoList: Map<Int?, List<TodoData>>,
    scope: CoroutineScope,
    bottomScaffoldState: BottomSheetScaffoldState,
    categoryColor: MutableState<Int>,
    todoYear: Int,
    todoMonth: Int,
    todoDay: Int,
    todoTitle: String,
    todoColor: Int,
    isCategoryVisibility: MutableState<Boolean>,
) {

    categoryTodoList.forEach { (key, items) ->
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
                            modifier = Modifier.padding(start = 14.dp, top = 11.dp, bottom = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(19.dp)
                                    .padding(top = 2.dp)
                                    .clickable {
//                                        vm.setTodoDone(todo, todo.done!!)
                                        vm.updateTodo(
                                            token,
                                            todo.id!!,
                                            com.lotus.todo_android.Data.Todo.UpdateTodo(
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
                                textAlign = TextAlign.Center,
                                style = LocalTextStyle.current.merge(
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

        item {
            if (isCategoryVisibility.value && categoryColor.value == key) {
                val focusRequester = remember { FocusRequester() }

                LaunchedEffect(isCategoryVisibility) {
                    delay(500)
                    if (isCategoryVisibility.value) {
                        focusRequester.requestFocus()
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(start = 21.dp, end = 21.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 14.dp,
                                top = 13.dp,
                                bottom = 13.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.defaultcheckbox),
                            contentDescription = null
                        )

                        BasicTextField(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(start = 6.dp)
                                .focusRequester(focusRequester)
                                .imePadding(),
                            value = todoTitle,
                            onValueChange = { text: String ->
                                vm.setTodoTitle(text)
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Normal,
                                color = Color.Black,
                                lineHeight = 31.sp
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                scope.launch {
                                    vm.createTodo(
                                        token,
                                        com.lotus.todo_android.Data.Todo.CreateTodo(
                                            todoYear,
                                            todoMonth,
                                            todoDay,
                                            todoTitle,
                                            todoColor
                                        )
                                    )
                                    vm.setTodoTitle("")
                                    isCategoryVisibility.value = !isCategoryVisibility.value
                                }
                            })
                        )
                    }
                }
            }
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