package com.example.backgroundremover_changebg.presentation.ui.screens.blur

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.saveImageWithBackground
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LowKey(navController: NavController) {
    val viewModel: MainViewModel = koinInject()
    val bitmap by viewModel.originalBitmap.collectAsState()
    var isScanComplete by remember { mutableStateOf(false) }
    val scanAnimationOffset = remember { Animatable(0f) }

    val contrast = 1f
    val brightness = 0f

    val colorMatrix = floatArrayOf(
        0.33f * contrast, 0.33f * contrast, 0.33f * contrast, 0f, brightness,
        0.33f * contrast, 0.33f * contrast, 0.33f * contrast, 0f, brightness,
        0.33f * contrast, 0.33f * contrast, 0.33f * contrast, 0f, brightness,
        0f, 0f, 0f, 1f, 0f
    )



    LaunchedEffect(Unit) {
        launch {
            while (!isScanComplete) {
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
        isScanComplete = true
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
                        saveImageWithBackground(
                            selectedPhoto = null,
                            selectedColor = null,
                            bitmap,
                            erasedBitmap = null,
                            context = context
                        )
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
            bitmap?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(770.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        colorFilter = if (isScanComplete) ColorFilter.colorMatrix(
                            ColorMatrix(
                                colorMatrix
                            )
                        ) else {
                            null
                        }
                    )


                    if (!isScanComplete) {
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





