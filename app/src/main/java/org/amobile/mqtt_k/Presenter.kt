package org.amobile.mqtt_k

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.amobile.mqtt_k.push_notification.MyMqttAndroidClient
import kotlin.math.log

class Presenter {
    companion object {
        private const val TAG = "Presenter"
    }

    var mContext: Context


    constructor(context: Context) {
        this.mContext = context
    }

    fun doMQTTConnection() {
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

    fun endConnection() {
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

    fun isMQTTRunning() : Boolean{
        var isRunning : Boolean = false
        isRunning = MQTTServiceExecutor.isForegroundServiceRunning(mContext) and MyMqttAndroidClient.isConnecting()



        return isRunning

    }
}