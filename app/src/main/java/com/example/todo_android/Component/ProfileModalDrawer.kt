package com.example.todo_android.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.todo_android.Navigation.Action.RouteAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileModalDrawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Text in Drawer")
            Text(text = "Text in Drawer")
            Text(text = "Text in Drawer")
            Text(text = "Text in Drawer")
            Text(text = "Text in Drawer")
            Text(text = "Text in Drawer")
            Button(onClick = {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }) {
                Text(text = "Close Drawer")
            }
        }
    }
}