package org.amobile.mqtt_k

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmInfoDao {

    @Insert
    fun insert(info : AlarmInfoEntity)

    @Update
    fun update(info: AlarmInfoEntity)

    @Delete
    fun delete(info: AlarmInfoEntity)

    @Query("DELETE FROM alarmInfo_table")
    fun deleteAllInfo()

    @Query("SELECT * FROM alarmInfo_table")
    fun getAllInfo() : LiveData<List<AlarmInfoEntity>>

}