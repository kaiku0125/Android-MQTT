package org.amobile.mqtt_k

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmInfoViewModel(ctx :Context) : ViewModel() {
    companion object{
        private const val TAG = "AlarmInfoViewModel"
    }


    private val mRepository = MainRepository(ctx)

//    private val _allInfoLive = MutableList(List<AlarmInfoEntity>())
    val allInfo: LiveData<List<AlarmInfoEntity>> = mRepository.allInfo


    fun insertMsg(info: AlarmInfoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.insert(info)
        }

    }

    fun deleteAllData(){
        viewModelScope.launch(Dispatchers.IO) {
//            Log.e(TAG, "deleteAllData: ${Thread.currentThread().name}")
            mRepository.deleteAllInfo()
        }

    }
}