package org.amobile.mqtt_k

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.amobile.mqtt_k.push_notification.MyMqttAndroidClient


class MQTTViewModel(ctx: Context) : ViewModel() {
    companion object {
        private const val TAG = "MQTTViewModel"
    }

    private val mqttLogic = MQTTLogic(ctx)
//    private val mRepository = MainRepository(ctx)

    private val _isMQTTRunningLive = MutableLiveData(false)
    val isMQTTRunning: LiveData<Boolean>
        get() = _isMQTTRunningLive

    fun checkMQTTConnection(ctx: Context) {
        val connection: Boolean =
            MQTTServiceExecutor.isForegroundServiceRunning(ctx) and MyMqttAndroidClient.isConnecting()
        _isMQTTRunningLive.postValue(connection)
        if (connection)
            notifyStatusChange(mqttLogic.getConnectedStatus())
    }

    fun btnClick() {
        val change = _isMQTTRunningLive.value?.not()
        Log.e(TAG, "btnClick: $change")
        _isMQTTRunningLive.postValue(change)
        if (change == true) {
            mqttLogic.doMQTTConnection()
        } else {
            mqttLogic.endConnection()
        }

    }

    private val _mqttStatusDescriptionLive =
        MutableLiveData(MQTTServiceExecutor.MQTTStatus.DEFAULT.description)
    val mqttStatusDescription: LiveData<String>
        get() = _mqttStatusDescriptionLive

    fun notifyStatusChange(status: Int) {
        val changeDescription = mqttLogic.getMQTTStatusDescription(status)
        _mqttStatusDescriptionLive.postValue(changeDescription)
    }

}