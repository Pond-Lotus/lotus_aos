package com.example.todo_android.Screen

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.todo_android.Data.Category.UpdateCategory
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.Request.CategoryRequest.UpdateCategoryRequest
import com.example.todo_android.Response.CategoryResponse.ReadCategoryResponse
import com.example.todo_android.Response.CategoryResponse.UpdateCategoryResponse
import com.example.todo_android.Util.MyApplication
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun updateCategory(
    token: String, categorySet: Map<String, String?>, response: (UpdateCategoryResponse?) -> Unit
) {
    var updateCategoryResponse: UpdateCategoryResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("https://team-lotus.kr/ ").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var updateCategoryRequest: UpdateCategoryRequest =
        retrofit.create(UpdateCategoryRequest::class.java)

    updateCategoryRequest.requestUpdateCategory(token, UpdateCategory(categorySet))
        .enqueue(object : Callback<UpdateCategoryResponse> {
            override fun onResponse(
                call: Call<UpdateCategoryResponse>, response: Response<UpdateCategoryResponse>
            ) {
                updateCategoryResponse = response.body()

                when (updateCategoryResponse?.resultCode) {
                    200 -> {
                        Log.d(
                            "updateCategory", "resultCode : " + updateCategoryResponse?.resultCode
                        )
                        response(updateCategoryResponse)
                    }
                    500 -> {
                        Log.d(
                            "updateCategory", "resultCode : " + updateCategoryResponse?.resultCode
                        )
                    }
                }
            }

            override fun onFailure(call: Call<UpdateCategoryResponse>, t: Throwable) {
                Log.e("updateCategory", t.message.toString())
            }

        })
}


@ExperimentalComposeUiApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun ChangeCategoryNameScreen(
    routeAction: RouteAction, categoryName: String?, categoryId: String?, categoryColor: Int?
) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"
    var categoryName by remember { mutableStateOf(categoryName ?: "") }
    var categoryId by remember { mutableStateOf(categoryId ?: "") }
    var categoryData = remember { mutableStateListOf<ReadCategoryResponse>() }
    var testList = remember { mutableStateMapOf<String, String>() }
    var scope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            readCategory(response = { response ->
                response?.data?.let { data ->
                    categoryData.clear()
                    data.forEach { (key, value) ->
                        categoryData.add(
                            ReadCategoryResponse(
                                response.resultCode, mapOf(key to value)
                            )
                        )
                        testList[key] = value
                    }
                }
            })
        }
    })

    LaunchedEffect(Unit, block = {
        focusRequester.requestFocus()
        keyboardController?.show()
    })


    val categorySet = when (categoryId) {
        "1" -> mapOf(
            categoryId to categoryName,
            "2" to testList["2"],
            "3" to testList["3"],
            "4" to testList["4"],
            "5" to testList["5"],
            "6" to testList["6"]
        )
        "2" -> mapOf(
            "1" to testList["1"],
            categoryId to categoryName,
            "3" to testList["3"],
            "4" to testList["4"],
            "5" to testList["5"],
            "6" to testList["6"]
        )
        "3" -> mapOf(
            "1" to testList["2"],
            "2" to testList["3"],
            categoryId to categoryName,
            "4" to testList["4"],
            "5" to testList["5"],
            "6" to testList["6"]
        )
        "4" -> mapOf(
            "1" to testList["2"],
            "2" to testList["3"],
            "3" to testList["4"],
            categoryId to categoryName,
            "5" to testList["5"],
            "6" to testList["6"]
        )
        "5" -> mapOf(
            "1" to testList["2"],
            "2" to testList["3"],
            "3" to testList["4"],
            "4" to testList["5"],
            categoryId to categoryName,
            "6" to testList["6"]
        )
        "6" -> mapOf(
            "1" to testList["2"],
            "2" to testList["3"],
            "3" to testList["4"],
            "4" to testList["5"],
            "5" to testList["6"],
            categoryId to categoryName
        )
        else -> return
    }

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
                                updateCategory(token, categorySet) {
                                    routeAction.navTo(NAV_ROUTE.SELECTCATEGORY)
                                }
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

                Button(modifier = Modifier.size(28.dp),
                    colors = ButtonDefaults.buttonColors(Color(categoryColor!!)),
                    onClick = {},
                    content = {})

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
                            value = categoryName,
                            onValueChange = {
                                categoryName = it
                            },
                            textStyle = TextStyle(
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
                        )
                    }
                }
            }
        }
    }
}