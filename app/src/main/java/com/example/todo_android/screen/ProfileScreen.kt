package com.example.todo_android.screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_android.R
import com.example.todo_android.navigation.Action.RouteAction
import com.example.todo_android.navigation.NAV_ROUTE
import com.example.todo_android.request.ModifyRequest.ChangeProfileRequest
import com.example.todo_android.request.ModifyRequest.DeleteProfileImageRequest
import com.example.todo_android.response.ModifyResponse.ChangeProfileResponse
import com.example.todo_android.response.ModifyResponse.DeleteProfileImageResponse
import com.example.todo_android.util.MyApplication
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

fun changeProfile(
    token: String,
    imdel: Boolean,
    nickname: RequestBody,
    image: MultipartBody.Part,
    routeAction: RouteAction,
) {

    var changeProfileResponse: ChangeProfileResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http:35.225.210.179:8000/ ").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var changeProfileRequest: ChangeProfileRequest =
        retrofit.create(ChangeProfileRequest::class.java)

    changeProfileRequest.requestChangeProfile(token, imdel, nickname, image)
        .enqueue(object : Callback<ChangeProfileResponse> {

            // 성공 했을때
            override fun onResponse(
                call: Call<ChangeProfileResponse>,
                response: Response<ChangeProfileResponse>,
            ) {
                changeProfileResponse = response.body()

                when (changeProfileResponse?.resultCode) {
                    200 -> {
                        MyApplication.prefs.setData(
                            "nickname", changeProfileResponse?.data?.nickname.toString()
                        )
                        MyApplication.prefs.setData(
                            "image", changeProfileResponse?.data?.image.toString()
                        )
                        routeAction.goBack()

                        Log.d("changeProfile", "resultCode : " + changeProfileResponse?.resultCode)
                        Log.d("changeProfile", "resultCode : " + changeProfileResponse?.data)
                    }
                    500 -> {
                        Log.d("changeProfile", "resultCode : " + changeProfileResponse?.resultCode)
                    }
                }
            }

            // 실패 했을때
            override fun onFailure(call: Call<ChangeProfileResponse>, t: Throwable) {
                Log.e("changeProfile", t.message.toString())
            }
        })
}

