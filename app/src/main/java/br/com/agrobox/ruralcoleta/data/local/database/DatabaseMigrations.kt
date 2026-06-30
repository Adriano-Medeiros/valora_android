package br.com.agrobox.ruralcoleta.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE foto_coleta ADD COLUMN observacao TEXT")
            db.execSQL("ALTER TABLE foto_benfeitoria ADD COLUMN observacao TEXT")
        }
    }
}
