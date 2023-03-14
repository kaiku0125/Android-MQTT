package org.amobile.mqtt_k

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarmInfo_table")
data class AlarmInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id:Int,

    @ColumnInfo(name = "infoMsg")
    var infoMsg :String

)
