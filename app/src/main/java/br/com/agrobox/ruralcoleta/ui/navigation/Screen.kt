package br.com.agrobox.ruralcoleta.ui.navigation

sealed class Screen(
    val route: String
) {

    data object Splash : Screen("splash")
    data object Dashboard : Screen("dashboard")
    data object NovaColeta : Screen("nova_coleta")
    data object DadosGeraisColeta : Screen("dados_gerais_coleta")
    data object Configuracoes : Screen("configuracoes")

    data object GruposVariaveis : Screen("grupos_variaveis")
    data object NovoGrupoVariavel : Screen("novo_grupo_variavel")
    data object EditarGrupoVariavel : Screen("editar_grupo_variavel/{grupoId}") {
        fun createRoute(grupoId: Long): String {
            return "editar_grupo_variavel/$grupoId"
        }
    }

    data object Variaveis : Screen("variaveis")
    data object NovaVariavel : Screen("nova_variavel")
    data object EditarVariavel : Screen("editar_variavel/{variavelId}") {
        fun createRoute(variavelId: Long): String {
            return "editar_variavel/$variavelId"
        }
    }

    data object ModelosColeta : Screen("modelos_coleta")
    data object NovoModeloColeta : Screen("novo_modelo_coleta")
    data object EditarModeloColeta : Screen("editar_modelo_coleta/{modeloId}") {
        fun createRoute(modeloId: Long): String {
            return "editar_modelo_coleta/$modeloId"
        }
    }

    data object SelecionarModeloColeta : Screen("selecionar_modelo_coleta")

    data object FormularioDinamicoColeta : Screen("formulario_dinamico_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "formulario_dinamico_coleta/$coletaId"
        }
    }

    data object NovaBenfeitoria : Screen("nova_benfeitoria/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "nova_benfeitoria/$coletaId"
        }
    }

    data object EditarBenfeitoria : Screen("editar_benfeitoria/{coletaId}/{benfeitoriaId}") {
        fun createRoute(
            coletaId: Long,
            benfeitoriaId: Long
        ): String {
            return "editar_benfeitoria/$coletaId/$benfeitoriaId"
        }
    }

    data object BenfeitoriasColeta : Screen("benfeitorias_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "benfeitorias_coleta/$coletaId"
        }
    }

    data object FotosColeta : Screen("fotos_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "fotos_coleta/$coletaId"
        }
    }

    data object FotosBenfeitoria : Screen("fotos_benfeitoria/{benfeitoriaId}") {
        fun createRoute(benfeitoriaId: Long): String {
            return "fotos_benfeitoria/$benfeitoriaId"
        }
    }

    data object ObservacoesFinaisColeta : Screen("observacoes_finais_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "observacoes_finais_coleta/$coletaId"
        }
    }

    data object ResumoColeta : Screen("resumo_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "resumo_coleta/$coletaId"
        }
    }

    data object Coletas : Screen("coletas")

    data object DetalheColeta : Screen("detalhe_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "detalhe_coleta/$coletaId"
        }
    }

    data object OpcoesVariavel : Screen("opcoes_variavel/{variavelId}") {
        fun createRoute(variavelId: Long): String {
            return "opcoes_variavel/$variavelId"
        }
    }

    data object EditarDadosGeraisColeta : Screen("editar_dados_gerais_coleta/{coletaId}") {
        fun createRoute(coletaId: Long): String {
            return "editar_dados_gerais_coleta/$coletaId"
        }
    }

    data object ExportacaoModelos : Screen("exportacao_modelos")

    data object ExportacaoColetas : Screen("exportacao_coletas/{modeloId}") {
        fun createRoute(modeloId: Long): String {
            return "exportacao_coletas/$modeloId"
        }
    }

    data object Preferencias : Screen("preferencias")
    data object SobreApp : Screen("sobre_app")
    data object Mapa : Screen("mapa")
}
