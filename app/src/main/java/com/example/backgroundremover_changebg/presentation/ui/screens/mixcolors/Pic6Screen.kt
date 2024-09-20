package com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.saveImageWithBackground
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pic6Screen(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bgRemovedBitmap by viewModel.bgRemovedBitmap.collectAsState()
    val originalBitmap by viewModel.originalBitmap.collectAsState()
    var showOriginalImage by remember { mutableStateOf(true) }
    val scanAnimationOffset = remember { Animatable(0f) }
    val revealAnimation = remember { Animatable(0f) }
    var selectedBg by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(showOriginalImage) {
        if (!showOriginalImage) {
            revealAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
            )
        }
    }

    LaunchedEffect(Unit) {
        while (showOriginalImage) {
            scanAnimationOffset.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
            )
            scanAnimationOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(5000)
        showOriginalImage = false
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mixColorBg = listOf(
        R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4,
        R.drawable.bg5, R.drawable.bg6, R.drawable.bg7, R.drawable.bg8,
        R.drawable.bg9, R.drawable.bg10, R.drawable.bg11,
    )

    Scaffold(topBar = {
        TopAppBar(title = { /*TODO*/ }, navigationIcon = {
            Text(
                text = "Cancel",
                color = Color.Blue,
                modifier = Modifier.clickable { navController.navigateUp() })
        }, actions = {

            Text(text = "Save", color = Color.Magenta, modifier = Modifier.clickable {
                scope.launch {
                    try {
                        bgRemovedBitmap?.let { bitmap ->
                            val circularBitmap = clipBitmapToCircle(bitmap)
                            selectedBg?.let { bgResId ->
                                saveImageToGallery(
                                    context,
                                    bgResId,
                                    circularBitmap,
                                    "ProfilePic_${System.currentTimeMillis()}"
                                )
                            } ?: run {
                                Log.e("SaveImage", "Background resource ID is null")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SaveImage", "Error saving image: ${e.message}")
                    }
                }
            })


        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }, bottomBar = {
        BottomAppBar(containerColor = Color.White) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(mixColorBg) { bgResId ->
                    Image(
                        painter = painterResource(id = bgResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.LightGray, CircleShape)
                            .clickable {
                                selectedBg = bgResId
                            }
                    )
                }
            }
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (showOriginalImage) {
                Box(
                    modifier = Modifier
                        .size(350.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    originalBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(350.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .drawWithContent {
                                drawContent()
                                val scannerPosition = scanAnimationOffset.value * size.height
                                drawLine(
                                    color = Color.Blue,
                                    start = Offset(0f, scannerPosition),
                                    end = Offset(size.width, scannerPosition),
                                    strokeWidth = 6.dp.toPx()
                                )
                            }
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(350.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(BorderStroke(1.dp, Color.LightGray), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = selectedBg ?: R.drawable.bg6),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )

                    bgRemovedBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .alpha(revealAnimation.value)
                        )
                    }
                }
            }
        }
    }
}


fun mergeBitmapWithBackground(backgroundBitmap: Bitmap, foregroundBitmap: Bitmap): Bitmap {
    val width = max(backgroundBitmap.width, foregroundBitmap.width)
    val height = max(backgroundBitmap.height, foregroundBitmap.height)
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(output)
    val paint = Paint()

    canvas.drawBitmap(backgroundBitmap, null, Rect(0, 0, width, height), paint)

    val rect = Rect(0, 0, width, height)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(foregroundBitmap, rect, rect, paint)

    return output
}


fun clipBitmapToCircle(originalBitmap: Bitmap): Bitmap {
    val size = min(originalBitmap.width, originalBitmap.height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, size, size)
    val rectF = RectF(rect)

    paint.isAntiAlias = true

    canvas.drawOval(rectF, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(originalBitmap, rect, rect, paint)

    return output
}

suspend fun saveImageToGallery(
    context: Context,
    backgroundResId: Int, // Change to accept resource ID
    foregroundBitmap: Bitmap,
    displayName: String
) {
    // Get the background drawable from the resource ID
    val backgroundDrawable = context.getDrawable(backgroundResId)
    val backgroundBitmap = Bitmap.createBitmap(350, 350, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        backgroundDrawable?.setBounds(0, 0, width, height)
        backgroundDrawable?.draw(canvas)
    }

    // Merge the background and foreground bitmaps
    val mergedBitmap = mergeBitmapWithBackground(backgroundBitmap, foregroundBitmap)

    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
    }

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it).use { outputStream ->
            outputStream?.let { it1 -> mergedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it1) }
        }
    }
}


