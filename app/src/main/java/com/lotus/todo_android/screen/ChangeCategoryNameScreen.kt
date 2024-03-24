package com.lotus.todo_android.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.TextFieldDecorationBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lotus.todo_android.R
import com.lotus.todo_android.navigation.Action.RouteAction
import com.lotus.todo_android.util.MyApplication
import com.lotus.todo_android.viewmodel.Todo.TodoViewModel
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun ChangeCategoryNameScreen(
    routeAction: RouteAction,
    color: Int,
    name: String
) {
    val vm: TodoViewModel = hiltViewModel()
    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    val categoryName = remember { mutableStateOf("") }
    val categoryDataSet = mapOf(color to categoryName.value)
    val categoryColor = when (color) {
        1 -> Color(0xffFFB4B4)
        2 -> Color(0xffFFDCA8)
        3 -> Color(0xffB1E0CF)
        4 -> Color(0xffB7D7F5)
        5 -> Color(0xffFFB8EB)
        6 -> Color(0xffB6B1EC)
        else -> Color.Black
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit, block = {
        focusRequester.requestFocus()
        keyboardController?.show()
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
            }, actions = {
                Text(text = "완료",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff9E9E9E),
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .clickable {
                            scope.launch {
                                vm.updateCategory(token, categoryDataSet)
                                routeAction.goBack()
                            }
                        })
            })
        }
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

                Spacer(modifier = Modifier.padding(vertical = 86.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(shape = CircleShape)
                        .background(color = categoryColor)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .focusRequester(focusRequester)
                        .padding(start = 10.dp),
                    colors = CardDefaults.cardColors(Color(0xffF3F3F3))
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, top = 13.dp, bottom = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = categoryName.value,
                            onValueChange = {
                                categoryName.value = it
                            },
                            textStyle = LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = Color.Black,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            interactionSource = remember { MutableInteractionSource() }
                        ) { innerTextField ->
                            TextFieldDefaults.TextFieldDecorationBox(
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    disabledLabelColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                    start = 0.dp, top = 0.dp, bottom = 0.dp
                                ),
                                placeholder = {
                                    Text(
                                        text = name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Light,
                                        color = Color(0xff9E9E9E),
                                    )
                                },
                                value = categoryName.value,
                                innerTextField = innerTextField,
                                singleLine = true,
                                enabled = true,
                                interactionSource = remember { MutableInteractionSource() },
                                visualTransformation = VisualTransformation.None
                            )
                        }
                    }
                }
            }
        }
    }
}