package com.example.todo_android.Screen

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ModifyRequest.ChangeNicknameAndProfileRequest
import com.example.todo_android.Request.ModifyRequest.DeleteProfileImageRequest
import com.example.todo_android.Response.ModifyResponse.ChangeNicknameAndProfileResponse
import com.example.todo_android.Response.ModifyResponse.DeleteProfileImageResponse
import com.example.todo_android.Util.MyApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

fun goChangePassword(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

//fun bitmapString(bitmap: Bitmap): String {
//    val byteArrayOutputStream = ByteArrayOutputStream()
//
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//
//    val byteArray = byteArrayOutputStream.toByteArray()
//
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)
//}

//private fun getRealPathFromURI(contentResolver: ContentResolver, uri: String): String {
//    val projection = arrayOf(MediaStore.Images.Media.DATA)
//    val cursor = contentResolver.query(Uri.parse(uri), projection, null, null, null)
//    val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//    cursor?.moveToFirst()
//    val filePath = cursor?.getString(columnIndex!!)
//    cursor?.close()
//    return filePath!!
//}


fun changeNicknameAndProfile(
    token: String,
    nickname: String,
    body: MultipartBody.Part,
    routeAction: RouteAction,
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
                routeAction.goBack()
                Log.d("changeNickname&Profile",
                    "resultCode : " + changeNicknameAndProfileResponse?.resultCode)
                Log.d("changeNickname&Profile",
                    "resultCode : " + changeNicknameAndProfileResponse?.data)


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
    var nickname by remember { mutableStateOf("") }
    val token = "Token ${MyApplication.prefs.getData("token", "")}"

    var changeNickname: String = MyApplication.prefs.getData("nickname", nickname)

    var openDialog by remember { mutableStateOf(false) }


    val imageUri = rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    val painter = rememberImagePainter(
        data = imageUri.value,
        builder = {
            if(imageUri.value == null) {
                placeholder(R.drawable.defaultprofile)
            }
        }
    )

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri: Uri? ->
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
//        setImageDialog()
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
                            if (body != null) {
                                changeNicknameAndProfile(token, changeNickname, body, routeAction)
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
//                    launcher.launch("image/*")
                    openDialog = !openDialog
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "닉네임")

        TextField(modifier = Modifier
            .width(308.dp)
            .height(54.dp),
//            value = MyApplication.prefs.getData("nickname", nickname),
            value = nickname,
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                nickname = it
            },
            placeholder = {
                Text(text = "닉네임", fontSize = 16.sp, color = Color(0xffA9A9A9))
            })

        Spacer(modifier = Modifier.height(5.dp))


        Text(text = "이메일")

        TextField(modifier = Modifier
            .width(308.dp)
            .height(54.dp),
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