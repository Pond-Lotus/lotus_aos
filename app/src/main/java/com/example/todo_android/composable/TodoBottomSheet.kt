package com.example.todo_android.composable

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Data.Todo.UpdateTodo
import com.example.todo_android.R
import com.example.todo_android.util.MyApplication
import com.example.todo_android.viewmodel.Todo.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun TodoUpdateBottomSheet(
    vm: TodoViewModel,
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    focusRequester: FocusRequester
) {
    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    var dayString by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val Todo = vm.bottomsheetViewData.collectAsState()
    val title = vm.todoBottomSheetTitle.collectAsState()
    val description = vm.todoBottomSheetDescription.collectAsState()
    val time = vm.todoBottomSheetTime.collectAsState()
    var color = vm.todoBottomSheetColor.collectAsState()

    // TimePicker 관련 변수
    var context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    // 요일 관련
    val selectedDate = LocalDate.of(
        vm.todoYear.value,
        vm.todoMonth.value,
        vm.todoDay.value
    )
    val dayOfWeek = selectedDate.dayOfWeek

    dayString = when (dayOfWeek.value) {
        1 -> "월요일"
        2 -> "화요일"
        3 -> "수요일"
        4 -> "목요일"
        5 -> "금요일"
        6 -> "토요일"
        7 -> "일요일"
        else -> ""
    }


    val timePickerDialog = TimePickerDialog(
        context, R.style.TimePickerDialog, { _, hour: Int, minute: Int ->
            vm.todoBottomSheetTime.value = String.format("%02d%02d", hour, minute)
        }, hour, minute, false
    )

    val TodoColor: (Int) -> Color = {
        when (color.value) {
            1 -> {
                Color(0xffFFB4B4)
            }
            2 -> {
                Color(0xffFFDCA8)
            }
            3 -> {
                Color(0xffB1E0CF)
            }
            4 -> {
                Color(0xffB7D7F5)
            }
            5 -> {
                Color(0xffFFB8EB)
            }
            6 -> {
                Color(0xffB6B1EC)
            }
            else -> {
                Color.Black
            }
        }
    }

    LaunchedEffect(bottomSheetScaffoldState.bottomSheetState.currentValue) {
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }


    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 25.dp, end = 25.dp, top = 20.dp)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.clickable {
                    scope.launch {
                        keyboardController?.hide()
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }, painter = painterResource(id = R.drawable.close), contentDescription = null
            )

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(30.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color(0xffFFBE3C7))
                    .clickable {
                        scope.launch {
                            vm.updateTodo(
                                token,
                                Todo.value.id!!,
                                UpdateTodo(
                                    Todo.value.year!!,
                                    Todo.value.month!!,
                                    Todo.value.day!!,
                                    title.value,
                                    Todo.value.done!!,
                                    description.value,
                                    color.value,
                                    time.value
                                )
                            )
                            keyboardController?.hide()
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "저장",
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(9.dp)
                    .height(51.dp)
                    .clip(shape = CircleShape)
                    .background(color = TodoColor(color.value))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${Todo.value.month}월 ${Todo.value.day}일 $dayString",
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TodoColor(color.value)
                )
                BasicTextField(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .focusRequester(focusRequester),
                    value = title.value,
                    onValueChange = {
                        vm.todoBottomSheetTitle.value = it
                    },
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 31.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (Todo.value.title?.isEmpty() == true) {
                                Text(
                                    text = "토도리스트 입력",
                                    fontSize = 24.sp,
                                    lineHeight = 31.2.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color(0xFFC8C8C8),
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            value = description.value,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(10.dp),
            onValueChange = {
                if (it.count { it == '\n' } < 4) {
                    vm.todoBottomSheetDescription.value = it
                }
            },
            maxLines = 4,
            placeholder = {
                Text(
                    text = "+  메모하고 싶은 내용이 있나요?",
                    fontSize = 14.sp,
                    lineHeight = 19.6.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF9E9E9E)
                )
            },
            textStyle = TextStyle(fontSize = 14.sp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.clock),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 5.dp)
            )
            Text(
                text = "시간",
                modifier = Modifier.padding(end = 8.dp),
                fontWeight = FontWeight.Bold,
                lineHeight = 19.sp,
                fontSize = 15.sp
            )
            Text(
                modifier = Modifier.clickable {
                    timePickerDialog.show()
                },
                text = if (time.value == "9999") {
                    "미지정"
                } else {
                    TimeFormat(time.value)
                },
                lineHeight = 19.sp,
                fontSize = 15.sp,
                color = Color(0xff9E9E9E)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 19.dp),
            color = Color(0xffe9e9e9)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 19.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(1)
                    }, painter = if (color.value == 1) {
                    painterResource(id = R.drawable.redselecbutton)
                } else {
                    painterResource(id = R.drawable.redbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(2)
                    }, painter = if (color.value == 2) {
                    painterResource(id = R.drawable.yellowselecbutton)
                } else {
                    painterResource(id = R.drawable.yellowbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(3)
                    }, painter = if (color.value == 3) {
                    painterResource(id = R.drawable.greenselecbutton)
                } else {
                    painterResource(id = R.drawable.greenbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(4)
                    }, painter = if (color.value == 4) {
                    painterResource(id = R.drawable.blueselecbutton)
                } else {
                    painterResource(id = R.drawable.bluebutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(5)
                    }, painter = if (color.value == 5) {
                    painterResource(id = R.drawable.pinkselecbutton)
                } else {
                    painterResource(id = R.drawable.pinkbutton)
                }, contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        vm.setTodoBottomSheetColor(6)
                    }, painter = if (color.value == 6) {
                    painterResource(id = R.drawable.purpleselecbutton)
                } else {
                    painterResource(id = R.drawable.purplebutton)
                }, contentDescription = null
            )
        }
    }
}

fun TimeFormat(time: String): String {
    val hour = time.substring(0, 2).toInt()
    val minute = time.substring(2).toInt()

    return if (hour >= 12) {
        val formattedHour = if (hour > 12) hour else hour + 12
        val formattedMinute = String.format("%02d", minute)
        "오후 ${formattedHour}:$formattedMinute"
    } else {
        val formattedHour = if (hour == 0) 12 else hour
        val formattedMinute = String.format("%02d", minute)
        "오전 ${formattedHour}:$formattedMinute"
    }
}