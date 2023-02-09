package com.example.todo_android.Screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Util.MyApplication

@ExperimentalMaterial3Api
@Composable
fun ChangePasswordScreen(routeAction: RouteAction) {

    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    Text(text = "현재 비밀번호")

    TextField(
        modifier = Modifier
            .width(308.dp)
            .height(54.dp),
        value = MyApplication.prefs.getData("password1", password1),
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


    Text(text = "비밀번호")

    TextField(
        modifier = Modifier
            .width(308.dp)
            .height(54.dp),
        value = MyApplication.prefs.getData("password1", password1),
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
        value = MyApplication.prefs.getData("password2", password2),
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