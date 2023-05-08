package com.example.todo_android.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Navigation.NAV_ROUTE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.todo_android.R
import com.example.todo_android.Util.MyApplication

fun goDetailProfile(route: NAV_ROUTE, routeAction: RouteAction) {
    routeAction.navTo(route)
}

@ExperimentalMaterialApi
@Composable
fun ProfileModalDrawer(
    scope: CoroutineScope,
    bottomScaffoldState: BottomSheetScaffoldState,
    routeAction: RouteAction
) {

    val nickname: String = MyApplication.prefs.getData("nickname", "")
    val email: String = MyApplication.prefs.getData("email", "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 17.dp, end = 17.dp, top = 73.dp),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 21.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(41.dp),
                painter = painterResource(id = R.drawable.defaultprofile),
                contentDescription = "profile",
                contentScale = ContentScale.Crop)

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {
                Text(text = nickname,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp)
                Text(text = email,
                    color = Color(0xff9E9E9E),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp)
            }

            IconButton(onClick = {
                scope.launch {
                    bottomScaffoldState.drawerState.close()
                }
                goDetailProfile(NAV_ROUTE.PROFILE, routeAction)
            }) {
                Icon(modifier = Modifier
                    .size(24.dp)
                    .padding(start = 5.dp),
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null)
            }
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 21.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp)

        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 20.dp),
            text = "환경 설정",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            lineHeight = 17.sp)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Image(modifier = Modifier
                .size(22.dp)
                .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null)

            Text(modifier = Modifier.wrapContentWidth(),
                text = "비밀번호 변경",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 17.sp)
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Image(modifier = Modifier
                .size(22.dp)
                .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null)

            Text(modifier = Modifier.wrapContentWidth(),
                text = "알림 설정",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 17.sp)
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Image(modifier = Modifier
                .size(22.dp)
                .padding(start = 2.dp, end = 6.dp),
                painter = painterResource(id = R.drawable.setting),
                contentDescription = null)

            Text(modifier = Modifier.wrapContentWidth(),
                text = "테마 변경",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                lineHeight = 17.sp)
        }

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 21.dp),
            color = Color(0xffe9e9e9),
            thickness = 1.dp)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 19.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp),
                text = "그룹 설정",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                lineHeight = 17.sp)

            IconButton(onClick = { /*TODO*/ }) {
                Icon(modifier = Modifier.size(14.dp),
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = null)
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 28.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround) {
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB4B4)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFDCA8)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB1E0CF)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB7D7F5)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffFFB8EB)),
                onClick = {},
                content = {})
            Button(modifier = Modifier.size(width = 25.dp, height = 25.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffB6B1EC)),
                onClick = {},
                content = {})
        }

        Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xffe9e9e9), thickness = 1.dp)


//        Button(onClick = {
//            scope.launch {
//                bottomScaffoldState.drawerState.close()
//            }
//        }) {
//            Text(text = "Close Drawer")
//        }
    }
}