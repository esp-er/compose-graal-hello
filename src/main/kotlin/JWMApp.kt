import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import club.eridani.compose.jwm.ApplicationWindow

import io.github.humbleui.jwm.App
import kotlin.system.exitProcess


fun main() {
    App.start {
        val winSize = IntSize(300, 300)
        val app = ApplicationWindow(
            onClose = { exitProcess(0) },
            title = "Compose JWM",
            winSize = winSize,
            winPos = IntSize(600, 600)
        ) {

            val count = remember { mutableStateOf(0) }
            MaterialTheme {
                Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            count.value++
                        }) {
                        Text(if (count.value == 0) "Hello World" else "Clicked ${count.value}!")
                    }
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            count.value = 0
                        }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}