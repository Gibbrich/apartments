package com.github.gibbrich.airmee.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.gibbrich.airmee.data.db.dao.ApartmentsDao
import com.github.gibbrich.airmee.data.db.entity.DBApartment

@Database(entities = [
    DBApartment::class
], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun charactersDao(): ApartmentsDao
}