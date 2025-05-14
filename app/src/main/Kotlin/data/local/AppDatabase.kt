package data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [UniversalItemClass_BD::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Convertor_BD::class)
abstract class Library_DB : RoomDatabase() {

    abstract fun getDao(): LibraryDao

    companion object{
        @Volatile
        private var INSTANCE: Library_DB? = null

        fun getDb(context: Context): Library_DB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Library_DB::class.java,
                    "test.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}