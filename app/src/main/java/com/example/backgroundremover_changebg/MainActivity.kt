package com.example.backgroundremover_changebg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.backgroundremover_changebg.di.appModule
import com.example.backgroundremover_changebg.presentation.ui.navigation.NavEntry
import com.example.backgroundremover_changebg.ui.theme.BackgroundRemoverChangebgTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            modules(appModule)
        }
        setContent {
            BackgroundRemoverChangebgTheme {
                NavEntry()
            }
        }
    }
}


