package com.example.backgroundremover_changebg.presentation.ui.screens

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.backgroundremover_changebg.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatchScreen(navController: NavController) {
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
                .fillMaxSize()
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
                        text = "Batch",
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = "Saving time by editing up to dozens of images at once",
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .align(Alignment.CenterHorizontally),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )


                    Spacer(modifier = Modifier.height(30.dp))

                    Image(
                        painter = painterResource(id = R.drawable.batchscreen),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Batch is a Pro feature",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Upgrade now", color = Color.White)
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Start new Batch", color = Color.White)
                    }

                }
            }
        }
    }
}



