package mx.itesm.beneficiojoven.view.ui

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.SvgDecoder

@Composable
fun rememberAppImageLoader(): ImageLoader {
    val ctx = LocalContext.current
    return remember {
        ImageLoader.Builder(ctx)
            .components { add(SvgDecoder.Factory()) }
            .crossfade(true)
            .build()
    }
}