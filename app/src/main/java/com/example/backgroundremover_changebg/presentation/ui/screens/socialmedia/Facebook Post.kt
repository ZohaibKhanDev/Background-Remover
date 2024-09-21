package com.example.backgroundremover_changebg.presentation.ui.screens.socialmedia

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Filter
import androidx.compose.material.icons.outlined.Flip
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.RotateLeft
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.PhotoFilter
import ja.burhanrashid52.photoeditor.ViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.net.URL


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facebook_Post(navController: NavController) {
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
    var filter by remember { mutableStateOf(false) }
    var colorshowDialog by remember { mutableStateOf(false) }
    var emojiShowDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    var textColor by remember { mutableStateOf(Color.Black) }
    val undoStack = remember { mutableStateListOf<Bitmap>() }


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
        delay(7000)
        isBlurred = true
    }

    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            Text(text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
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
            Icon(imageVector = Icons.Outlined.RotateLeft,
                contentDescription = "Rotate",
                modifier = Modifier
                    .rotate(180f)
                    .clickable {
                        photoEditor?.setFilterEffect(PhotoFilter.ROTATE)
                        saveStateToUndoStack(undoStack, bgbitmap)
                    })
        })
    }, bottomBar = {
        if (filter) {
            BottomAppBar {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .border(BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isBlurred) {
                    bitmap?.let {
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

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(imageVector = Icons.Default.Undo,
                            contentDescription = "Undo",
                            modifier = Modifier.clickable {
                                photoEditor?.redo()
                            })
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.Redo,
                            contentDescription = "Redo",
                            modifier = Modifier.clickable {
                                photoEditor?.undo()
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
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Filter, label = "Filter") {
                        filter = true
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Layers, label = "Layers") {
                        bgbitmap?.let { it1 -> photoEditor?.addImage(it1) }
                        filter = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Edit, label = "Edit") {
                        filter = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.Brush, label = "Brush") {
                        selectedTool = Tool.Brush
                        colorshowDialog = true
                        filter = false
                    }
                }

                item {
                    ToolBoxItem(icon = Icons.Outlined.StickyNote2, label = "Sticker") {
                        selectedTool = Tool.Sticker
                        showDialog = true
                        filter = false
                    }
                }


                item {
                    ToolBoxItem(icon = Icons.Outlined.ContentCut, label = "Eraser") {
                        photoEditor?.setBrushDrawingMode(false)
                        photoEditor?.brushEraser()
                    }
                }


                item {
                    ToolBoxItem(icon = Icons.Outlined.EmojiEmotions, label = "Emoji") {
                        emojiShowDialog = true
                    }
                }
            }

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

                AlertDialog(onDismissRequest = { }, title = { Text("Select Emoji") }, text = {
                    LazyVerticalGrid(columns = GridCells.FixedSize(25.dp)) {
                        items(emojis) { emoji ->
                            Text(text = emoji, modifier = Modifier
                                .clickable {
                                    emojiShowDialog = false
                                    photoEditor?.addEmoji(emoji)
                                }
                                .padding(8.dp))
                        }
                    }
                }, confirmButton = {
                    TextButton(onClick = { emojiShowDialog = false }) {
                        Text("Close")
                    }
                })
            }

            if (colorshowDialog) {
                AlertDialog(onDismissRequest = { colorshowDialog = false }, confirmButton = {
                    Text(text = "Ok")
                }, text = {
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
                })
            }
        }
    }
}

@Composable
fun showEmojiPickerDialog(onEmojiSelected: (String) -> Unit) {

}


fun PhotoEditor.addText(text: String, color: Color) {

    this.addText(text, color.toArgb())
}


@Composable
fun ColorPicker(onColorSelected: (Color) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        listOf(
            Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black, Color.White
        )
    }
}

fun saveStateToUndoStack(undoStack: MutableList<Bitmap>, bitmap: Bitmap?) {
    bitmap?.let {
        undoStack.add(it.copy(it.config, true))
    }
}


enum class Tool {
    Text, Insert, Layers, Edit, Brush, Sticker
}


val stickerList = listOf(
    Sticker(R.drawable.pic5), Sticker(R.drawable.fblogo), Sticker(R.drawable.instalogo)
)


data class Sticker(val resourceId: Int)


val ColorPalette = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Black)


@Composable
fun ToolBoxItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .width(50.dp)
        .clickable { onClick() }
        .background(Color.White)
        .height(58.dp), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Icon(imageVector = icon, contentDescription = "")
            Text(
                text = label, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Medium
            )
        }
    }
}


private fun decodeSampledBitmapFromResource(
    res: Resources, resId: Int, reqWidth: Int, reqHeight: Int
): Bitmap? {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(res, resId, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    val halfHeight: Int = height / 2
    val halfWidth: Int = width / 2

    var inSampleSize = 1
    while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
        inSampleSize *= 2
    }
    return inSampleSize
}


suspend fun downloadImageWithSize(url: String, size: Size): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = URL(url).openStream()
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val scaledBitmap =
                Bitmap.createScaledBitmap(originalBitmap, size.width, size.height, true)
            scaledBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

fun saveImageToMediaStore(context: Context, bitmap: Bitmap, size: Size) {
    val filename = "image_${System.currentTimeMillis()}.jpg"
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.WIDTH, size.width)
        put(MediaStore.Images.Media.HEIGHT, size.height)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
    }
}

