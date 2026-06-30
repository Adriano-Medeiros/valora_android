package br.com.agrobox.ruralcoleta.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.agrobox.ruralcoleta.data.local.dao.BenfeitoriaDao
import br.com.agrobox.ruralcoleta.data.local.dao.ColetaDao
import br.com.agrobox.ruralcoleta.data.local.dao.FotoBenfeitoriaDao
import br.com.agrobox.ruralcoleta.data.local.dao.FotoColetaDao
import br.com.agrobox.ruralcoleta.data.local.dao.GrupoVariavelDao
import br.com.agrobox.ruralcoleta.data.local.dao.ModeloColetaDao
import br.com.agrobox.ruralcoleta.data.local.dao.ModeloColetaVariavelDao
import br.com.agrobox.ruralcoleta.data.local.dao.OpcaoVariavelDao
import br.com.agrobox.ruralcoleta.data.local.dao.RespostaColetaDao
import br.com.agrobox.ruralcoleta.data.local.dao.VariavelDao
import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.GrupoVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.OpcaoVariavelEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

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
    version = 6,
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
