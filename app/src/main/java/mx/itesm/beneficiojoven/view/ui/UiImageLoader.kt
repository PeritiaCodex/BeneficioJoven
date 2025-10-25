package mx.itesm.beneficiojoven.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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