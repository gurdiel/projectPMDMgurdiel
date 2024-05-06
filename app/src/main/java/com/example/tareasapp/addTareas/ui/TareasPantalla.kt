package com.example.tareasapp.addTareas.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.tareasapp.R
import com.example.tareasapp.addTareas.ui.model.TareaModel

@Composable
fun TareasPantalla(tareasViewModel: TareasViewModel) {

    val showDialogo: Boolean by tareasViewModel.showDialogo.observeAsState(false)//EN UN ESTADO?
    val showAlert: Boolean by tareasViewModel.showAlert.observeAsState(false)
    val showConfirmacion: Boolean by tareasViewModel.showConfirmacion.observeAsState(false)
    val lifacycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TareaUiState>(
        initialValue = TareaUiState.Loading,
        key1 = lifacycle,
        key2 = tareasViewModel
    ) {
        lifacycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            tareasViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TareaUiState.Error -> {}
        TareaUiState.Loading -> {
            CircularProgressIndicator()
        }

        is TareaUiState.Success -> {

            Box(modifier = Modifier.fillMaxSize()) {

                AddTareaDialogo(
                    showDialogo,
                    onDismiss = { tareasViewModel.onDialogoCerrar() },
                    onTareaAdd = { tareasViewModel.onTareaCreated(it) })
                Dialogo(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    tareasViewModel
                )

                btnDelete(
                    (uiState as TareaUiState.Success).tareas,
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp),
                    tareasViewModel
                )
                ClearDialogo(
                    show = showAlert,
                    onDismiss = { tareasViewModel.onAlertCerrar() },
                    onClearAll = { tareasViewModel.onItemClear() }
                )

                TareasList((uiState as TareaUiState.Success).tareas, tareasViewModel, showConfirmacion)
            }
        }
    }
}

//onClearAll = { tareasViewModel.onItemClear() }
@Composable
fun TareasList(tareas: List<TareaModel>, tareasViewModel: TareasViewModel, show: Boolean) {

    LazyColumn {
        items(tareas, key = { it.id }) {
            ItemTarea(tareaModel = it, tareasViewModel = tareasViewModel, show = show)
        }
    }
}

@Composable
fun ItemTarea(tareaModel: TareaModel, tareasViewModel: TareasViewModel, show: Boolean) {

    DeleteDialogo(
        show = show,
        onDismiss = { tareasViewModel.onConfirmacionCerrar() },
        onDeleteItem = { tareasViewModel.onItemEliminar(tareaModel) }
    )
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        tareasViewModel.onMostrarConfirmacionClick()
                    },
                    onDoubleTap = {
                        //Viajar a otra pantalla. Con el tareaModel y editarlo. MEJORA
                    })
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = tareaModel.tarea, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
            Checkbox(
                checked = tareaModel.selected,
                onCheckedChange = { tareasViewModel.onCheckBoxSelected(tareaModel) })
        }
    }
}

@Composable
fun Dialogo(modifier: Modifier, tareasViewModel: TareasViewModel) {

    FloatingActionButton(onClick = {
        tareasViewModel.onMostrarDialogoClick()
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, contentDescription = "Añadir")
    }

}

@Composable
fun btnDelete(tareas: List<TareaModel>, modifier: Modifier, tareasViewModel: TareasViewModel) {

    if (tareas.isNotEmpty()) {
        FloatingActionButton(
            onClick = { tareasViewModel.onMostrarAlertClick() },
            modifier = modifier
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar todo")
        }
    }
}

@Composable
fun ClearDialogo(
    show: Boolean,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                TituloDialogo(text = "¡Atención!")
                Mensaje("¿Desea eliminar todo?", R.drawable.cubo_de_basura)
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = { onClearAll() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Aceptar")
                }
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

@Composable
fun DeleteDialogo(
    show: Boolean,
    onDismiss: () -> Unit,
    onDeleteItem: () -> Unit,

) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                TituloDialogo(text = "¡Ojo!")
                Mensaje("Va a eliminar un registro de la lista", R.drawable.cubo_de_basura)
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = { onDeleteItem() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Aceptar")
                }
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

@Composable
fun Mensaje(text: String, @DrawableRes drawable: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = "Eliminar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Yellow)
                .size(40.dp)
                .clip(CircleShape)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun TituloDialogo(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        color = Color.Red,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun AddTareaDialogo(show: Boolean, onDismiss: () -> Unit, onTareaAdd: (String) -> Unit) {
    var miTarea by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Recordarme de...",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = miTarea,
                    onValueChange = { miTarea = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    onTareaAdd(miTarea)
                    miTarea = ""
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Añadir tú tarea.")
                }
            }
        }
    }
}