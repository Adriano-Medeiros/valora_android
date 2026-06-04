package br.com.agrobox.ruralcoleta.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.agrobox.ruralcoleta.data.local.dao.*
import br.com.agrobox.ruralcoleta.data.local.entity.*

@Database(
    entities = [
        GrupoVariavelEntity::class,
        VariavelEntity::class,
        OpcaoVariavelEntity::class,
        ModeloColetaEntity::class,
        ModeloColetaVariavelEntity::class,
        ColetaEntity::class,
        RespostaColetaEntity::class,
        BenfeitoriaEntity::class,
        FotoColetaEntity::class,
        FotoBenfeitoriaEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun grupoVariavelDao(): GrupoVariavelDao
    abstract fun variavelDao(): VariavelDao
    abstract fun opcaoVariavelDao(): OpcaoVariavelDao

    abstract fun modeloColetaDao(): ModeloColetaDao
    abstract fun modeloColetaVariavelDao(): ModeloColetaVariavelDao

    abstract fun coletaDao(): ColetaDao
    abstract fun respostaColetaDao(): RespostaColetaDao

    abstract fun benfeitoriaDao(): BenfeitoriaDao

    abstract fun fotoColetaDao(): FotoColetaDao
    abstract fun fotoBenfeitoriaDao(): FotoBenfeitoriaDao
}