package com.example.backgroundremover_changebg.presentation.ui.screens.blur

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.saveImageWithBackground
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SepiaScreen(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bitmap by viewModel.originalBitmap.collectAsState()

    var isBlurred by remember { mutableStateOf(false) }
    val scanAnimationOffset = remember { Animatable(0f) }

    val sepiaColorMatrix = androidx.compose.ui.graphics.ColorMatrix(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    val sepiaColorFilter = androidx.compose.ui.graphics.ColorFilter.colorMatrix(sepiaColorMatrix)


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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigateUp()
                    })
            },
            actions = {
                Text(text = "Save", color = Color.Magenta, modifier = Modifier.clickable {

                    scope.launch {
                        bitmap?.let {
                            saveImageToFile(
                                context,
                                it, isBlurred = false, isSepia = true
                            )
                        }
                    }
                })
            }
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            contentAlignment = Alignment.Center
        ) {
            bitmap?.let { originalBitmap ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(770.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Image(
                        bitmap = originalBitmap.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        colorFilter = if (isBlurred) sepiaColorFilter else null
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
        }
    }
}


suspend fun saveImageToFile(
    context: Context,
    originalBitmap: Bitmap,
    isBlurred: Boolean,
    isSepia: Boolean
): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val modifiedBitmap = if (isBlurred || isSepia) {
                applyEffectsToBitmap(originalBitmap, isBlurred, isSepia)
            } else {
                originalBitmap
            }

            val filename = "modified_image_${System.currentTimeMillis()}.jpg"
            val file = File(context.externalMediaDirs.first(), filename)

            val outStream: OutputStream = FileOutputStream(file)
            modifiedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()

            val uri = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                filename,
                "Modified image with effects"
            )

            Uri.parse(uri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}




fun applyEffectsToBitmap(
    bitmap: Bitmap,
    isBlurred: Boolean,
    isSepia: Boolean
): Bitmap {
    val modifiedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

    val canvas = android.graphics.Canvas(modifiedBitmap)
    val paint = Paint()

    if (isSepia) {
        val sepiaMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        val sepiaColorFilter = ColorMatrixColorFilter(sepiaMatrix)
        paint.colorFilter = sepiaColorFilter

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    } else {
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    if (isBlurred) {

    }

    return modifiedBitmap
}



