package org.amobile.mqtt_k

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmInfoViewModel(ctx :Context) : ViewModel() {
    companion object{
        private const val TAG = "AlarmInfoViewModel"
    }


    private val mRepository = MainRepository(ctx)

//    private val _allInfoLive = MutableList(List<AlarmInfoEntity>())
    val allInfo: LiveData<List<AlarmInfoEntity>> = mRepository.allInfo


    fun insertMsg(info: AlarmInfoEntity) {
        mRepository.insert(info)
    }
}