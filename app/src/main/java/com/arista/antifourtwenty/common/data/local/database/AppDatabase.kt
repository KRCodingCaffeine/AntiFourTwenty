package com.arista.antifourtwenty.common.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arista.antifourtwenty.common.data.local.database.daos.CallLogDao
import com.arista.antifourtwenty.common.data.local.database.entities.CallLog

@Database(entities = [CallLog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun callLogDao(): CallLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "CallLogs.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}