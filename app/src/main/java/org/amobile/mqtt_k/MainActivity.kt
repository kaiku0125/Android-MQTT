package org.amobile.mqtt_k

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.amobile.mqtt_k.prefs.Prefs
import org.amobile.mqtt_k.ui.MainView
import org.amobile.mqtt_k.ui.theme.MQTT_KTheme
import kotlin.math.log

class MainActivity : ComponentActivity() {
    lateinit var statusUpdateIntentReceiver : StatusUpdateReceiver
    lateinit var viewModel : MQTTViewModel
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.load(this)
        viewModel = ViewModelProvider(this, MQTTViewModelFactory(this)).get(MQTTViewModel::class.java)

        setContent {
            MQTT_KTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView(this)
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        statusUpdateIntentReceiver = StatusUpdateReceiver(viewModel)
//        val intentFilter = IntentFilter(MQTTServiceExecutor.MQTT_STATUS_INTENT).also {
//            registerReceiver(statusUpdateIntentReceiver, it)
//        }
        val intentFilter = IntentFilter()
        intentFilter.addAction(MQTTServiceExecutor.MQTT_STATUS_INTENT)
        registerReceiver(statusUpdateIntentReceiver, intentFilter)

        Thread{
            viewModel.checkMQTTConnection(this)
        }.start()


    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(statusUpdateIntentReceiver)
    }

    class StatusUpdateReceiver(private val viewModel : MQTTViewModel) : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            val notificationData = intent?.extras
            val newStatus = notificationData?.getInt(MQTTServiceExecutor.MQTT_STATUS_MSG)
//            Log.e(TAG, "onReceive: $newStatus")

            newStatus?.let { viewModel.notifyStatusChange(it) }
            ctx?.let { viewModel.checkMQTTConnection(it) }

            if(newStatus == MQTTServiceExecutor.MQTTStatus.CONNECTED.ordinal)
                Toast.makeText(ctx, "訂閱成功", Toast.LENGTH_SHORT).show()

        }

    }



}



