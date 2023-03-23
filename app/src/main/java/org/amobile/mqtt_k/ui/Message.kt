package org.amobile.mqtt_k.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.amobile.mqtt_k.*

@Composable
fun MessageUI() {
    Log.e("TAG", "MessageUI: start")
    val ctx = LocalContext.current
    val viewModel: AlarmInfoViewModel = viewModel(factory = MessageViewModelFactory(ctx))
    val allInfo = viewModel.allInfo.observeAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            MyFAB(onClick = {
                viewModel.deleteAllData()
            })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (allInfo.value.isEmpty()) {
                Log.e("TAG", "MessageUI: database is empty")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(allInfo.value) { index, info ->
                        InfoItem(index = index, info = info)
                    }
                }
            }
        }
    }


}

@Composable
fun InfoItem(index: Int, info: AlarmInfoEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "編號 : ${(index + 1).toString()}", fontWeight = FontWeight.Bold)
            Text(text = "警報訊息 -> ${info.infoMsg}")
        }
    }
}

@Composable
fun MyFAB(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text(text = "刪除") },
        onClick = { onClick() },
        icon = { Icon(Icons.Filled.Delete, "") }
    )
}