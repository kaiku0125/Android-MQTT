package org.amobile.mqtt_k

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.red
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import org.amobile.mqtt_k.prefs.Prefs
import org.amobile.mqtt_k.push_notification.Connection
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


class MQTTServiceExecutor() : Service(), MqttCallback {
    companion object {
        private const val TAG = "MQTTServiceExecutor"
        private const val MAX_MQTT_CLIENT_ID_LENGTH: Int = 22
        private const val NOTIFY_USER_ID: String = "notify_user_id"
        private const val FOREGROUND_CHANNEL_ID: String = "push_foreground_channel_id"
        private const val FOREGROUND_CHANNEL_NAME: String = "push_foreground_channel_name"
        const val MQTT_STATUS_INTENT: String = "my_mqtt_intent"
        const val MQTT_STATUS_MSG: String = "my_mqtt_msg"

        var isPressedStop: Boolean = false

        fun isForegroundServiceRunning(ctx: Context): Boolean {
            var isRunning = false
            try {
                val activityManager =
                    ctx.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                for (myRunningService in activityManager.getRunningServices(Integer.MAX_VALUE)) {
                    if (MQTTServiceExecutor::class.java.name.equals(myRunningService.service.className))
                        isRunning = true;
                }
                Log.e(TAG, "isForegroundServiceRunning ➔ $isRunning")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "isForegroundServiceRunning: exception ➔ $e")
            }
            return isRunning
        }
    }

    enum class MQTTStatus(val description : String) {
        DEFAULT("..."),
        CONNECTING("連接中..."),
        CONNECTED("已連接 !"),
        DISCONNECTED("已中斷"),
        ERROR("錯誤")
    }

    lateinit var mClient: MqttAndroidClient
    var connectionStatus: MQTTStatus = MQTTStatus.DEFAULT

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1002, getNotificationChannelBuilder().build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        doConnect()
        return START_STICKY
    }

    override fun onDestroy() {
        try {
            mClient.disconnect()
        }catch (e : MqttException){
            e.printStackTrace()
        }
        super.onDestroy()
    }

    private fun doConnect() {
        val connection = Connection(false)
        mClient = connection.getMqttAndroidClient(applicationContext)
        try {
            mClient.connect(connection.getMqttConnectOptions(), null, null)
            mClient.setCallback(this)
            connectionStatus = MQTTStatus.CONNECTING
            rebroadcastStatus()
            Log.e(TAG, "rebroadcastStatus: ${connectionStatus.name}")
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.e(TAG, "Fail to connect to MQTT server! ");
        }
        delayConnect(2000)
    }

    private fun doSubscribe() {
        try {
            val listener = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    connectionStatus = MQTTStatus.CONNECTED
                    rebroadcastStatus()
                    notifyUser("Connected Success", "連線狀態", "連線已回復")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(TAG, "onFailure: subscribe")
                }
            }
            mClient.subscribe(Prefs.companyName, 0, null, listener)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.e(TAG, "Fail to subscribe! ")
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun connectionLost(cause: Throwable?) {
        Log.e(TAG, "connectionLost !")
        connectionStatus = MQTTStatus.DISCONNECTED
        rebroadcastStatus()
        notifyUser("Connected failed", "連線狀態", "連線已中斷")

        if (!isPressedStop)
            doConnect()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Log.e(TAG, "messageArrived : topic -> $topic \n message -> ${message.toString()}")
//        Log.e(TAG, "messageArrived: topic : " + topic + "\n" +
//                "message : " + message.toString())
        val data = message.toString()
//        if (!db.isDataExist(data)) {
//            notifyUser("New data received", topic, data)
//            db.addInformation(message.toString())
//        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {}

    private fun rebroadcastStatus() {
        Log.e(TAG, "rebroadcastStatus: ${connectionStatus.name}")
        val broadcastIntent = Intent()
        broadcastIntent.action = MQTT_STATUS_INTENT
        broadcastIntent.putExtra(MQTT_STATUS_MSG, connectionStatus.ordinal)
        sendBroadcast(broadcastIntent)
    }

    private fun delayConnect(milliSeconds: Long) {
        Thread {
            try {
                Thread.sleep(milliSeconds)
                if (mClient.isConnected) {
//                    Log.e(TAG, "doConnect: do subscribe !")
                    doSubscribe()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun getNotificationChannelBuilder(): Notification.Builder {
        var channel: NotificationChannel? = null
        var builder = Notification.Builder(this)

        var openActivityIntent = Intent(this, MainActivity::class.java)
        openActivityIntent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        var pi = PendingIntent.getActivity(
            this,
            0,
            openActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(
                FOREGROUND_CHANNEL_ID, FOREGROUND_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
            builder.setChannelId(FOREGROUND_CHANNEL_ID)
                .setContentIntent(pi)
                .setColor(ContextCompat.getColor(this, R.color.red))
                .setContentTitle("MQTT Service ->")
                .setContentText("Push notification is running")
                .setSmallIcon(R.drawable.ic_notification)
        }
        return builder
    }

    private fun notifyUser(alert: String, title: String, body: String) {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(this)
        builder.setSmallIcon(R.drawable.ic_notification)
        builder.setTicker(alert)
        builder.setContentTitle(title)
        builder.setContentText(body)
        builder.setWhen(System.currentTimeMillis()) //發送時間
        builder.setDefaults(Notification.DEFAULT_ALL)
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                NOTIFY_USER_ID, "Notify Test", NotificationManager.IMPORTANCE_HIGH
            )
            builder.setChannelId(NOTIFY_USER_ID)
            nm.createNotificationChannel(channel)
        } else {
            builder.setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
        }
        val notification = builder.build()
        nm.notify(123, notification)
    }


}