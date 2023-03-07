package org.amobile.mqtt_k.push_notification

import android.content.Context
import org.amobile.mqtt_k.prefs.Prefs
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

class Connection {
    companion object {
        private const val HOST = "service.amobile.com.tw"
        private const val PORT = 1883
        private const val CLIENT_ID = "s409_app"
        private const val USERNAME = "Kevin"
        private const val PASSWORD = "7777"
    }

    var tls: Boolean = false

    constructor(tls: Boolean) {
        this.tls = tls
    }

    fun getMqttAndroidClient(ctx: Context): MqttAndroidClient {
        val uri = "tcp://$HOST:$PORT"
//        return MyMqttAndroidClient(ctx, uri)
        return MyMqttAndroidClient.getInstance(ctx, uri)
//        return MqttAndroidClient(ctx, uri, Prefs.clientID)
    }

    fun getMqttConnectOptions(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.isCleanSession = false

        options.userName = Prefs.userName
        options.password = PASSWORD.toCharArray()

        return options
    }

}