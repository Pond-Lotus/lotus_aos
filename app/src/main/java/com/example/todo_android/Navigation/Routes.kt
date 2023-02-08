package com.example.todo_android.Navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Screen.*

// 컴포즈 네비게이션 이넘(값을 가지는 이넘)
enum class NAV_ROUTE(val routeName: String, val description: String) {
    MAIN("MAIN", "메인 화면"),
    LOGIN("LOGIN", "로그인 화면"),
    REGISTER("REGISTER", "회원가입 화면"),
    AUTHCODE("AUTHCODE", "이메일 인증코드 화면"),
    AUTHEMAIL("AUTHEMAIL", "이메일 인증 화면"),
    CALENDAR("CALENDAR", "캘린더 화면"),
    PROFILE("PROFILE", "프로파일 화면")
}

@ExperimentalMaterial3Api
@Composable
fun NavigationGraph(startRoute: NAV_ROUTE = NAV_ROUTE.LOGIN) {

    // 네비게이션 컨트롤러 가져오기
    val navController = rememberNavController()

    // 네비게이션 라우트 액션
    val routeAction = remember(navController) { RouteAction(navController) }

    // NavHost 로 네비게이션 결정
    // 네비게이션 연결할 스크린들을 설정한다.
    NavHost(navController, startRoute.routeName) {

//        //라우트 이름 = 화면의 키
//        //메인
//        composable(NAV_ROUTE.MAIN.routeName) {
//            // 화면이 들어가는 부분 = 값
//            MainScreen(routeAction)
//        }

        //라우트 이름 = 화면의 키
        //로그인
        composable(NAV_ROUTE.LOGIN.routeName) {
            // 화면이 들어가는 부분 = 값
            LoginScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //회원가입
        composable(NAV_ROUTE.REGISTER.routeName) {
            // 화면이 들어가는 부분 = 값
            RegisterScreen(routeAction, )
        }

        //라우트 이름 = 화면의 키
        //이메일 인증
        composable(NAV_ROUTE.AUTHEMAIL.routeName) {
            // 화면이 들어가는 부분 = 값
            AuthEmailScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //이메일 인증 코드
        composable(NAV_ROUTE.AUTHCODE.routeName) {
            // 화면이 들어가는 부분 = 값
            AuthCodeScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //캘린더
        composable(NAV_ROUTE.CALENDAR.routeName) {
            // 화면이 들어가는 부분 = 값
            CalendarScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //프로필
        composable(NAV_ROUTE.PROFILE.routeName) {
            // 화면이 들어가는 부분 = 값
            ProfileScreen(routeAction)
        }
    }
}
