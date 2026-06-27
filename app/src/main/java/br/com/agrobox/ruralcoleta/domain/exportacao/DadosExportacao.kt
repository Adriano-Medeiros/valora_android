package br.com.agrobox.ruralcoleta.domain.exportacao

import br.com.agrobox.ruralcoleta.data.local.entity.BenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoBenfeitoriaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.FotoColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.ModeloColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.RespostaColetaEntity
import br.com.agrobox.ruralcoleta.data.local.entity.VariavelEntity

data class DadosExportacao(
    val modelo: ModeloColetaEntity?,
    val nomeModelo: String,
    val coletas: List<ColetaEntity>,
    val variaveis: List<VariavelEntity>,
    val respostasPorColeta: Map<Long, List<RespostaColetaEntity>>,
    val fotoPrincipalPorColeta: Map<Long, String?>,
    val fotosBenfeitoriasPorColeta: Map<Long, List<String>>,
    val fotosGeraisPorColeta: Map<Long, List<FotoColetaEntity>>,
    val benfeitoriasPorColeta: Map<Long, List<BenfeitoriaEntity>>,
    val fotosPorBenfeitoria: Map<Long, List<FotoBenfeitoriaEntity>>
)
