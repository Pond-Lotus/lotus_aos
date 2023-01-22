package com.example.todo_android.Component

import androidx.compose.runtime.Composable

@Composable
fun SegmentedControlPage() {

    val genders = listOf("Male", "Female")


//    Box(modifier = Modifier.background(Color.White)) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(30.dp)
//                .background(Color.White),
//        ) {
//            /**
//             * Wrap size
//             */
//            Text(text = "Wrap size : ")
//            Spacer(modifier = Modifier.height(10.dp))
//            val genders = listOf("Male", "Female")
//            SegmentedControl(
//                items = genders,
//                defaultSelectedItemIndex = 0
//            ) {
//                Log.e("CustomToggle", "Selected item : ${genders[it]}")
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//            val genders4 = listOf("Male", "Female")
//            SegmentedControl(
//                items = genders4,
//                defaultSelectedItemIndex = 0,
//                cornerRadius = 50,
//                color = R.color.purple_200
//            ) {
//                Log.e("CustomToggle", "Selected item : ${genders4[it]}")
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            /**
//             * Fixed size
//             */
//            Text(text = "Fixed size : ")
//            Spacer(modifier = Modifier.height(10.dp))
//            val genders1 = listOf("Male", "Female")
//            SegmentedControl(
//                items = genders1,
//                defaultSelectedItemIndex = 0,
//                useFixedWidth = true,
//                itemWidth = 120.dp
//            ) {
//                Log.e("CustomToggle", "Selected item : ${genders1[it]}")
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//            val genders2 = listOf("Male", "Female")
//            SegmentedControl(
//                items = genders2,
//                defaultSelectedItemIndex = 0,
//                useFixedWidth = true,
//                itemWidth = 120.dp,
//                cornerRadius = 50,
//                color = R.color.purple_200
//            ) {
//                Log.e("CustomToggle", "Selected item : ${genders2[it]}")
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            /**
//             * Multiple items
//             */
//            Text(text = "Multiple items : ")
//            Spacer(modifier = Modifier.height(10.dp))
//            val items = listOf("One", "Two", "Three", "Four")
//            SegmentedControl(
//                items = items,
//                defaultSelectedItemIndex = 0
//            ) {
//                Log.e("CustomToggle", "Selected item : ${items[it]}")
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//            val items1 = listOf("One", "Two", "Three", "Four")
//            SegmentedControl(
//                items = items1,
//                defaultSelectedItemIndex = 0,
//                color = R.color.purple_200,
//                cornerRadius = 50
//            ) {
//                Log.e("CustomToggle", "Selected item : ${items1[it]}")
//            }
//        }
//    }
}

//fun SegmentedControl(items = genders, defaultSelectedItemIndex = 0) {
//    Log.e("CustomToggle", "Selected item : ${genders[it]}")
//}