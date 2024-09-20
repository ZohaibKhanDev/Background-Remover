package com.example.backgroundremover_changebg.presentation.ui.screens.socialmedia

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facebook_Post(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bitmap by viewModel.originalBitmap.collectAsState()
    val bgbitmap by viewModel.bgRemovedBitmap.collectAsState()

    var isBlurred by remember { mutableStateOf(false) }
    val scanAnimationOffset = remember { Animatable(0f) }


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

    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            Text(text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigateUp()
                })
        }, actions = {
            Text(text = "Save", color = Color.Magenta, modifier = Modifier.clickable {
                scope.launch {
                    downloadImageWithSize(bitmap.toString(), size = Size(370, 282))
                }
            })
        })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            if (!isBlurred) {
                bitmap?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(482.dp)
                            .border(
                                BorderStroke(1.dp, color = Color.Gray),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray)
                    ) {
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
                                    .height(6.dp)
                                    .offset(y = with(LocalDensity.current) {
                                        (scanAnimationOffset.value * 770).dp
                                    })
                                    .background(Color.Blue)
                            )
                        }
                    }
                }
            } else {
                bgbitmap?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(482.dp)
                            .border(
                                BorderStroke(1.dp, color = Color.Gray),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray)
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

suspend fun downloadImageWithSize(url: String, size: Size): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = URL(url).openStream()
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val scaledBitmap = Bitmap.createScaledBitmap(
                originalBitmap, size.width, size.height, true
            )
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
