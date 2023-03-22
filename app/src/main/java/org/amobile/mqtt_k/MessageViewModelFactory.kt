package org.amobile.mqtt_k

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessageViewModelFactory(private val ctx :Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmInfoViewModel(ctx) as T
    }
}