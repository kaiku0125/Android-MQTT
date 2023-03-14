package org.amobile.mqtt_k

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class MainRepository {
    private var infoDao: AlarmInfoDao
    private var allInfo: LiveData<List<AlarmInfoEntity>>

    constructor(app: Application){
        val dataBase: AlarmInfoDataBase = AlarmInfoDataBase.getInstance(app)
        infoDao = dataBase.alarmInfoDao()
        allInfo = infoDao.getAllInfo()

    }

    fun insert(info: AlarmInfoEntity){
        val job = CoroutineScope(Job()).launch {

        }

        runBlocking {

        }

        GlobalScope.launch(Dispatchers.Default) {

        }


    }

    fun update(info: AlarmInfoEntity){

    }

    fun delete(info: AlarmInfoEntity){

    }

    fun deleteAllInfo(){

    }



}