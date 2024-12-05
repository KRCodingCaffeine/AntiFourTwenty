package com.arista.antifourtwenty.common.data.local.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.arista.antifourtwenty.common.data.local.database.entities.CallLog

@Dao
interface CallLogDao {

    @Insert
    suspend fun insert(callLog: CallLog): Long

    @Query("SELECT * FROM logdetails ORDER BY CallID DESC")
    suspend fun getAllCallLogs(): List<CallLog>

    @Query("DELETE FROM logdetails WHERE CallID = :id")
    suspend fun deleteCallLogById(id: Int)
}