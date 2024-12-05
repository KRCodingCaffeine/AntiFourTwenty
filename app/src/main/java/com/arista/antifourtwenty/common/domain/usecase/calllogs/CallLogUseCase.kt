package com.arista.antifourtwenty.common.domain.usecase.calllogs

import com.arista.antifourtwenty.common.data.local.database.entities.CallLog

interface CallLogUseCase {
    suspend fun addCallLog(mobile: String, date: String, time: String): Long
    suspend fun getAllCallLogs(): List<CallLog>
    suspend fun deleteCallLog(id: Int)
}