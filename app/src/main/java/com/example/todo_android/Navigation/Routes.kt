package com.example.todo_android.Navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_android.Navigation.Action.RouteAction
import com.example.todo_android.Screen.*

// 컴포즈 네비게이션 이넘(값을 가지는 이넘)
enum class NAV_ROUTE(val routeName: String, val description: String) {
    LOGIN("LOGIN", "로그인 화면"),
    REGISTER("REGISTER", "회원가입 화면"),
    AUTHCODE("AUTHCODE", "이메일 인증코드 화면"),
    AUTHEMAIL("AUTHEMAIL", "이메일 인증 화면"),
    CALENDAR("CALENDAR", "캘린더 화면"),
    PROFILE("PROFILE", "프로파일 화면"),
    CHANGEPASSWORD("CHANGEPASSWORD", "비밀번호 변경 화면"),
    LOTTIE("LOTTIE", "로티 화면"),
    DELETEACCOUNT("DELETEACCOUNT", "계정 탈퇴 화면"),
    SEARCHPASSWORD("SEARCHPASSWORD", "비밀번호 찾기 화면"),
    SPLASH("SPLASH", "실행 로딩 화면"),
    SELECTCATEGORY("SELECTGROUP", "그룹 설정 화면"),
    CHANGECATEGORY("CHANGECATEGORY", "그룹 이름 변경 화면"),
    SETALARM("SETALARM", "알림 설정 화면")
}

@ExperimentalMotionApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun NavigationGraph(startRoute: NAV_ROUTE = NAV_ROUTE.SPLASH) {

    // 네비게이션 컨트롤러 가져오기
    val navController = rememberNavController()

    // 네비게이션 라우트 액션
    val routeAction = remember(navController) { RouteAction(navController) }

    // NavHost 로 네비게이션 결정
    // 네비게이션 연결할 스크린들을 설정한다.
    NavHost(navController, startRoute.routeName) {

        //라우트 이름 = 화면의 키
        //실행 로딩 화면
        composable(NAV_ROUTE.SPLASH.routeName) {
            // 화면이 들어가는 부분 = 값
            SplashScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //메인
        composable(
            NAV_ROUTE.LOGIN.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.LOTTIE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                null
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.LOTTIE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                null
            }
        ) {
            // 화면이 들어가는 부분 = 값
            LoginScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //이메일 인증
        composable(
            NAV_ROUTE.AUTHEMAIL.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.LOGIN.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.AUTHCODE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.AUTHCODE.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.LOGIN.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.AUTHCODE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.AUTHCODE.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            AuthEmailScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //이메일 인증 코드
        composable(
            NAV_ROUTE.AUTHCODE.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.AUTHEMAIL.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.REGISTER.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.REGISTER.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.AUTHEMAIL.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.REGISTER.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.REGISTER.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            AuthCodeScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //회원가입
        composable(
            NAV_ROUTE.REGISTER.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.AUTHCODE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.LOTTIE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.LOTTIE.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.AUTHCODE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.LOTTIE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.LOTTIE.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            RegisterScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //로티 화면
        composable(
            NAV_ROUTE.LOTTIE.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.REGISTER.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.LOGIN.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.LOGIN.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.REGISTER.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    NAV_ROUTE.LOGIN.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.LOGIN.routeName -> null
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        targetOffset = { -it }
                    )
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            LottieScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //캘린더
        composable(NAV_ROUTE.CALENDAR.routeName) {
            // 화면이 들어가는 부분 = 값
            CalendarScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //프로필
        composable(
            NAV_ROUTE.PROFILE.routeName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {
            // 화면이 들어가는 부분 = 값
            ProfileScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //비밀번호 변경
        composable(
            NAV_ROUTE.CHANGEPASSWORD.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.PROFILE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                    )
                    else -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                    )
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.PROFILE.routeName -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.PROFILE.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                    )
                    else -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                    )
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.PROFILE.routeName -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            ChangePasswordScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //계정 탈퇴 화면
        composable(
            NAV_ROUTE.DELETEACCOUNT.routeName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                    initialOffset = { -it }
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300),
                    targetOffset = { -it }
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                    initialOffset = { -it }
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300),
                    targetOffset = { -it }
                )
            }
        ) {
            // 화면이 들어가는 부분 = 값
            DeleteAccountScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //비밀번호 찾기 화면
        composable(
            NAV_ROUTE.SEARCHPASSWORD.routeName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300),
                    initialOffset = { -it }
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                    targetOffset = { -it }
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300),
                    initialOffset = { -it }
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                    targetOffset = { -it }
                )
            }
        ) {
            // 화면이 들어가는 부분 = 값
            SearchPasswordScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //그룹 설정 화면
        composable(
            NAV_ROUTE.SELECTCATEGORY.routeName,
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.CHANGECATEGORY.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.CALENDAR.routeName -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.CHANGECATEGORY.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    NAV_ROUTE.CALENDAR.routeName -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                    else -> null
                }
            }
        ) {
            // 화면이 들어가는 부분 = 값
            CategoryScreen(routeAction)
        }

        //라우트 이름 = 화면의 키
        //그룹 설정 화면
        composable(
            "${NAV_ROUTE.CHANGECATEGORY.routeName}/{categoryName}/{categoryId}/{categoryColor}",
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType },
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("categoryColor") { type = NavType.IntType }),
            enterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.SELECTCATEGORY.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    NAV_ROUTE.SELECTCATEGORY.routeName -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300),
                        initialOffset = { -it }
                    )
                    else -> null
                }
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) { backStackEntry ->
            // 화면이 들어가는 부분 = 값
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val categoryColor = backStackEntry.arguments?.getInt("categoryColor")
            ChangeCategoryNameScreen(routeAction, categoryName, categoryId, categoryColor)
        }

        //라우트 이름 = 화면의 키
        //알림 설정 화면
        composable(
            NAV_ROUTE.SETALARM.routeName,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {
            // 화면이 들어가는 부분 = 값
            SetAlarmScreen(routeAction)
        }
    }
}