fun deleteProfileImage(
    token: String,
    imdel: Boolean,
    nickname: RequestBody,
    routeAction: RouteAction,
) {
    var deleteProfileImageResponse: DeleteProfileImageResponse? = null

    val okHttpClient: OkHttpClient by lazy {
        val httpLoInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(httpLoInterceptor).build()
    }

    var retrofit = Retrofit.Builder().baseUrl("http:35.225.210.179:8000/ ").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    var deleteProfileImageRequest: DeleteProfileImageRequest =
        retrofit.create(DeleteProfileImageRequest::class.java)

    deleteProfileImageRequest.requestDeleteProfileImage(token, nickname, imdel)
        .enqueue(object : Callback<DeleteProfileImageResponse> {

            // 성공 했을때
            override fun onResponse(
                call: Call<DeleteProfileImageResponse>,
                response: Response<DeleteProfileImageResponse>
            ) {
                deleteProfileImageResponse = response.body()

                when (deleteProfileImageResponse?.resultCode) {
                    200 -> {
                        MyApplication.prefs.setData(
                            "nickname", deleteProfileImageResponse?.data?.nickname.toString()
                        )
                        MyApplication.prefs.setData(
                            "image", deleteProfileImageResponse?.data?.image.toString()
                        )
                        routeAction.goBack()

                        Log.d(
                            "deleteProfileImage",
                            "resultCode : " + deleteProfileImageResponse?.resultCode
                        )
                        Log.d(
                            "deleteProfileImage", "resultCode : " + deleteProfileImageResponse?.data
                        )
                    }
                    500 -> {
                        Log.d(
                            "deleteProfileImage",
                            "resultCode : " + deleteProfileImageResponse?.resultCode
                        )
                    }
                }
            }

            // 실패 했을때
            override fun onFailure(call: Call<DeleteProfileImageResponse>, t: Throwable) {
                Log.e("deleteProfileImage", t.message.toString())
            }

        })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(routeAction: RouteAction) {

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var email: String = MyApplication.prefs.getData("email", "")
    var nickname by remember { mutableStateOf(MyApplication.prefs.getData("nickname", "")) }
    var openDialog by remember { mutableStateOf(false) }
    var imdel = remember { mutableStateOf(true) }
    var image = remember { mutableStateOf<MultipartBody.Part?>(null) }
    val context = LocalContext.current

    val initializeComposition = remember { mutableStateOf(true) }

    var scope = rememberCoroutineScope()

    var bitmap = remember {
        mutableStateOf<ImageBitmap?>(
            BitmapFactory.decodeResource(
                context.resources, R.drawable.defaultprofile
            ).asImageBitmap()
        )
    }

    if (openDialog) {
        setImageDialog(onDismissRequest = { openDialog = false },
            image = image,
            context = context,
            imdel = imdel,
            bitmap = { resultBitmap -> bitmap.value = resultBitmap })
    }

    LaunchedEffect(key1 = initializeComposition) {

        scope.launch {
            bitmap.value = if (MyApplication.prefs.getData("image", "") == "null") {
                BitmapFactory.decodeResource(context.resources, R.drawable.defaultprofile)
                    .asImageBitmap()
            } else {
                val base64EncodedImage = MyApplication.prefs.getData("image", "")
                val DecodedBytes = Base64.decode(base64EncodedImage, Base64.DEFAULT)
                val ImageBitmap = BitmapFactory.decodeByteArray(DecodedBytes, 0, DecodedBytes.size)

                ImageBitmap.asImageBitmap()
            }
            initializeComposition.value = !initializeComposition.value
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .imePadding(),
        topBar = {
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
                        text = "프로필 수정",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )
                }, navigationIcon = {
                    IconButton(onClick = {
//                        routeAction.navTo(NAV_ROUTE.CALENDAR)
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
                                if (image.value != null) {
                                    scope.launch {
                                        changeProfile(
                                            token,
                                            imdel.value,
                                            nickname.toRequestBody("text/plain".toMediaTypeOrNull()),
                                            image.value!!,
                                            routeAction
                                        )
                                    }
                                } else {
                                    scope.launch {
                                        deleteProfileImage(
                                            token,
                                            imdel.value,
                                            nickname.toRequestBody("text/plain".toMediaTypeOrNull()),
                                            routeAction
                                        )
                                    }
                                }
                            })
                })
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp, end = 20.dp, bottom = 37.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(vertical = 35.dp))

            Box(modifier = Modifier.padding(8.dp)) {
                Image(bitmap = bitmap.value!!,
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .size(90.dp)
                        .clickable {
                            openDialog = true
                        }
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop)
                Row(modifier = Modifier.padding(2.dp)) {
                    Image(
                        painter = painterResource(R.drawable.profile_bottom_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 60.dp, top = 60.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "닉네임",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = CardDefaults.cardColors(Color.White),
                border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 13.dp, bottom = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTextField(
                        value = nickname,
                        onValueChange = {
                            nickname = it
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Black,
                            lineHeight = 31.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )
                }
            }

            Spacer(modifier = Modifier.padding(bottom = 14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "이메일", fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 21.sp
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = CardDefaults.cardColors(Color(0xffF3F3F3)),
                border = BorderStroke(1.dp, Color(0xffBFBFBF)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 13.dp, bottom = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color(0xFF9E9E9E),
                            lineHeight = 31.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        enabled = false
                    )
                }
            }

            Spacer(modifier = Modifier.padding(bottom = 40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 0.5.dp,
                        color = Color(0xff424242),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        routeAction.navTo(NAV_ROUTE.CHANGEPASSWORD)
                    }
                    .background(Color.Transparent), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "비밀번호 변경",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .width(95.dp)
                    .height(30.dp)
                    .background(
                        color = Color(0xFFE9E9E9), shape = RoundedCornerShape(size = 10.dp)
                    )
                    .clickable {
                        routeAction.navTo(NAV_ROUTE.DELETEACCOUNT)
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "계정 탈퇴하기",
                    fontWeight = FontWeight(600),
                    color = Color(0xFF9E9E9E),
                    fontSize = 12.sp,
                    lineHeight = 15.6.sp,
                )
            }
        }
    }
}

@Composable
fun setImageDialog(
    onDismissRequest: () -> Unit,
    image: MutableState<MultipartBody.Part?>,
    context: Context,
    imdel: MutableState<Boolean>,
    bitmap: (ImageBitmap?) -> Unit
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri.let {

                if (Build.VERSION.SDK_INT < 28) {
                    bitmap(
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                            .asImageBitmap()
                    )
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it!!)
                    bitmap(ImageDecoder.decodeBitmap(source).asImageBitmap())
                }


                val inputStream = context.contentResolver.openInputStream(it!!)
                val imageBytes = inputStream?.buffered()?.use { it.readBytes() }
                val encodePicture = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                val file = encodePicture?.let { base64String ->
                    val tempFile = File.createTempFile("image", null, context.cacheDir)
                    tempFile.outputStream().use { outputStream ->
                        outputStream.write(Base64.decode(base64String, Base64.DEFAULT))
                    }
                    tempFile
                }
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
                val result = MultipartBody.Part.createFormData("image", file.name, requestBody)
                image.value = result

                Log.v("setImage", "image: ${result}")
            }
        }

    Dialog(onDismissRequest = { onDismissRequest }) {
        Surface(shape = RoundedCornerShape(15.dp), color = Color.White) {
            Column(
                modifier = Modifier.width(285.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.padding(vertical = 17.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(start = 55.dp, end = 55.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffFFDAB9)),
                    onClick = {
                        launcher.launch("image/*")
                        imdel.value = false
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "앨범에서 선택",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(start = 55.dp, end = 55.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xffE9E9E9)),
                    onClick = {
                        imdel.value = true
                        bitmap(
                            BitmapFactory.decodeResource(
                                context.resources, R.drawable.defaultprofile
                            ).asImageBitmap()
                        )
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "기본 이미지로 변경",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    androidx.compose.material.TextButton(modifier = Modifier
                        .background(
                            Color(
                                0xffE9E9E9
                            )
                        )
                        .weight(1f), onClick = {
                        onDismissRequest()
                    }) {
                        Text(text = "닫기", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}