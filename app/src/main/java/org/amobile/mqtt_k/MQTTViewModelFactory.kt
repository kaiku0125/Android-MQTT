package org.amobile.mqtt_k

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MQTTViewModelFactory(private val context : Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MQTTViewModel(context) as T
    }
}