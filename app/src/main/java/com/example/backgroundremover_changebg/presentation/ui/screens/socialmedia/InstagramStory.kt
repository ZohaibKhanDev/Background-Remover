package com.example.backgroundremover_changebg.presentation.ui.screens.socialmedia

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Size
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Filter
import androidx.compose.material.icons.outlined.Flip
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Redo
import androidx.compose.material.icons.outlined.RotateLeft
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import dev.eren.removebg.RemoveBg
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.PhotoFilter
import ja.burhanrashid52.photoeditor.SaveFileResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.io.File

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramStory(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bitmap by viewModel.originalBitmap.collectAsState()
    val bgbitmap by viewModel.bgRemovedBitmap.collectAsState()

    var isBlurred by remember { mutableStateOf(false) }
    var selectedTool by remember { mutableStateOf<Tool?>(null) }
    var photoEditor: PhotoEditor? by remember { mutableStateOf(null) }
    val scanAnimationOffset = remember { Animatable(0f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }
    var showColorDialog by remember { mutableStateOf(false) }
    var stikerShowDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf(false) }
    var colorshowDialog by remember { mutableStateOf(false) }
    var emojiShowDialog by remember { mutableStateOf(false) }
    var eraser by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Black) }
    val undoStack = remember { mutableStateListOf<Bitmap>() }
    var sliderPosition by remember { mutableStateOf(0f) }
    var Facebook_Post_Image by remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val sharedViewModel: MainViewModel = koinInject()
    val Facebook_Post_Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            Facebook_Post_Image = bitmap
            sharedViewModel.setOriginalBitmap(Facebook_Post_Image!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(Facebook_Post_Image!!).collect { output ->
                    Facebook_Post_Image = output
                    sharedViewModel.setBgRemovedBitmap(Facebook_Post_Image!!)
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        Facebook_Post_Image = null
    }
    LaunchedEffect(Unit) {
        launch {
            while (!isBlurred) {
                scanAnimationOffset.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
                scanAnimationOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
            }
        }
        delay(4000)
        isBlurred = true
    }


    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            Text(text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    Facebook_Post_Image = null
                    navController.navigateUp()
                })
        }, actions = {
            Icon(imageVector = Icons.Outlined.Flip,
                contentDescription = "Flip",
                modifier = Modifier.clickable {
                    photoEditor?.setFilterEffect(PhotoFilter.FLIP_HORIZONTAL)
                    saveStateToUndoStack(undoStack, bgbitmap)
                })

            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = Icons.Outlined.Undo,
                contentDescription = "Undo",
                modifier = Modifier
                    .clickable {
                        photoEditor?.redo()
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = Icons.Outlined.Redo,
                contentDescription = "Redo",
                modifier = Modifier
                    .clickable {
                        photoEditor?.undo()
                    }
            )


            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Save",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Magenta,
                modifier = Modifier.clickable {
                    scope.launch {

                        val fileName = "image_${System.currentTimeMillis()}.png"
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

                        val result = photoEditor?.saveAsFile(file.absolutePath)

                        if (result is SaveFileResult.Success) {
                            Toast.makeText(context, "Image Saved Successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Image Save Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

        })
    }, bottomBar = {

        if (eraser || filter) {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                containerColor = Color.White
            ) {
                if (eraser) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Eraser Size",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Slider(value = sliderPosition, onValueChange = { newPosition ->
                            sliderPosition = newPosition
                            photoEditor?.setBrushEraserSize(newPosition)
                        })
                    }

                }


                if (filter) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.AUTO_FIX)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.autofix),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.TINT)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.tint),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.VIGNETTE)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.vignette),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.CONTRAST)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.contrast),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.BLACK_WHITE)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.black_white),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.BRIGHTNESS)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.brightness),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.CROSS_PROCESS)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.cross_process),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.DOCUMENTARY)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.documentry),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .clickable {
                                        photoEditor?.setFilterEffect(PhotoFilter.TEMPERATURE)
                                    },

                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.temperature),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }


                    }
                }
            }
        }


    }) {
        val verticalScroll = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier
                    .padding(17.dp)
                    .width(1080.dp)
                    .height(600.dp)
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (!isBlurred) {
                    Facebook_Post_Image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (!isBlurred) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .offset(y = with(LocalDensity.current) { (scanAnimationOffset.value * 770).dp })
                                    .background(Color.Blue)
                            )
                        }
                    }
                } else {
                    bgbitmap?.let {
                        AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                            val photoEditorView = PhotoEditorView(context).apply {
                                ImageView(context).apply {
                                    setImageBitmap(it)
                                }
                            }
                            photoEditor = PhotoEditor.Builder(context, photoEditorView)
                                .setPinchTextScalable(true).build()
                            photoEditorView
                        }, update = { view ->
                            val imageView = view.source
                            imageView.setImageBitmap(it)
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                item {
                    ToolBoxItem(icon = Icons.Outlined.TextFields, label = "Text") {
                        selectedTool = Tool.Text
                        filter = false
                        showDialog = true
                        eraser = false
                    }

                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Filter, label = "Filter") {
                        filter = true
                        eraser = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Layers, label = "Layers") {
                        bgbitmap?.let { it1 -> photoEditor?.addImage(it1) }
                        filter = false
                        eraser = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Add, label = "Insert") {
                        filter = false
                        Facebook_Post_Launcher.launch("image/*")
                        eraser = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Brush, label = "Brush") {
                        selectedTool = Tool.Brush
                        colorshowDialog = true
                        filter = false
                        eraser = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.StickyNote2, label = "Sticker") {
                        selectedTool = Tool.Sticker
                        stikerShowDialog = false
                        eraser = false
                    }
                }


                item {
                    ToolBoxItem(icon = Icons.Default.RemoveCircle, label = "Eraser") {
                        photoEditor?.setBrushDrawingMode(false)
                        photoEditor?.brushEraser()
                        eraser = true
                    }
                }


                item {
                    ToolBoxItem(icon = Icons.Outlined.EmojiEmotions, label = "Emoji") {
                        emojiShowDialog = true
                        eraser = false
                    }
                }
            }

            if (selectedTool == Tool.Sticker) {
                ModalBottomSheet(onDismissRequest = { stikerShowDialog }) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(stickerList) { sticker ->
                            AsyncImage(model = sticker.resourceId,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        stikerShowDialog = false
                                        val stickerBitmap = decodeSampledBitmapFromResource(
                                            context.resources, sticker.resourceId, 100, 100
                                        )
                                        stickerBitmap?.let {
                                            photoEditor?.addImage(it)
                                            selectedTool = null
                                        }
                                    })
                        }
                    }
                }
            }

            val color = listOf(
                Color.Gray,
                Color.White,
                Color.LightGray,
                Color.DarkGray,
                Color.Blue,
                Color.Black,
                Color.Red,
                Color.Yellow,
                Color.Magenta
            )

            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false },
                    title = { Text(text = "Add Text") },
                    text = {
                        Column {
                            TextField(value = textInput,
                                onValueChange = { textInput = it },
                                label = { Text("Enter text") })
                            ColorPicker { color ->
                                textColor = color
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(color) {
                                    Box(modifier = Modifier
                                        .border(
                                            if (selectedColor == it) 2.dp else 0.dp,
                                            if (selectedColor == it) Color.DarkGray else Color.Transparent,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .size(30.dp)
                                        .background(it)
                                        .clickable {
                                            textColor = it
                                            selectedColor = it
                                            showColorDialog = true
                                        })
                                }

                            }

                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            photoEditor?.addText(textInput, textColor)
                            saveStateToUndoStack(undoStack, bgbitmap)
                            textInput = ""
                            showDialog = false
                        }) {
                            Text("Add Text")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    })
            }


            if (emojiShowDialog) {
                val emojis = listOf(
                    "😀",
                    "😃",
                    "😄",
                    "😁",
                    "😆",
                    "😅",
                    "😂",
                    "🤣",
                    "😊",
                    "😇",
                    "🥰",
                    "😍",
                    "🤩",
                    "😘",
                    "😗",
                    "😙",
                    "😚",
                    "😋",
                    "😜",
                    "😝",
                    "😛",
                    "🤑",
                    "🤗",
                    "🤭",
                    "🤫",
                    "🤔",
                    "😶",
                    "😐",
                    "😑",
                    "😬",
                    "🙄",
                    "😯",
                    "😦",
                    "😧",
                    "😮",
                    "😲",
                    "🥺",
                    "😳",
                    "😵",
                    "😱",
                    "😨",
                    "😰",
                    "😥",
                    "😓",
                    "😩",
                    "😫",
                    "😢",
                    "😭",
                    "😤",
                    "😡",
                    "😠",
                    "🤬",
                    "😈",
                    "👿",
                    "💀",
                    "👻",
                    "👽",
                    "🤖",
                    "🎃",
                    "😺",
                    "❤️",
                    "🧡",
                    "💛",
                    "💚",
                    "💙",
                    "💜",
                    "🖤",
                    "🤍",
                    "🤎",
                    "💖",
                    "💗",
                    "💓",
                    "💞",
                    "💕",
                    "💘",
                    "💌",
                    "🏳️",
                    "🏴‍☠️",
                    "🐶",
                    "🐕",
                    "🐩",
                    "🐺",
                    "🐱",
                    "🐈",
                    "🐾",
                    "🐻",
                    "🐼",
                    "🐨",
                    "🐯",
                    "🦁",
                    "🦓",
                    "🦒",
                    "🐘",
                    "🐪",
                    "🐫",
                    "🐍",
                    "🐢",
                    "🦎",
                    "🐬",
                    "🐳",
                    "🐋",
                    "🐟",
                    "🐠",
                    "🐡",
                    "🦈",
                    "🐅",
                    "🦊",
                    "🐉",
                    "🍏",
                    "🍎",
                    "🍐",
                    "🍊",
                    "🍋",
                    "🍌",
                    "🍉",
                    "🍇",
                    "🍓",
                    "🍈",
                    "🍒",
                    "🍑",
                    "🥭",
                    "🍍",
                    "🥥",
                    "🥝",
                    "🍅",
                    "🥑",
                    "🍆",
                    "🥔",
                    "🥕",
                    "🌽",
                    "🌶️",
                    "🥬",
                    "🥦",
                    "🍄",
                    "🍞",
                    "🥖",
                    "🧀",
                    "🍗",
                    "🌸",
                    "🌼",
                    "🌻",
                    "🌺",
                    "🌹",
                    "🌷",
                    "🌾",
                    "🌿",
                    "🍂",
                    "🍃",
                    "🌳",
                    "🌲",
                    "🌵",
                    "🌴",
                    "🌈",
                    "🌞",
                    "🌝",
                    "🌚",
                    "🌪️",
                    "🌈",
                    "💡",
                    "🔔",
                    "🎉",
                    "🎊",
                    "🎈",
                    "🎁",
                    "🧸",
                    "🎨",
                    "🎶",
                    "🎤",
                    "🎧",
                    "🎷",
                    "🎸",
                    "🎻",
                    "🎺",
                    "🪕",
                    "🎵",
                    "🎼",
                    "🎹",
                    "📚",
                    "📖",
                    "📘",
                    "📙",
                    "📗",
                    "📕",
                    "📓",
                    "📔",
                    "📝",
                    "✏️",
                    "✒️",
                    "🖊️",
                    "🖋️",
                    "🔍",
                    "🔎",
                    "🗝️",
                    "🔑",
                    "🔒",
                    "🔓",
                    "🔧",
                    "🔨"
                )


                ModalBottomSheet(onDismissRequest = { emojiShowDialog = false }) {
                    LazyVerticalGrid(
                        columns = GridCells.FixedSize(36.dp), modifier = Modifier.fillMaxSize()
                    ) {
                        items(emojis) { emoji ->
                            Text(text = emoji, modifier = Modifier
                                .clickable {
                                    emojiShowDialog = false
                                    photoEditor?.addEmoji(emoji)
                                }
                                .padding(8.dp))
                        }
                    }
                }
            }

            if (colorshowDialog) {
                ModalBottomSheet(onDismissRequest = { colorshowDialog = false }) {
                    if (selectedTool == Tool.Brush) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ColorPalette.forEach { color ->
                                Box(modifier = Modifier
                                    .size(50.dp)
                                    .background(color)
                                    .clickable {
                                        photoEditor?.setBrushDrawingMode(true)
                                        photoEditor?.brushColor = color.toArgb()
                                        selectedTool = null
                                        colorshowDialog = false
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}