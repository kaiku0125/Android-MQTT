package org.amobile.mqtt_k

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.amobile.mqtt_k.push_notification.MyMqttAndroidClient


class MQTTViewModel(ctx: Context) : ViewModel() {
    companion object{
        private const val TAG = "MQTTViewModel"
    }
    private val mContext = ctx

    private val _isMQTTRunningLive = MutableLiveData(false)
    val isMQTTRunning : LiveData<Boolean>
        get() = _isMQTTRunningLive

    fun checkMQTTConnection(){
        val connection : Boolean = MQTTServiceExecutor.isForegroundServiceRunning(mContext) and MyMqttAndroidClient.isConnecting()
        _isMQTTRunningLive.postValue(connection)
    }

    fun btnClick(){
        val change = _isMQTTRunningLive.value?.not()
        Log.e(TAG, "btnClick: $change")
        _isMQTTRunningLive.postValue(change)
        if(change == true){
            doMQTTConnection()
        }else{
            endConnection()
        }
    }

    private fun doMQTTConnection() {
        if (MQTTServiceExecutor.isForegroundServiceRunning(mContext)) {
            Log.e(TAG, "doMQTTConnection: Service is Running now!")
            return
        }

        if (MyMqttAndroidClient.isConnecting()) {
            Log.e(TAG, "doMQTTConnection: Server is connected !")
            return
        }

        val svc = Intent(mContext, MQTTServiceExecutor::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mContext.startForegroundService(svc)

    }

    private fun endConnection() {
        MQTTServiceExecutor.isPressedStop = true
        val svc = Intent(mContext, MQTTServiceExecutor::class.java)
        mContext.stopService(svc)

        Thread {
            try {
                Thread.sleep(5000)
                MQTTServiceExecutor.isPressedStop = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun a(){
        Log.e(TAG, "a: 77777777777777777")
    }


}