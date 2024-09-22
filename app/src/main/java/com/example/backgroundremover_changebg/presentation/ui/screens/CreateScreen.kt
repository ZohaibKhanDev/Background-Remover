package com.example.backgroundremover_changebg.presentation.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R
import com.example.backgroundremover_changebg.presentation.ui.navigation.Screens
import com.example.backgroundremover_changebg.presentation.viewmodel.MainViewModel
import dev.eren.removebg.RemoveBg
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateScreen(navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    var inputImage by remember { mutableStateOf<Bitmap?>(null) }
    var InstagramStoryImage by remember { mutableStateOf<Bitmap?>(null) }
    var InstagramPostImage by remember { mutableStateOf<Bitmap?>(null) }
    var InstagramReelImage by remember { mutableStateOf<Bitmap?>(null) }
    var Facebook_Post_Image by remember { mutableStateOf<Bitmap?>(null) }
    var TiktokVideoImage by remember { mutableStateOf<Bitmap?>(null) }
    var FacebookStoryImage by remember { mutableStateOf<Bitmap?>(null) }
    var TiktokAdImage by remember { mutableStateOf<Bitmap?>(null) }
    var outputImage by remember { mutableStateOf<Bitmap?>(null) }
    var WhiteColorsInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var BlackColorsInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var TransparentColorsInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var originalColorsInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var BlurInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var motionInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var lowKeyInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var heighKeyInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var sepiaInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var ColorSplashInputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic1InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic2InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic3InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic4InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic5InputImage by remember { mutableStateOf<Bitmap?>(null) }
    var pic6InputImage by remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val sharedViewModel: MainViewModel = koinInject()
    val secondRowItems = listOf(
        EditingOption("White", R.drawable.white),
        EditingOption("Black", R.drawable.black),
        EditingOption("Transparent", R.drawable.transparent),
        EditingOption("Original", R.drawable.orignal)
    )

    var pro by remember {
        mutableStateOf(false)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            inputImage = bitmap
            sharedViewModel.setOriginalBitmap(inputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(inputImage!!).collect { output ->
                    outputImage = output
                    sharedViewModel.setBgRemovedBitmap(outputImage!!)
                    navController.navigate(Screens.BgDetail.route)
                }
            }
        }
    }

    val InstagramStoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            InstagramStoryImage = bitmap
            sharedViewModel.setOriginalBitmap(InstagramStoryImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(InstagramStoryImage!!).collect { output ->
                    InstagramStoryImage = output
                    sharedViewModel.setBgRemovedBitmap(InstagramStoryImage!!)
                    navController.navigate(Screens.InstagramStory.route)
                }
            }
        }
    }


    val InstagramPostLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            InstagramPostImage = bitmap
            sharedViewModel.setOriginalBitmap(InstagramPostImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(InstagramPostImage!!).collect { output ->
                    InstagramPostImage = output
                    sharedViewModel.setBgRemovedBitmap(InstagramPostImage!!)
                    navController.navigate(Screens.InstagramPost.route)
                }
            }
        }
    }


    val InstagramReelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            InstagramReelImage = bitmap
            sharedViewModel.setOriginalBitmap(InstagramReelImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(InstagramReelImage!!).collect { output ->
                    InstagramReelImage = output
                    sharedViewModel.setBgRemovedBitmap(InstagramReelImage!!)
                    navController.navigate(Screens.InstagramReel.route)
                }
            }
        }
    }


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
                    navController.navigate(Screens.FacebookPost.route)
                }
            }
        }
    }

    val TiktokVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            TiktokVideoImage = bitmap
            sharedViewModel.setOriginalBitmap(TiktokVideoImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(TiktokVideoImage!!).collect { output ->
                    TiktokVideoImage = output
                    sharedViewModel.setBgRemovedBitmap(TiktokVideoImage!!)
                    navController.navigate(Screens.TiktokVideo.route)
                }
            }
        }
    }


    val FacebookStoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            FacebookStoryImage = bitmap
            sharedViewModel.setOriginalBitmap(FacebookStoryImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(FacebookStoryImage!!).collect { output ->
                    FacebookStoryImage = output
                    sharedViewModel.setBgRemovedBitmap(FacebookStoryImage!!)
                    navController.navigate(Screens.FacebookStory.route)
                }
            }
        }
    }


    val TiktokAdLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            TiktokAdImage = bitmap
            sharedViewModel.setOriginalBitmap(TiktokAdImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(TiktokAdImage!!).collect { output ->
                    TiktokAdImage = output
                    sharedViewModel.setBgRemovedBitmap(TiktokAdImage!!)
                    navController.navigate(Screens.TiktokAdd.route)
                }
            }
        }
    }


    val whiteColorsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            WhiteColorsInputImage = bitmap
            sharedViewModel.setOriginalBitmap(WhiteColorsInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(WhiteColorsInputImage!!).collect { output ->
                    WhiteColorsInputImage = output
                    sharedViewModel.setBgRemovedBitmap(WhiteColorsInputImage!!)
                    navController.navigate(Screens.ColorsBgDetail.route)
                }
            }
        }
    }


    val blackColorsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            BlackColorsInputImage = bitmap
            sharedViewModel.setOriginalBitmap(BlackColorsInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(BlackColorsInputImage!!).collect { output ->
                    BlackColorsInputImage = output
                    sharedViewModel.setBgRemovedBitmap(BlackColorsInputImage!!)
                    navController.navigate(Screens.BlackColor.route)
                }
            }
        }
    }

    val transparentColorsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            TransparentColorsInputImage = bitmap
            sharedViewModel.setOriginalBitmap(TransparentColorsInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(TransparentColorsInputImage!!).collect { output ->
                    TransparentColorsInputImage = output
                    sharedViewModel.setBgRemovedBitmap(TransparentColorsInputImage!!)
                    navController.navigate(Screens.TransparentColor.route)
                }
            }
        }
    }

    val originalColorsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            originalColorsInputImage = bitmap
            sharedViewModel.setOriginalBitmap(originalColorsInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(originalColorsInputImage!!).collect { output ->
                    originalColorsInputImage = output
                    sharedViewModel.setBgRemovedBitmap(originalColorsInputImage!!)
                    navController.navigate(Screens.OriginalColor.route)
                }
            }
        }
    }


    val thirdRowItems = listOf(
        EditingOption("", R.drawable.pic1),
        EditingOption("", R.drawable.pic2),
        EditingOption("", R.drawable.pic3),
        EditingOption("", R.drawable.pic4),
        EditingOption("", R.drawable.pic5),
        EditingOption("", R.drawable.pic6)
    )

    val BlurLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            BlurInputImage = bitmap
            sharedViewModel.setOriginalBitmap(BlurInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(BlurInputImage!!).collect { output ->
                    BlurInputImage = output
                    sharedViewModel.setBgRemovedBitmap(BlurInputImage!!)
                    navController.navigate(Screens.BlursScreen.route)
                }
            }
        }
    }


    val colorSplashLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            ColorSplashInputImage = bitmap
            sharedViewModel.setOriginalBitmap(ColorSplashInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(ColorSplashInputImage!!).collect { output ->
                    ColorSplashInputImage = output
                    sharedViewModel.setBgRemovedBitmap(ColorSplashInputImage!!)
                    navController.navigate(Screens.ColorSplash.route)
                }
            }
        }
    }


    val MotionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            motionInputImage = bitmap
            sharedViewModel.setOriginalBitmap(motionInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(motionInputImage!!).collect { output ->
                    motionInputImage = output
                    sharedViewModel.setBgRemovedBitmap(motionInputImage!!)
                    navController.navigate(Screens.MotionBlur.route)
                }
            }
        }
    }

    val LowKeyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            lowKeyInputImage = bitmap
            sharedViewModel.setOriginalBitmap(lowKeyInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(lowKeyInputImage!!).collect { output ->
                    lowKeyInputImage = output
                    sharedViewModel.setBgRemovedBitmap(lowKeyInputImage!!)
                    navController.navigate(Screens.LowKey.route)
                }
            }
        }
    }

    val HeighKeyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            heighKeyInputImage = bitmap
            sharedViewModel.setOriginalBitmap(heighKeyInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(heighKeyInputImage!!).collect { output ->
                    heighKeyInputImage = output
                    sharedViewModel.setBgRemovedBitmap(heighKeyInputImage!!)
                    navController.navigate(Screens.HeighKey.route)
                }
            }
        }
    }

    val sepiaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            sepiaInputImage = bitmap
            sharedViewModel.setOriginalBitmap(sepiaInputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(sepiaInputImage!!).collect { output ->
                    sepiaInputImage = output
                    sharedViewModel.setBgRemovedBitmap(sepiaInputImage!!)
                    navController.navigate(Screens.Sepia.route)
                }
            }
        }
    }


    val pic1Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic1InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic1InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic1InputImage!!).collect { output ->
                    pic1InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic1InputImage!!)
                    navController.navigate(Screens.Pic1Screen.route)
                }
            }
        }
    }

    val pic2Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic2InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic2InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic2InputImage!!).collect { output ->
                    pic2InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic2InputImage!!)
                    navController.navigate(Screens.Pic2Screen.route)
                }
            }
        }
    }

    val pic3Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic3InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic3InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic3InputImage!!).collect { output ->
                    pic3InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic3InputImage!!)
                    navController.navigate(Screens.Pic3Screen.route)
                }
            }
        }
    }

    val pic4Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic4InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic4InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic4InputImage!!).collect { output ->
                    pic4InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic4InputImage!!)
                    navController.navigate(Screens.Pic4Screen.route)
                }
            }
        }
    }

    val pic5Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic5InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic5InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic5InputImage!!).collect { output ->
                    pic5InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic5InputImage!!)
                    navController.navigate(Screens.Pic5Screen.route)
                }
            }
        }
    }

    val pic6Launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(it)
            )
            pic6InputImage = bitmap
            sharedViewModel.setOriginalBitmap(pic6InputImage!!)

            coroutineScope.launch {
                val remover = RemoveBg(context)
                remover.clearBackground(pic6InputImage!!).collect { output ->
                    pic6InputImage = output
                    sharedViewModel.setBgRemovedBitmap(pic6InputImage!!)
                    navController.navigate(Screens.Pic6Screen.route)
                }
            }
        }
    }


    Scaffold(topBar = {
        TopAppBar(title = { }, actions = {
            Image(
                painter = painterResource(id = R.drawable.questionmark),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Image(
                painter = painterResource(id = R.drawable.getpro),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
            )
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), bottom = 90.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Create",
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = searchText,
                        onValueChange = { newText ->
                            searchText = newText
                        },
                        placeholder = {
                            Text(text = "Search Photoroom Templates", color = Color.Gray)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .height(54.dp),
                        shape = RoundedCornerShape(11.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "",
                                tint = Color.Gray
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Gray
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.40f),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.40f),
                        )
                    )
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
                    val scope = rememberCoroutineScope()

                    val paymentSheetContent: @Composable ColumnScope.() -> Unit = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Unlock Pro Features",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Purchase the Pro version to access exclusive features.",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {

                            }) {
                                Text(text = "Buy Now")
                            }
                        }
                    }

                    if (pro) {
                        ModalBottomSheet(
                            sheetState = sheetState, onDismissRequest = {
                                pro = false
                            }, scrimColor = Color.Black.copy(alpha = 0.32f)
                        ) {
                            paymentSheetContent()
                        }
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val firstRowItems = listOf(
                            EditingOption("Remove Bg", R.drawable.bgremover),
                            EditingOption("Retouch", R.drawable.retouch),
                            EditingOption("Ai Bg", R.drawable.aibg_remove),
                            EditingOption("Ai Shadows", R.drawable.aishadow),
                            EditingOption("Resize", R.drawable.crop)
                        )

                        items(firstRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column {
                                Box(modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .width(78.dp)
                                    .clickable {
                                        when (option.name) {
                                            "Remove Bg" -> galleryLauncher.launch("image/*")
                                            "Resize" -> navController.navigate(Screens.CropScreen.route)
                                            "Retouch", "Ai Bg", "Ai Shadows" -> {
                                                pro = true
                                            }
                                        }
                                    }
                                    .height(70.dp)
                                    .background(Color(0XFF9eaaf7).copy(alpha = 0.20f)),
                                    contentAlignment = Alignment.Center) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = option.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(40.dp)
                                    )

                                    if (option.name == "Retouch" || option.name == "Ai Bg" || option.name == "Ai Shadows") {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .background(
                                                    Color.Red, shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "Pro",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(7.dp))
                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }


                    LazyRow(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        items(secondRowItems) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(78.dp)
                                        .height(70.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.name) {
                                                "White" -> whiteColorsLauncher.launch("image/*")
                                                "Black" -> blackColorsLauncher.launch("image/*")
                                                "Transparent" -> transparentColorsLauncher.launch("image/*")
                                                "Original" -> originalColorsLauncher.launch("image/*")

                                            }
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Photo Editing Classic  >",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val thirdRowItems = listOf(
                            EditingOption("Blur", R.drawable.blur),
                            EditingOption("Color Splash", R.drawable.colorsplash),
                            EditingOption("Motion", R.drawable.motion),
                            EditingOption("Low Key", R.drawable.lowkey),
                            EditingOption("Heigh Key", R.drawable.heightkey),
                            EditingOption("Sepia", R.drawable.sepia)
                        )

                        items(thirdRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.name) {
                                                "Blur" -> BlurLauncher.launch("image/*")
                                                "Color Splash" -> colorSplashLauncher.launch("image/*")
                                                "Motion" -> MotionLauncher.launch("image/*")
                                                "Low Key" -> LowKeyLauncher.launch("image/*")
                                                "Heigh Key" -> HeighKeyLauncher.launch("image/*")
                                                "Sepia" -> sepiaLauncher.launch("image/*")

                                            }
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(7.dp))

                                Text(
                                    text = option.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }


                    Text(
                        text = "Profile Pics  >",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp)
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(thirdRowItems.filter { option ->
                            option.name.contains(searchText.text, ignoreCase = true)
                        }) { option ->
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .width(80.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0XFF9eaaf7).copy(alpha = 0.20f))
                                        .clickable {
                                            when (option.icon) {
                                                R.drawable.pic1 -> pic1Launcher.launch("image/*")
                                                R.drawable.pic2 -> pic2Launcher.launch("image/*")
                                                R.drawable.pic3 -> pic3Launcher.launch("image/*")
                                                R.drawable.pic4 -> pic4Launcher.launch("image/*")
                                                R.drawable.pic5 -> pic5Launcher.launch("image/*")
                                                R.drawable.pic6 -> pic6Launcher.launch("image/*")

                                            }
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = option.icon),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }


                    Text(
                        text = "Social Media >",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 10.dp)
                    )


                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .clickable {
                                            navController.navigate(Screens.InstagramStory.route)
                                        }
                                        .height(140.dp)
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.instalogo),
                                        contentDescription = "Instagram Story",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Instagram Story",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable { navController.navigate(Screens.InstagramPost.route) }
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.instalogo),
                                        contentDescription = "Instagram Post",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Instagram Post",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .clickable {
                                            navController.navigate(Screens.InstagramReel.route)
                                        }
                                        .height(120.dp)
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.instalogo),
                                        contentDescription = "Instagram Reel",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Instagram Reel",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(100.dp)
                                        .clickable { navController.navigate(Screens.FacebookPost.route) }
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.fblogo),
                                        contentDescription = "Facebook Post",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Facebook Post",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .clickable {
                                            navController.navigate(Screens.TiktokVideo.route)
                                        }
                                        .height(140.dp)
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.tiktoklogo),
                                        contentDescription = "TikTok Video",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "TikTok Video",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(80.dp)
                                        .clickable {
                                            navController.navigate(Screens.FacebookStory.route)
                                        }
                                        .height(160.dp)
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.fblogo),
                                        contentDescription = "Facebook Story",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "Facebook Story",
                                    fontSize = 12.sp
                                )
                            }
                        }


                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .clickable {
                                            navController.navigate(Screens.TiktokAdd.route)
                                        }
                                        .height(160.dp)
                                        .border(
                                            BorderStroke(1.dp, color = Color.Gray),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.tiktoklogo),
                                        contentDescription = "TikTok Ad",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Text(
                                    text = "TikTok Ad",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class EditingOption(val name: String, val icon: Int)