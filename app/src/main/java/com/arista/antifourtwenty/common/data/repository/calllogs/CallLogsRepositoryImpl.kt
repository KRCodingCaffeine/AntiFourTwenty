package com.arista.antifourtwenty.common.data.repository.calllogs

import android.content.Context
import com.arista.antifourtwenty.common.data.local.database.AppDatabase
import com.arista.antifourtwenty.common.data.local.database.daos.CallLogDao
import com.arista.antifourtwenty.common.data.local.database.entities.CallLog
import com.arista.antifourtwenty.common.domain.repository.calllogs.CallLogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CallLogsRepositoryImpl (context: Context) : CallLogsRepository {

    private val callLogDao: CallLogDao = AppDatabase.getDatabase(context).callLogDao()

    override suspend fun insert(callLog: CallLog): Long {
        return withContext(Dispatchers.IO) {
            callLogDao.insert(callLog)
        }
    }

    override suspend fun getAllCallLogs(): List<CallLog> {
        return withContext(Dispatchers.IO) {
            callLogDao.getAllCallLogs()
        }
    }

    override suspend fun deleteCallLogById(id: Int) {
        withContext(Dispatchers.IO) {
            callLogDao.deleteCallLogById(id)
        }
    }
}