package com.example.todo_android.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.navigation.NAV_ROUTE
import com.example.todo_android.viewmodel.Todo.TodoViewModel
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun ChangeCategoryNameScreen(
    routeAction: RouteAction,
    color: Int,
    categoryText: String
) {


//    var categoryName by remember { mutableStateOf(categoryName ?: "") }
//    var categoryId by remember { mutableStateOf(categoryId ?: "") }
//    var categoryData = remember { mutableStateListOf<ReadCategoryResponse>() }
//    var testList = remember { mutableStateMapOf<String, String>() }
//    var scope = rememberCoroutineScope()
    val vm: TodoViewModel = hiltViewModel()
    val categoryList by vm.categoryList.collectAsState()
    val categoryColor = when(color){
        1 -> Color(0xffFFB4B4)
        2 -> Color(0xffFFDCA8)
        3 -> Color(0xffB1E0CF)
        4 -> Color(0xffB7D7F5)
        5 -> Color(0xffFFB8EB)
        6 -> Color(0xffB6B1EC)
        else -> Color.Black
    }
    val categoryText by vm.categoryText.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit, block = {
        focusRequester.requestFocus()
        keyboardController?.show()
    })


//    val categorySet = when (categoryId) {
//        "1" -> mapOf(
//            categoryId to categoryName,
//            "2" to testList["2"],
//            "3" to testList["3"],
//            "4" to testList["4"],
//            "5" to testList["5"],
//            "6" to testList["6"]
//        )
//        "2" -> mapOf(
//            "1" to testList["1"],
//            categoryId to categoryName,
//            "3" to testList["3"],
//            "4" to testList["4"],
//            "5" to testList["5"],
//            "6" to testList["6"]
//        )
//        "3" -> mapOf(
//            "1" to testList["2"],
//            "2" to testList["3"],
//            categoryId to categoryName,
//            "4" to testList["4"],
//            "5" to testList["5"],
//            "6" to testList["6"]
//        )
//        "4" -> mapOf(
//            "1" to testList["2"],
//            "2" to testList["3"],
//            "3" to testList["4"],
//            categoryId to categoryName,
//            "5" to testList["5"],
//            "6" to testList["6"]
//        )
//        "5" -> mapOf(
//            "1" to testList["2"],
//            "2" to testList["3"],
//            "3" to testList["4"],
//            "4" to testList["5"],
//            categoryId to categoryName,
//            "6" to testList["6"]
//        )
//        "6" -> mapOf(
//            "1" to testList["2"],
//            "2" to testList["3"],
//            "3" to testList["4"],
//            "4" to testList["5"],
//            "5" to testList["6"],
//            categoryId to categoryName
//        )
//        else -> return
//    }

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
//                            scope.launch {
//                                updateCategory(token, categorySet) {
//                                    routeAction.navTo(NAV_ROUTE.SELECTCATEGORY)
//                                }
//                            }
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

//                Button(modifier = Modifier.size(28.dp),
//                    colors = ButtonDefaults.buttonColors(Color(categoryColor!!)),
//                    onClick = {},
//                    content = {})
                Box(modifier = Modifier
                    .size(28.dp)
                    .background(color = categoryColor))

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
//                        BasicTextField(
//                            value = categoryName,
//                            onValueChange = {
//                                categoryName = it
//                            },
//                            textStyle = TextStyle(
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 15.sp,
//                                color = Color.Black,
//                                platformStyle = PlatformTextStyle(
//                                    includeFontPadding = false
//                                )
//                            ),
//                            singleLine = true,
//                            maxLines = 1,
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        )
                    }
                }
            }
        }
    }
}