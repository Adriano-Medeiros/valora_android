package br.com.agrobox.ruralcoleta.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    const val DATABASE_NAME = "rural_coleta_db"

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(
                    DatabaseMigrations.MIGRATION_5_6
                )
                .fallbackToDestructiveMigrationFrom(1, 2, 3, 4)
                .build()

            INSTANCE = instance
            instance
        }
    }

    fun closeDatabase() {
        synchronized(this) {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
