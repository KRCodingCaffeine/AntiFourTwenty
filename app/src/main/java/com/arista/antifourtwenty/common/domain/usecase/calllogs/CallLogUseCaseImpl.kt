package com.arista.antifourtwenty.common.domain.usecase.calllogs

import com.arista.antifourtwenty.common.data.local.database.entities.CallLog
import com.arista.antifourtwenty.common.domain.repository.calllogs.CallLogsRepository

class CallLogUseCaseImpl (private val callLogRepository: CallLogsRepository) : CallLogUseCase {

    override suspend fun addCallLog(mobile: String, date: String, time: String): Long {
        val callLog = CallLog(mobileNumber = mobile, date = date, time = time)
        return callLogRepository.insert(callLog)
    }

    override suspend fun getAllCallLogs(): List<CallLog> {
        return callLogRepository.getAllCallLogs()
    }

    override suspend fun deleteCallLog(id: Int) {
        callLogRepository.deleteCallLogById(id)
    }
}