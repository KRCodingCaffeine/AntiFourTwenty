package com.arista.antifourtwenty.common.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logdetails")
data class CallLog(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "CallID") val id: Int = 0,
    @ColumnInfo(name = "MobileNumber") val mobileNumber: String,
    @ColumnInfo(name = "Date") val date: String,
    @ColumnInfo(name = "Time") val time: String
)