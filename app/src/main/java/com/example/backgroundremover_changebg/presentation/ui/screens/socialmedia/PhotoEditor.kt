package com.example.backgroundremover_changebg.presentation.ui.screens.socialmedia

import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.backgroundremover_changebg.R
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView

@Composable
fun PhotoEditorScreen(modifier: Modifier = Modifier, imageUri: Uri?) {
    val context = LocalContext.current
    var photoEditor: PhotoEditor? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                var photoEditorView = PhotoEditorView(context).apply {
                    ImageView(context).apply {
                        setImageURI(imageUri)
                    }
                }

                photoEditor = PhotoEditor.Builder(context, photoEditorView)
                    .setPinchTextScalable(true)
                    .build()

                photoEditorView

            },
            update = { view ->
                imageUri?.let {
                    val imageView = view.source
                    imageView.setImageURI(it)
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {

            }) {
                Text("Add Text")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {

            }) {
                Text("Draw")
            }
        }
    }
}
