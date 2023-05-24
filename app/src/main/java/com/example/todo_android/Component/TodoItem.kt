package com.example.todo_android.Component


//@ExperimentalMaterial3Api
//@ExperimentalMaterialApi
//@Composable
//fun TodoItem(Todo: RToDoResponse) {
//
//    var checked by remember { mutableStateOf(false) }
//    val token = "Token ${MyApplication.prefs.getData("token", "")}"
//    var time = "0900"
//    var done = true
//    var color = 0
//
//    var todoList = remember {
//        mutableStateListOf<RToDoResponse>()
//    }
//
////    val coroutineScope = rememberCoroutineScope()
////    val bottomSheetScaffoldState =
////        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
//
////    val onclickCard = UpdateTodoSheet(
////        year = Todo.year,
////        month = Todo.month,
////        day = Todo.day,
////        done = Todo.done,
////        color = Todo.color,
////        id = Todo.id,
////        title = Todo.title,
////        time = Todo.time
////    )
//
//    var openDialog by remember { mutableStateOf(false) }
//
//    if (openDialog) {
//        UpdateTodoDialog(
//            year = Todo.year,
//            month = Todo.month,
//            day = Todo.day,
//            done = done,
//            color = color,
//            id = Todo.id,
//            title = Todo.title,
//            time = time
//        )
//    }
//
//    Card(
//        colors = CardDefaults.cardColors(Color.White),
//        shape = RoundedCornerShape(8.dp),
//        modifier = Modifier
//            .width(350.dp)
//            .height(50.dp)
//            .clickable {
//                Log.d("onclick", "onClick: ${Todo.id}")
//            }
////            .pointerInput(Unit) {
////                detectTapGestures(
////                    onLongPress = { deleteTodo(token, Todo.id) },
////                    onPress = {
//////                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
//////                        UpdateTodoSheet(
//////                            year = Todo.year,
//////                            month = Todo.month,
//////                            day = Todo.day,
//////                            done = done,
//////                            color = color,
//////                            id = Todo.id,
//////                            title = Todo.title,
//////                            time = time
//////                        )
//////                        onclickCard
////
////                        openDialog = !openDialog
////
////                    }
////                )
////            }
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(start = 13.dp, top = 15.dp, bottom = 15.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Checkbox(
//                checked = checked,
//                onCheckedChange = {
//                    checked = it
//                }
//            )
//
//            Text(
//                text = Todo.title,
//                fontSize = 13.sp,
//                fontStyle = FontStyle.Normal
//            )
//        }
//    }
//    Spacer(modifier = Modifier.height(6.dp))
//}
//
//@ExperimentalMaterialApi
//@ExperimentalMaterial3Api
//@Composable
//fun TodoItemList(Todo: List<RToDoResponse>) {
//
////    val token = "Token ${MyApplication.prefs.getData("token", "")}"
////    var asdf = remember { mutableStateListOf(Todo) }
//
//
//    LazyColumn {
//
//        itemsIndexed(items = Todo) { index, item ->
//
////            val dismissState = rememberDismissState(
////                confirmValueChange = {
////                    if (it == DismissValue.DismissedToStart) {
////                        deleteTodo(token, Todo[0].id)
//////                        Todo.remove(item)
////                        asdf.removeAt(index)
////                        asdf.clear()
////                    }
////                    true
////                }
////            )
//
////            SwipeToDismiss(
////                state = dismissState,
////                background = {
////                    val color = when (dismissState.dismissDirection) {
////                        DismissDirection.StartToEnd -> Color.Transparent
////                        DismissDirection.EndToStart -> Color.Red
////                        null -> Color.Transparent
////                    }
////
////                    Box(modifier = Modifier
////                        .fillMaxSize()
////                        .background(color)
////                        .padding(8.dp)) {
////                        Text(
////                            text = "삭제",
////                            modifier = Modifier.align(Alignment.CenterEnd)
////                        )
////                    }
////                },
////                dismissContent = {
////                    TodoItem(Todo = item)
////                }
////            )
////
////            Divider()
//
//            TodoItem(Todo = item)
//        }
//    }
//}

//@ExperimentalMaterialApi
//@ExperimentalMaterial3Api
//@Composable
//fun UpdateTodoSheet(
//    year: Int,
//    month: Int,
//    day: Int,
//    done: Boolean,
//    color: Int,
//    id: Int,
//    title: String,
//    time: String,
//) {
//
//    var description by remember { mutableStateOf("") }
//    val token = "Token ${MyApplication.prefs.getData("token", "")}"


//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
//
//    val coroutineScope = rememberCoroutineScope()
//
//    BottomSheetScaffold(
//        scaffoldState = bottomSheetScaffoldState,
//        sheetContent = {
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//                    .background(Color.Transparent)
//            ) {
//                Column(
//                    Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(text = "test")
//                    TopAppBar(
//                        title = { Text(text = "") },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                coroutineScope.launch {
//                                    bottomSheetScaffoldState.bottomSheetState.collapse()
//                                }
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Filled.Close,
//                                    contentDescription = "close"
//                                )
//                            }
//                        },
//                        actions = {
//                            Button(
//                                onClick = {
//                                    updateTodo(token,
//                                        year,
//                                        month,
//                                        day,
//                                        title,
//                                        done,
//                                        description,
//                                        color,
//                                        time,
//                                        id)
//                                    coroutineScope.launch {
//                                        bottomSheetScaffoldState.bottomSheetState.collapse()
//                                    }
//                                },
//                                shape = RoundedCornerShape(20.dp),
//                                modifier = Modifier
//                                    .width(45.dp)
//                                    .height(25.dp)
//                            ) {
//                                Text(
//                                    text = "저장",
//                                    modifier = Modifier.padding(6.dp))
//                            }
//                        }
//                    )
//
//
//                    Text(
//                        text = "${month} 월 ${day}일",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold)
//
//                    Divider()
//
//                    TextField(
//                        modifier = Modifier
//                            .width(340.dp)
//                            .height(65.dp),
//                        colors = TextFieldDefaults.textFieldColors(
//                            containerColor = Color(0xffF3F3F3),
//                            disabledLabelColor = Color(0xffF3F3F3),
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        ),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        shape = RoundedCornerShape(10.dp),
//                        placeholder = {
//                            Text(
//                                text = "수정할 텍스트 입력",
//                                fontSize = 16.sp,
//                                color = Color(0xffA9A9A9)
//                            )
//                        },
//                        value = description,
//                        onValueChange = {
//                            description = it
//                        }
//                    )
//                }
//            }
//        }, sheetPeekHeight = 49.dp
//    ) {
//
//        Column(
//            Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Button(onClick = {
//
//                coroutineScope.launch {
//
//                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
//                        bottomSheetScaffoldState.bottomSheetState.expand()
//                    } else {
//                        bottomSheetScaffoldState.bottomSheetState.collapse()
//                    }
//                }
//
//            }) {
//                Text(text = "Click Me", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//            }
//        }
//    }
//}

//@ExperimentalMaterial3Api
//@Composable
//fun UpdateTodoDialog(
//    year: Int,
//    month: Int,
//    day: Int,
//    done: Boolean,
//    color: Int,
//    id: Int,
//    title: String,
//    time: String,
//) {
//
//    var description by remember { mutableStateOf("") }
//    var title by remember { mutableStateOf("") }
//    val token = "Token ${MyApplication.prefs.getData("token", "")}"
//    var openDialog by remember { mutableStateOf(true) }
//
//    if (openDialog) {
//
//        Dialog(onDismissRequest = {
//            openDialog = false
//        }) {
//
//            androidx.compose.material3.Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(400.dp),
//                shape = RoundedCornerShape(12.dp),
//                color = Color.White
//            ) {
//                Column() {
//                    TopAppBar(title = { Text(text = "") }, navigationIcon = {
//                        IconButton(onClick = {
//                            openDialog = false
//                        }) {
//                            Icon(imageVector = Icons.Filled.Close, contentDescription = "close")
//                        }
//                    }, actions = {
//                        Button(
//                            onClick = {
//                                updateTodo(
//                                    token,
//                                    year,
//                                    month,
//                                    day,
//                                    title,
//                                    done,
//                                    description,
//                                    color,
//                                    time,
//                                    id
//                                )
//                                openDialog = false
//                            },
//                            shape = RoundedCornerShape(20.dp),
//                            modifier = Modifier
//                                .width(90.dp)
//                                .height(50.dp)
//                        ) {
//                            Text(text = "저장", modifier = Modifier.padding(6.dp))
//                        }
//                    })
//
//                    Spacer(modifier = Modifier.height(15.dp))
//
//                    Text(
//                        text = "${month} 월 ${day}일",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Divider()
//
//                    Text(text = "제목", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    TextField(modifier = Modifier
//                        .width(340.dp)
//                        .height(65.dp),
//                        colors = TextFieldDefaults.textFieldColors(
//                            containerColor = Color(0xffF3F3F3),
//                            disabledLabelColor = Color(0xffF3F3F3),
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        ),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        shape = RoundedCornerShape(10.dp),
//                        placeholder = {
//                            Text(text = "수정할 텍스트 입력", fontSize = 16.sp, color = Color(0xffA9A9A9))
//                        },
//                        value = title,
//                        onValueChange = {
//                            title = it
//                        })
//
//                    Spacer(modifier = Modifier.height(15.dp))
//
//                    Text(text = "디테일한 내용", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    TextField(modifier = Modifier
//                        .width(340.dp)
//                        .height(65.dp),
//                        colors = TextFieldDefaults.textFieldColors(
//                            containerColor = Color(0xffF3F3F3),
//                            disabledLabelColor = Color(0xffF3F3F3),
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        ),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        shape = RoundedCornerShape(10.dp),
//                        placeholder = {
//                            Text(text = "수정할 텍스트 입력", fontSize = 16.sp, color = Color(0xffA9A9A9))
//                        },
//                        value = description,
//                        onValueChange = {
//                            description = it
//                        })
//                }
//            }
//        }
//    }
//}