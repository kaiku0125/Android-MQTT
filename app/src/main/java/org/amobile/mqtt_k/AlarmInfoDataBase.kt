package org.amobile.mqtt_k

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmInfoEntity::class], version = 1, exportSchema = true)
abstract class AlarmInfoDataBase : RoomDatabase() {

    companion object {
        private var instance: AlarmInfoDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AlarmInfoDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmInfoDataBase::class.java, "alarm_dataBase"
                )
                    .fallbackToDestructiveMigration()
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build()
            }
            return instance as AlarmInfoDataBase
        }


    }

    abstract fun alarmInfoDao(): AlarmInfoDao


}