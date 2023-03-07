package org.amobile.mqtt_k

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.amobile.mqtt_k.push_notification.MyMqttAndroidClient
import org.amobile.mqtt_k.MQTTServiceExecutor.*

class MQTTLogic(ctx: Context) {
    companion object{
        private const val TAG = "MQTTLogic"
    }
    private val mContext : Context = ctx

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

    fun getMQTTStatusDescription(status : Int) : String{
        var description = ""

        description = when(status){
            MQTTStatus.DEFAULT.ordinal -> MQTTStatus.DEFAULT.description
            MQTTStatus.CONNECTING.ordinal -> MQTTStatus.CONNECTING.description
            MQTTStatus.CONNECTED.ordinal -> MQTTStatus.CONNECTED.description
            MQTTStatus.DISCONNECTED.ordinal -> MQTTStatus.DISCONNECTED.description
            MQTTStatus.ERROR.ordinal -> MQTTStatus.ERROR.description
            else -> {
                MQTTStatus.DEFAULT.description
            }
        }.toString()

        return description
    }

    fun getConnectedStatus() : Int{
        return MQTTStatus.CONNECTED.ordinal
    }


}