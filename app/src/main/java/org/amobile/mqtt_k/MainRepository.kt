package org.amobile.mqtt_k

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*

class MainRepository {
    private val TAG = "MainRepository"
    var infoDao: AlarmInfoDao
    var allInfo: LiveData<List<AlarmInfoEntity>>

    constructor(ctx : Context){
        val dataBase: AlarmInfoDataBase = AlarmInfoDataBase.getInstance(ctx)
        infoDao = dataBase.alarmInfoDao()
        allInfo = infoDao.getAllInfo()
        Log.e(TAG, "allInfo ➔ ${allInfo.value?.get(0)?.infoMsg}")
    }

    fun insert(info: AlarmInfoEntity){

        GlobalScope.launch(Dispatchers.IO) {
            Log.e("TAG", "insert: before")
            infoDao.insert(info)
            Log.e("TAG", "insert: after")
        }

    }

    fun update(info: AlarmInfoEntity){

    }

    fun delete(info: AlarmInfoEntity){

    }

    fun deleteAllInfo(){

    }



}