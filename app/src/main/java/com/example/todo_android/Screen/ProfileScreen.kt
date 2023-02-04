package com.example.todo_android.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE

@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(routeAction: RouteAction) {

    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TopAppBar(
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(onClick = {
                    routeAction.goBack()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
                }
            }
        )


        Text(text = "이메일")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = email,
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

        Text(text = "닉네임")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = nickname,
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

        Text(text = "비밀번호")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = password1,
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
                password1 = it
            },
            placeholder = {
                Text(
                    text = "비밀번호",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = "비밀번호 확인")

        TextField(
            modifier = Modifier
                .width(308.dp)
                .height(54.dp),
            value = password2,
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
                password2 = it
            },
            placeholder = {
                Text(
                    text = "비밀번호 확인",
                    fontSize = 16.sp,
                    color = Color(0xffA9A9A9)
                )
            }
        )

        Spacer(modifier = Modifier.height(5.dp))
    }
}