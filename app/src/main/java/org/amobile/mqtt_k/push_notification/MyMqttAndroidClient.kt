package org.amobile.mqtt_k.push_notification

import android.content.Context
import android.util.Log
import org.amobile.mqtt_k.prefs.Prefs
import org.eclipse.paho.android.service.MqttAndroidClient

class MyMqttAndroidClient private constructor(){
//    private var instance: MqttAndroidClient? = null
//    fun getInstance(ctx: Context, uri: String): MqttAndroidClient {
//        if (instance == null) {
//            instance = MqttAndroidClient(ctx, uri, Prefs.clientID)
//        }
//        return instance as MqttAndroidClient
//    }



    companion object {
        private var instance: MqttAndroidClient? = null
        fun getInstance(ctx: Context, uri: String): MqttAndroidClient {
            if (instance == null) {
                instance = MqttAndroidClient(ctx, uri, Prefs.clientID)
            }
            return instance as MqttAndroidClient
        }

        fun isConnecting() : Boolean{
            var connect = false
            if(instance != null)
                if(instance!!.isConnected){
                    connect = true
                }
            Log.e("MyMqttAndroidClient", "isConnecting: $connect")
            return connect
        }
    }


}