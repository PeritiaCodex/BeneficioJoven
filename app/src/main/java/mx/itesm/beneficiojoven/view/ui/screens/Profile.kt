package mx.itesm.beneficiojoven.view.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mx.itesm.beneficiojoven.view.ui.theme.AppBackgroundCream
import mx.itesm.beneficiojoven.view.ui.theme.AppBarText
import mx.itesm.beneficiojoven.view.ui.theme.AppBarYellow
import mx.itesm.beneficiojoven.vm.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val vm: ProfileViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val enabled by vm.pushEnabled.collectAsState(initial = true)
    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppBackgroundCream,
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Atrás") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarYellow,
                    titleContentColor = AppBarText
                )
            )
        }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp)
        ) {
            Text("Notificaciones push")
            Switch(
                checked = enabled,
                onCheckedChange = { checked ->
                    if (!checked) {
                        showConfirm = true
                    } else {
                        scope.launch { vm.setPush(true) }
                    }
                },
                modifier = Modifier.testTag("push_switch")   // ← el único Switch con testTag
            )

            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    title = { Text("Desactivar notificaciones") },
                    text = { Text("¿Seguro que deseas desactivarlas?") },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch { vm.setPush(false) }
                            showConfirm = false
                        }) { Text("Sí") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirm = false }) { Text("No") }
                    }
                )
            }
        }
    }
}
