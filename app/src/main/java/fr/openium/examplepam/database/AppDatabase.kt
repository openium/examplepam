package fr.openium.examplepam.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.openium.examplepam.model.Call

@Database(entities = [Call::class], version = 1)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun callDao(): CallDao

    companion object {
        private var sInstance: AppDatabase? = null

        /**
         * Gets the singleton instance of SampleDatabase.
         *
         * @param context The context.
         * @return The singleton instance of SampleDatabase.
         */
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return sInstance ?: Room
                .databaseBuilder(context.applicationContext, AppDatabase::class.java, "ex")
                .allowMainThreadQueries()
                .build().also { db ->
                    sInstance = db
                }
        }
    }
}