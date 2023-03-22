package org.amobile.mqtt_k.ui

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.amobile.mqtt_k.*
import kotlin.math.log

@Composable
fun MessageUI() {
    Log.e("TAG", "MessageUI: start")
    val ctx = LocalContext.current
    val viewModel: AlarmInfoViewModel = viewModel(factory = MessageViewModelFactory(ctx))
//    val allinfo = viewModel.allInfo.observe(LocalLifecycleOwner.current,

    val allInfo = viewModel.allInfo.observeAsState(emptyList())
//    Log.e("TAG", "MessageUI: ${allInfo.value.size}")

    Column(modifier = Modifier.fillMaxWidth()) {
        if (allInfo.value.isEmpty()) {
            Log.e("TAG", "MessageUI: database is empty")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(allInfo.value) { info ->
                    UserItem(info = info)
                }
            }
        }
    }



}

@Composable
fun UserItem(info: AlarmInfoEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${info.id}", fontWeight = FontWeight.Bold)
            Text(text = "Age: ${info.infoMsg}")
        }
    }
}