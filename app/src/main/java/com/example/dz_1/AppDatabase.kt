package com.example.dz_1

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import interfaces.LibraryDao

@Database(
    entities = [UniversalItemClass_BD::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertor_BD::class)
abstract class FirstDB : RoomDatabase() {

    abstract fun getDao(): LibraryDao

    companion object{
        @Volatile
        private var INSTANCE: FirstDB? = null

        fun getDb(context: Context): FirstDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FirstDB::class.java,
                    "test.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}