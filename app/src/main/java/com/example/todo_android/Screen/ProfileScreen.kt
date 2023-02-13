package com.example.todo_android.Screen

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import com.example.todo_android.R
import com.example.todo_android.Request.ModifyRequest.ChangePasswordRequest
import com.example.todo_android.Request.ModifyRequest.DeleteProfileImageRequest
import com.example.todo_android.Request.ProfileRequest.RegisterRequest
import com.example.todo_android.Request.TodoRequest.DeleteTodoRequest
import com.example.todo_android.Response.ModifyResponse.ChangePasswordResponse
import com.example.todo_android.Response.ModifyResponse.DeleteProfileImageResponse
import com.example.todo_android.Response.ProfileResponse.RegisterResponse
import com.example.todo_android.Response.TodoResponse.DeleteTodoResponse
import com.example.todo_android.Util.MyApplication
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

fun goChangePassword(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}



fun changeNicknameAndProfile() {

}

fun deleteProfileImage(token: String) {

    var deleteProfileImageResponse: DeleteProfileImageResponse? = null

    var retrofit = Retrofit.Builder()
        .baseUrl("https://plotustodo-ctzhc.run.goorm.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var deleteProfileImageRequest: DeleteProfileImageRequest =
        retrofit.create(DeleteProfileImageRequest::class.java)

    deleteProfileImageRequest.requestDeleteProfileImage(token)
        .enqueue(object : Callback<DeleteProfileImageResponse> {

            // 성공 했을때
            override fun onResponse(
                call: Call<DeleteProfileImageResponse>,
                response: Response<DeleteProfileImageResponse>,
            ) {
                deleteProfileImageResponse = response.body()

                Log.d("deleteProfileImage",
                    "resultCode : " + deleteProfileImageResponse?.resultCode)
            }

            // 실패 했을때
            override fun onFailure(call: Call<DeleteProfileImageResponse>, t: Throwable) {
                Log.e("deleteProfileImage", t.message.toString())
            }
        })
}

@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }


    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TopAppBar(
            title = { Text(text = "프로필 수정") },
            navigationIcon = {
                IconButton(onClick = {
                    routeAction.goBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            },
            actions = {
                Text(text = "완료", modifier = Modifier
                    .padding(30.dp)
                    .clickable {
                        /* TODO */
                    })
            }
        )

        Spacer(modifier = Modifier.height(50.dp))

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }


            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "profileImage",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(20.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { launcher.launch("image/*") }
        ) {
            Text(text = "버튼")
        }

        Spacer(modifier = Modifier.height(150.dp))

        Text(text = "닉네임")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = MyApplication.prefs.getData("nickname", nickname),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                nickname = it
            },
            placeholder = {
                Text(
                    text = "닉네임",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))


        Text(text = "이메일")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = MyApplication.prefs.getData("email", email),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xffF2F2F2),
                disabledLabelColor = Color(0xffF2F2F2),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(18.dp),
            onValueChange = {
                email = it
            },
            placeholder = {
                Text(
                    text = "이메일",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(
            onClick = {
                goChangePassword(NAV_ROUTE.CHANGEPASSWORD, routeAction)
            }
        ) {
            Text(text = "비밀번호 변경")
        }
    }
}