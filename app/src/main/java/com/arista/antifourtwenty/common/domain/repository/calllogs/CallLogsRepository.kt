package com.arista.antifourtwenty.common.domain.repository.calllogs

import com.arista.antifourtwenty.common.data.local.database.entities.CallLog

interface CallLogsRepository {
    suspend fun insert(callLog: CallLog): Long
    suspend fun getAllCallLogs(): List<CallLog>
    suspend fun deleteCallLogById(id: Int)
}