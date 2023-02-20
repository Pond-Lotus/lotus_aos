package com.example.todo_android.Screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ModifyRequest.ChangeNicknameAndProfileRequest
import com.example.todo_android.Response.ModifyResponse.ChangeNicknameAndProfileResponse
import com.example.todo_android.Util.MyApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.ByteString.Companion.decodeBase64
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun goChangePassword(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

fun changeNicknameAndProfile(
    token: String,
    nickname: String,
    body: MultipartBody.Part,
    routeAction: RouteAction,
    response: (ChangeNicknameAndProfileResponse?) -> Unit,
) {

    var changeNicknameAndProfileResponse: ChangeNicknameAndProfileResponse? = null


    var retrofit = Retrofit.Builder().baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    var changeNicknameAndProfileRequest: ChangeNicknameAndProfileRequest =
        retrofit.create(ChangeNicknameAndProfileRequest::class.java)

    changeNicknameAndProfileRequest.requestChangeNicknameAndProfile(token, nickname, body)
        .enqueue(object : Callback<ChangeNicknameAndProfileResponse> {

            // 성공 했을때
            override fun onResponse(
                call: Call<ChangeNicknameAndProfileResponse>,
                response: Response<ChangeNicknameAndProfileResponse>,
            ) {
                changeNicknameAndProfileResponse = response.body()

                when (changeNicknameAndProfileResponse?.resultCode) {
                    "200" -> {

                        MyApplication.prefs.setData("nickname", nickname)
                        MyApplication.prefs.setData("image",
                            changeNicknameAndProfileResponse!!.data.image)
                        response(changeNicknameAndProfileResponse)
//                        for (i in changeNicknameAndProfileResponse!!.data.image) {
//                            val base64String = i.toString()
//                            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
//                            val decodedImageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//                            decodedImage.value = decodedImageBitmap
//                        }
                        routeAction.goBack()

                        Log.d("changeNickname&Profile",
                            "resultCode : " + changeNicknameAndProfileResponse?.resultCode)
                        Log.d("changeNickname&Profile",
                            "resultCode : " + changeNicknameAndProfileResponse?.data)
                    }
                    "500" -> {
                        Log.d("changeNickname&Profile",
                            "resultCode : " + changeNicknameAndProfileResponse?.resultCode)
                    }
                }
            }

            // 실패 했을때
            override fun onFailure(call: Call<ChangeNicknameAndProfileResponse>, t: Throwable) {
                Log.e("changeNickname&Profile", t.message.toString())
            }
        })
}

@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }

    var nickname by remember { mutableStateOf(MyApplication.prefs.getData("nickname", "")) }

    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var openDialog by remember { mutableStateOf(false) }

    val responseImage = MyApplication.prefs.getData("image", "")
    val decodedBytes = Base64.decode(responseImage, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

    val decodeFile: File? = File.createTempFile("temp", null, LocalContext.current.cacheDir)
    val outputStream = FileOutputStream(decodeFile)

    decodedImage?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()

    val imageUri = rememberSaveable {
        mutableStateOf(decodeFile?.toUri())
    }

    var painter = rememberImagePainter(
        data = imageUri.value,
//        data = decodedImage,
        builder = {
            if (imageUri.value != null) {
                placeholder(R.drawable.defaultprofile)
            }
        }
    )

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri.value = it
                Log.v("image", "image: ${uri}")
            }
        }

    val file = imageUri.value?.let { uri ->
        val contentResolver = LocalContext.current.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("image", null, LocalContext.current.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        tempFile
    }

    val requestFile = file?.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val body = requestFile?.let {
        MultipartBody.Part.createFormData("image", file.name, requestFile)
    }


    if (openDialog) {
        Dialog(
            onDismissRequest = {
                openDialog = false
            }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            openDialog = false
                            launcher.launch("image/*")
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .width(250.dp)
                            .height(45.dp)
                            .background(Color.White))
                    {
                        Text(text = "앨범에서 선택")
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = {
                            openDialog = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .width(250.dp)
                            .height(45.dp)
                            .background(Color.White))
                    {
                        Text(text = "기본 이미지로 변경")
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {

        TopAppBar(
            title = {
                Text(text = "프로필 수정")
            },
            navigationIcon = {
                IconButton(onClick = {
                    routeAction.goBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            },
            actions = {
                Text(
                    text = "완료",
                    modifier = Modifier
                        .padding(30.dp)
                        .clickable {
                            val currenNickname = MyApplication.prefs.getData("nickname", nickname)
                            if ((body != null) || !(nickname.equals(currenNickname))) {
                                changeNicknameAndProfile(token,
                                    nickname,
                                    body!!,
                                    routeAction,
                                    response = {
//                                        for (i in it!!.data.image) {
//
//
//                                        }
                                    }
                                )
                            }
                        })
            })

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painter,
            contentDescription = "profileImage",
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp)
                .clickable {
                    openDialog = !openDialog
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "닉네임")

        TextField(modifier = Modifier
            .width(308.dp)
            .height(54.dp),
            value = nickname,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                nickname = it
            },
            placeholder = {
                Text(
                    text = "닉네임",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9))
            })

        Spacer(modifier = Modifier.height(5.dp))


        Text(text = "이메일")

        TextField(modifier = Modifier
            .width(308.dp)
            .height(54.dp)
            .focusable(false),
            value = MyApplication.prefs.getData("email", email),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(text = "이메일", fontSize = 16.sp, color = Color(0xffA9A9A9))
            })

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            goChangePassword(NAV_ROUTE.CHANGEPASSWORD, routeAction)
        }) {
            Text(text = "비밀번호 변경")
        }
    }
}

//@Composable
//fun setImageDialog() {
//
//    val imageUri = rememberSaveable { mutableStateOf("") }
//    val painter = rememberImagePainter(
//        if (imageUri.value.isEmpty()) {
//            R.drawable.defaultprofile
//        } else {
//            imageUri.value
//        }
//    )
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//    ) { uri: Uri? ->
//        uri?.let {
//            imageUri.value = it.toString()
//        }
//    }
//
//    var openDialog by remember { mutableStateOf(true) }
//
//    if (openDialog) {
//        Dialog(
//            onDismissRequest = {
//                openDialog = false
//            }) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(400.dp),
//                shape = RoundedCornerShape(12.dp),
//                color = Color.White
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Button(
//                        onClick = {
//                            launcher.launch("image/*")
//                            openDialog = false
//                        },
//                        shape = RoundedCornerShape(10.dp),
//                        modifier = Modifier
//                            .width(250.dp)
//                            .height(45.dp)
//                            .background(Color.White))
//                    {
//                        Text(text = "앨범에서 선택")
//                    }
//
//                    Spacer(modifier = Modifier.height(15.dp))
//
//                    Button(
//                        onClick = {
//                            openDialog = false
//                        },
//                        shape = RoundedCornerShape(10.dp),
//                        modifier = Modifier
//                            .width(250.dp)
//                            .height(45.dp)
//                            .background(Color.White))
//                    {
//                        Text(text = "기본 이미지로 변경")
//                    }
//                }
//            }
//        }
//    }
//}