package com.example.todo_android.Navigation.Action

import androidx.navigation.NavHostController
import com.example.todo_android.Navigation.NAV_ROUTE

// 컴포즈 네비게이션 라우트 액션
class RouteAction(navHostController: NavHostController) {

    //특정 라우트 이동
    val navTo: (NAV_ROUTE) -> Unit = { route ->
        navHostController.navigate(route.routeName)
        {
            popUpTo(NAV_ROUTE.SPLASH.routeName) {
                inclusive = true
            }
        }
    }
    // 뒤로가기 이동
    val goBack: () -> Unit = {
        navHostController.navigateUp()
    }

    val customNavto: (NAV_ROUTE, String, String, Int) -> Unit = { route, name, id, color ->
        navHostController.navigate("${route.routeName}/$name/$id/$color")
    }
}