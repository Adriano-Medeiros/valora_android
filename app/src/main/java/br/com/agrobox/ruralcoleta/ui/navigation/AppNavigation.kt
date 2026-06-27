package br.com.agrobox.ruralcoleta.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.agrobox.ruralcoleta.data.repository.BenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.ColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoBenfeitoriaRepository
import br.com.agrobox.ruralcoleta.data.repository.FotoColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.GrupoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.ModeloColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.OpcaoVariavelRepository
import br.com.agrobox.ruralcoleta.data.repository.PreferenciasRepository
import br.com.agrobox.ruralcoleta.data.repository.RespostaColetaRepository
import br.com.agrobox.ruralcoleta.data.repository.VariavelRepository
import br.com.agrobox.ruralcoleta.ui.coleta.DadosGeraisColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.NovaColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.NovaColetaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.NovaColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.SelecionarModeloColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.BenfeitoriasScreen
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.BenfeitoriasViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.BenfeitoriasViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.NovaBenfeitoriaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos.FotosBenfeitoriaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos.FotosBenfeitoriaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.benfeitorias.fotos.FotosBenfeitoriaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.detalhe.DetalheColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.detalhe.DetalheColetaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.detalhe.DetalheColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.editar.EditarDadosGeraisColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.editar.EditarDadosGeraisColetaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.editar.EditarDadosGeraisColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.formulario.FormularioDinamicoScreen
import br.com.agrobox.ruralcoleta.ui.coleta.formulario.FormularioDinamicoViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.formulario.FormularioDinamicoViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.fotos.FotosColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.fotos.FotosColetaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.fotos.FotosColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.lista.ColetasScreen
import br.com.agrobox.ruralcoleta.ui.coleta.observacoes.ObservacoesFinaisScreen
import br.com.agrobox.ruralcoleta.ui.coleta.observacoes.ObservacoesFinaisViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.observacoes.ObservacoesFinaisViewModelFactory
import br.com.agrobox.ruralcoleta.ui.coleta.resumo.ResumoColetaScreen
import br.com.agrobox.ruralcoleta.ui.coleta.resumo.ResumoColetaViewModel
import br.com.agrobox.ruralcoleta.ui.coleta.resumo.ResumoColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.components.EmBreveScreen
import br.com.agrobox.ruralcoleta.ui.components.RuralBottomBar
import br.com.agrobox.ruralcoleta.ui.configuracoes.ConfiguracoesScreen
import br.com.agrobox.ruralcoleta.ui.configuracoes.PreferenciasScreen
import br.com.agrobox.ruralcoleta.ui.configuracoes.PreferenciasViewModel
import br.com.agrobox.ruralcoleta.ui.configuracoes.PreferenciasViewModelFactory
import br.com.agrobox.ruralcoleta.ui.configuracoes.SobreAppScreen
import br.com.agrobox.ruralcoleta.ui.dashboard.DashboardScreen
import br.com.agrobox.ruralcoleta.ui.dashboard.DashboardViewModel
import br.com.agrobox.ruralcoleta.ui.dashboard.DashboardViewModelFactory
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoColetasScreen
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoColetasViewModel
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoColetasViewModelFactory
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoViewModel
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoViewModelFactory
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoModelosScreen
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoModelosViewModel
import br.com.agrobox.ruralcoleta.ui.exportacao.ExportacaoModelosViewModelFactory
import br.com.agrobox.ruralcoleta.ui.grupo.GrupoVariavelFormScreen
import br.com.agrobox.ruralcoleta.ui.grupo.GrupoVariavelListScreen
import br.com.agrobox.ruralcoleta.ui.grupo.GrupoVariavelViewModel
import br.com.agrobox.ruralcoleta.ui.grupo.GrupoVariavelViewModelFactory
import br.com.agrobox.ruralcoleta.ui.modelo.ModeloColetaFormScreen
import br.com.agrobox.ruralcoleta.ui.modelo.ModeloColetaListScreen
import br.com.agrobox.ruralcoleta.ui.modelo.ModeloColetaViewModel
import br.com.agrobox.ruralcoleta.ui.modelo.ModeloColetaViewModelFactory
import br.com.agrobox.ruralcoleta.ui.splash.SplashScreen
import br.com.agrobox.ruralcoleta.ui.theme.VerdeEscuro
import br.com.agrobox.ruralcoleta.ui.variavel.VariavelFormScreen
import br.com.agrobox.ruralcoleta.ui.variavel.VariavelListScreen
import br.com.agrobox.ruralcoleta.ui.variavel.VariavelViewModel
import br.com.agrobox.ruralcoleta.ui.variavel.VariavelViewModelFactory
import br.com.agrobox.ruralcoleta.ui.variavel.opcoes.OpcoesVariavelScreen
import br.com.agrobox.ruralcoleta.ui.variavel.opcoes.OpcoesVariavelViewModel
import br.com.agrobox.ruralcoleta.ui.variavel.opcoes.OpcoesVariavelViewModelFactory
import br.com.agrobox.ruralcoleta.ui.mapa.MapaColetasScreen
import br.com.agrobox.ruralcoleta.ui.mapa.MapaColetasViewModel
import br.com.agrobox.ruralcoleta.ui.mapa.MapaColetasViewModelFactory
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation(
    coletaRepository: ColetaRepository,
    modifier: Modifier = Modifier,
    grupoVariavelRepository: GrupoVariavelRepository,
    variavelRepository: VariavelRepository,
    modeloColetaRepository: ModeloColetaRepository,
    respostaColetaRepository: RespostaColetaRepository,
    benfeitoriaRepository: BenfeitoriaRepository,
    fotoColetaRepository: FotoColetaRepository,
    fotoBenfeitoriaRepository: FotoBenfeitoriaRepository,
    opcaoVariavelRepository: OpcaoVariavelRepository,
    preferenciasRepository: PreferenciasRepository,
) {
    val navController = rememberNavController()
    val tutorialPrimeiroAcessoConcluido by preferenciasRepository
        .tutorialPrimeiroAcessoConcluido
        .collectAsState()

    var tutorialPrimeiroAcessoAtivo by rememberSaveable {
        mutableStateOf(false)
    }

    var tutorialEtapa by rememberSaveable {
        mutableIntStateOf(0)
    }

    LaunchedEffect(tutorialPrimeiroAcessoConcluido) {
        if (!tutorialPrimeiroAcessoConcluido) {
            tutorialPrimeiroAcessoAtivo = true
            tutorialEtapa = 0
        }
    }

    fun finalizarTutorialPrimeiroAcesso() {
        preferenciasRepository.marcarTutorialPrimeiroAcessoConcluido()

        tutorialPrimeiroAcessoAtivo = false
        tutorialEtapa = 0

        navController.navigate(Screen.Dashboard.route) {
            popUpTo(Screen.Configuracoes.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun voltarParaConfiguracoesDoTutorial(
        proximaEtapa: Int
    ) {
        tutorialEtapa = proximaEtapa

        navController.navigate(Screen.Configuracoes.route) {
            popUpTo(Screen.Configuracoes.route) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != Screen.Splash.route

    Scaffold(
        containerColor = VerdeEscuro,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                RuralBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onFinish = {
                        if (tutorialPrimeiroAcessoConcluido) {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Splash.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            tutorialPrimeiroAcessoAtivo = true
                            tutorialEtapa = 0

                            navController.navigate(Screen.Configuracoes.route) {
                                popUpTo(Screen.Splash.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(coletaRepository, preferenciasRepository)
                )

                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNovaColetaClick = {
                        navController.navigate(Screen.NovaColeta.route)
                    },
                    onConfiguracoesClick = {
                        navController.navigate(Screen.Configuracoes.route)
                    },
                    onVerTodasClick = {
                        navController.navigate(Screen.Coletas.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.NovaColeta.route) { backStackEntry ->

                val novaColetaViewModel: NovaColetaViewModel = viewModel(
                    backStackEntry,
                    factory = NovaColetaViewModelFactory(coletaRepository)
                )

                NovaColetaScreen(
                    viewModel = novaColetaViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(Screen.SelecionarModeloColeta.route)
                    }
                )
            }

            composable(Screen.SelecionarModeloColeta.route) {

                val parentEntry = navController.getBackStackEntry(Screen.NovaColeta.route)

                val novaColetaViewModel: NovaColetaViewModel = viewModel(
                    parentEntry,
                    factory = NovaColetaViewModelFactory(coletaRepository)
                )

                val modeloViewModel: ModeloColetaViewModel = viewModel(
                    factory = ModeloColetaViewModelFactory(
                        modeloRepository = modeloColetaRepository,
                        variavelRepository = variavelRepository
                    )
                )

                SelecionarModeloColetaScreen(
                    novaColetaViewModel = novaColetaViewModel,
                    modeloColetaViewModel = modeloViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(Screen.DadosGeraisColeta.route)
                    }
                )
            }

            composable(Screen.DadosGeraisColeta.route) {

                val parentEntry = navController.getBackStackEntry(Screen.NovaColeta.route)

                val novaColetaViewModel: NovaColetaViewModel = viewModel(
                    parentEntry,
                    factory = NovaColetaViewModelFactory(coletaRepository)
                )

                val capturarGpsAutomaticamente by preferenciasRepository
                    .capturarGpsAutomaticamente
                    .collectAsState()

                DadosGeraisColetaScreen(
                    viewModel = novaColetaViewModel,
                    capturarGpsAutomaticamente = capturarGpsAutomaticamente,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        novaColetaViewModel.salvar { coletaId ->
                            navController.navigate(
                                Screen.FormularioDinamicoColeta.createRoute(coletaId)
                            )
                        }
                    }
                )
            }

            composable(
                route = Screen.FormularioDinamicoColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val formularioViewModel: FormularioDinamicoViewModel = viewModel(
                    factory = FormularioDinamicoViewModelFactory(
                        coletaId = coletaId,
                        coletaRepository = coletaRepository,
                        modeloRepository = modeloColetaRepository,
                        respostaRepository = respostaColetaRepository,
                        opcaoVariavelRepository = opcaoVariavelRepository
                    )
                )

                FormularioDinamicoScreen(
                    viewModel = formularioViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFinishClick = {
                        navController.navigate(
                            Screen.BenfeitoriasColeta.createRoute(coletaId)
                        )
                    }
                )
            }

            composable(
                route = Screen.BenfeitoriasColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val benfeitoriasViewModel: BenfeitoriasViewModel = viewModel(
                    factory = BenfeitoriasViewModelFactory(
                        repository = benfeitoriaRepository,
                        coletaId = coletaId
                    )
                )

                BenfeitoriasScreen(
                    viewModel = benfeitoriasViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAdicionarClick = {
                        navController.navigate(
                            Screen.NovaBenfeitoria.createRoute(coletaId)
                        )
                    },
                    onFotosClick = { benfeitoriaId ->
                        navController.navigate(
                            Screen.FotosBenfeitoria.createRoute(benfeitoriaId)
                        )
                    },
                    onNextClick = {
                        navController.navigate(
                            Screen.FotosColeta.createRoute(coletaId)
                        )
                    }
                )
            }
            composable(
                route = Screen.FotosBenfeitoria.route,
                arguments = listOf(
                    navArgument("benfeitoriaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val benfeitoriaId = backStackEntry.arguments?.getLong("benfeitoriaId") ?: 0L

                val fotosBenfeitoriaViewModel: FotosBenfeitoriaViewModel = viewModel(
                    factory = FotosBenfeitoriaViewModelFactory(
                        repository = fotoBenfeitoriaRepository,
                        benfeitoriaId = benfeitoriaId
                    )
                )

                FotosBenfeitoriaScreen(
                    viewModel = fotosBenfeitoriaViewModel,
                    benfeitoriaId = benfeitoriaId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.NovaBenfeitoria.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val benfeitoriasViewModel: BenfeitoriasViewModel = viewModel(
                    factory = BenfeitoriasViewModelFactory(
                        repository = benfeitoriaRepository,
                        coletaId = coletaId
                    )
                )

                NovaBenfeitoriaScreen(
                    viewModel = benfeitoriasViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.FotosColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val fotosColetaViewModel: FotosColetaViewModel = viewModel(
                    factory = FotosColetaViewModelFactory(
                        repository = fotoColetaRepository,
                        coletaId = coletaId
                    )
                )

                FotosColetaScreen(
                    viewModel = fotosColetaViewModel,
                    coletaId = coletaId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(
                            Screen.ObservacoesFinaisColeta.createRoute(coletaId)
                        )
                    }
                )
            }
            composable(
                route = Screen.ObservacoesFinaisColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val observacoesViewModel: ObservacoesFinaisViewModel = viewModel(
                    factory = ObservacoesFinaisViewModelFactory(
                        coletaId = coletaId,
                        coletaRepository = coletaRepository
                    )
                )

                ObservacoesFinaisScreen(
                    viewModel = observacoesViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNextClick = {
                        navController.navigate(
                            Screen.ResumoColeta.createRoute(coletaId)
                        )
                    }
                )
            }

            composable(
                route = Screen.ResumoColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val resumoViewModel: ResumoColetaViewModel = viewModel(
                    factory = ResumoColetaViewModelFactory(
                        coletaId = coletaId,
                        coletaRepository = coletaRepository,
                        respostaRepository = respostaColetaRepository,
                        benfeitoriaRepository = benfeitoriaRepository,
                        fotoColetaRepository = fotoColetaRepository,
                        modeloColetaRepository = modeloColetaRepository
                    )
                )

                ResumoColetaScreen(
                    viewModel = resumoViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onFinishClick = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.NovaColeta.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Configuracoes.route) {
                ConfiguracoesScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onGruposClick = {
                        navController.navigate(Screen.GruposVariaveis.route)
                    },
                    onVariaveisClick = {
                        navController.navigate(Screen.Variaveis.route)
                    },
                    onModelosClick = {
                        navController.navigate(Screen.ModelosColeta.route)
                    },
                    onPreferenciasClick = {
                        navController.navigate(Screen.Preferencias.route)
                    },
                    onSobreAppClick = {
                        navController.navigate(Screen.SobreApp.route)
                    },
                    mostrarTutorialPrimeiroAcesso = tutorialPrimeiroAcessoAtivo &&
                            tutorialEtapa in listOf(0, 2, 4),
                    tutorialEtapa = tutorialEtapa,
                    onTutorialAbrirGruposClick = {
                        tutorialEtapa = 1
                        navController.navigate(Screen.GruposVariaveis.route)
                    },
                    onTutorialAbrirVariaveisClick = {
                        tutorialEtapa = 3
                        navController.navigate(Screen.Variaveis.route)
                    },
                    onTutorialAbrirModelosClick = {
                        tutorialEtapa = 5
                        navController.navigate(Screen.ModelosColeta.route)
                    },
                    onTutorialPularClick = {
                        finalizarTutorialPrimeiroAcesso()
                    }
                )
            }
            composable(Screen.Preferencias.route) {
                val preferenciasViewModel: PreferenciasViewModel = viewModel(
                    factory = PreferenciasViewModelFactory(
                        repository = preferenciasRepository
                    )
                )

                PreferenciasScreen(
                    viewModel = preferenciasViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onIniciarTutorialClick = {
                        tutorialPrimeiroAcessoAtivo = true
                        tutorialEtapa = 0

                        navController.popBackStack()
                    }
                )
            }


            composable(Screen.SobreApp.route) {
                SobreAppScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.GruposVariaveis.route) {
                val grupoViewModel: GrupoVariavelViewModel = viewModel(
                    factory = GrupoVariavelViewModelFactory(grupoVariavelRepository)
                )

                GrupoVariavelListScreen(
                    viewModel = grupoViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNovoGrupoClick = {
                        navController.navigate(Screen.NovoGrupoVariavel.route)
                    },
                    onEditarGrupoClick = { grupoId ->
                        navController.navigate(
                            Screen.EditarGrupoVariavel.createRoute(grupoId)
                        )
                    },
                    mostrarTutorialPrimeiroAcesso = tutorialPrimeiroAcessoAtivo &&
                            tutorialEtapa == 1,
                    onTutorialProximoClick = {
                        voltarParaConfiguracoesDoTutorial(
                            proximaEtapa = 2
                        )
                    },
                    onTutorialPularClick = {
                        finalizarTutorialPrimeiroAcesso()
                    }
                )
            }

            composable(Screen.NovoGrupoVariavel.route) {
                val grupoViewModel: GrupoVariavelViewModel = viewModel(
                    factory = GrupoVariavelViewModelFactory(grupoVariavelRepository)
                )

                GrupoVariavelFormScreen(
                    viewModel = grupoViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.EditarGrupoVariavel.route,
                arguments = listOf(
                    navArgument("grupoId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val grupoId = backStackEntry.arguments?.getLong("grupoId") ?: 0L

                val grupoViewModel: GrupoVariavelViewModel = viewModel(
                    factory = GrupoVariavelViewModelFactory(
                        repository = grupoVariavelRepository,
                        grupoId = grupoId
                    )
                )

                GrupoVariavelFormScreen(
                    viewModel = grupoViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Variaveis.route) {
                val variavelViewModel: VariavelViewModel = viewModel(
                    factory = VariavelViewModelFactory(
                        variavelRepository = variavelRepository,
                        grupoRepository = grupoVariavelRepository
                    )
                )

                VariavelListScreen(
                    viewModel = variavelViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNovaVariavelClick = {
                        navController.navigate(Screen.NovaVariavel.route)
                    },
                    onEditarVariavelClick = { variavelId ->
                        navController.navigate(
                            Screen.EditarVariavel.createRoute(variavelId)
                        )
                    },
                    onOpcoesClick = { variavelId ->
                        navController.navigate(
                            Screen.OpcoesVariavel.createRoute(variavelId)
                        )
                    },
                    mostrarTutorialPrimeiroAcesso = tutorialPrimeiroAcessoAtivo &&
                            tutorialEtapa == 3,
                    onTutorialProximoClick = {
                        voltarParaConfiguracoesDoTutorial(
                            proximaEtapa = 4
                        )
                    },
                    onTutorialPularClick = {
                        finalizarTutorialPrimeiroAcesso()
                    }
                )
            }
            composable(
                route = Screen.OpcoesVariavel.route,
                arguments = listOf(
                    navArgument("variavelId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val variavelId = backStackEntry.arguments?.getLong("variavelId") ?: 0L

                val opcoesViewModel: OpcoesVariavelViewModel = viewModel(
                    factory = OpcoesVariavelViewModelFactory(
                        repository = opcaoVariavelRepository,
                        variavelId = variavelId
                    )
                )

                OpcoesVariavelScreen(
                    viewModel = opcoesViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.NovaVariavel.route) {
                val variavelViewModel: VariavelViewModel = viewModel(
                    factory = VariavelViewModelFactory(
                        variavelRepository = variavelRepository,
                        grupoRepository = grupoVariavelRepository
                    )
                )

                VariavelFormScreen(
                    viewModel = variavelViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.EditarVariavel.route,
                arguments = listOf(
                    navArgument("variavelId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val variavelId = backStackEntry.arguments?.getLong("variavelId") ?: 0L

                val variavelViewModel: VariavelViewModel = viewModel(
                    factory = VariavelViewModelFactory(
                        variavelRepository = variavelRepository,
                        grupoRepository = grupoVariavelRepository,
                        variavelId = variavelId
                    )
                )

                VariavelFormScreen(
                    viewModel = variavelViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.ModelosColeta.route) {
                val modeloViewModel: ModeloColetaViewModel = viewModel(
                    factory = ModeloColetaViewModelFactory(
                        modeloRepository = modeloColetaRepository,
                        variavelRepository = variavelRepository
                    )
                )

                ModeloColetaListScreen(
                    viewModel = modeloViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNovoModeloClick = {
                        navController.navigate(Screen.NovoModeloColeta.route)
                    },
                    onEditarModeloClick = { modeloId ->
                        navController.navigate(
                            Screen.EditarModeloColeta.createRoute(modeloId)
                        )
                    },
                    mostrarTutorialPrimeiroAcesso = tutorialPrimeiroAcessoAtivo &&
                            tutorialEtapa == 5,
                    onTutorialFinalizarClick = {
                        finalizarTutorialPrimeiroAcesso()
                    },
                    onTutorialPularClick = {
                        finalizarTutorialPrimeiroAcesso()
                    }
                )
            }

            composable(Screen.NovoModeloColeta.route) {
                val modeloViewModel: ModeloColetaViewModel = viewModel(
                    factory = ModeloColetaViewModelFactory(
                        modeloRepository = modeloColetaRepository,
                        variavelRepository = variavelRepository
                    )
                )

                ModeloColetaFormScreen(
                    viewModel = modeloViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.EditarModeloColeta.route,
                arguments = listOf(
                    navArgument("modeloId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val modeloId = backStackEntry.arguments?.getLong("modeloId") ?: 0L

                val modeloViewModel: ModeloColetaViewModel = viewModel(
                    factory = ModeloColetaViewModelFactory(
                        modeloRepository = modeloColetaRepository,
                        variavelRepository = variavelRepository,
                        modeloId = modeloId
                    )
                )

                ModeloColetaFormScreen(
                    viewModel = modeloViewModel,
                    onCancelClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Coletas.route) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(coletaRepository, preferenciasRepository)
                )

                ColetasScreen(
                    viewModel = dashboardViewModel,
                    onNovaColetaClick = {
                        navController.navigate(Screen.NovaColeta.route)
                    },
                    onColetaClick = { coletaId ->
                        navController.navigate(
                            Screen.DetalheColeta.createRoute(coletaId)
                        )
                    },
                    onExportarClick = {
                        navController.navigate(Screen.ExportacaoModelos.route)
                    }
                )
            }
            composable(Screen.ExportacaoModelos.route) {
                val exportacaoModelosViewModel: ExportacaoModelosViewModel = viewModel(
                    factory = ExportacaoModelosViewModelFactory(
                        modeloColetaRepository = modeloColetaRepository
                    )
                )

                ExportacaoModelosScreen(
                    viewModel = exportacaoModelosViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onModeloClick = { modeloId ->
                        navController.navigate(
                            Screen.ExportacaoColetas.createRoute(modeloId)
                        )
                    }
                )
            }
            composable(
                route = Screen.ExportacaoColetas.route,
                arguments = listOf(
                    navArgument("modeloId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val modeloId = backStackEntry.arguments?.getLong("modeloId") ?: 0L

                val exportacaoColetasViewModel: ExportacaoColetasViewModel = viewModel(
                    factory = ExportacaoColetasViewModelFactory(
                        modeloId = modeloId,
                        coletaRepository = coletaRepository
                    )
                )

                val exportacaoViewModel: ExportacaoViewModel = viewModel(
                    factory = ExportacaoViewModelFactory(
                        coletaRepository = coletaRepository,
                        modeloColetaRepository = modeloColetaRepository,
                        respostaColetaRepository = respostaColetaRepository,
                        fotoColetaRepository = fotoColetaRepository,
                        benfeitoriaRepository = benfeitoriaRepository,
                        fotoBenfeitoriaRepository = fotoBenfeitoriaRepository
                    )
                )

                ExportacaoColetasScreen(
                    viewModel = exportacaoColetasViewModel,
                    exportacaoViewModel = exportacaoViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.DetalheColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val detalheViewModel: DetalheColetaViewModel = viewModel(
                    factory = DetalheColetaViewModelFactory(
                        coletaId = coletaId,
                        coletaRepository = coletaRepository,
                        respostaRepository = respostaColetaRepository,
                        benfeitoriaRepository = benfeitoriaRepository,
                        fotoColetaRepository = fotoColetaRepository,
                        fotoBenfeitoriaRepository = fotoBenfeitoriaRepository
                    )
                )

                val exportacaoViewModel: ExportacaoViewModel = viewModel(
                    factory = ExportacaoViewModelFactory(
                        coletaRepository = coletaRepository,
                        modeloColetaRepository = modeloColetaRepository,
                        respostaColetaRepository = respostaColetaRepository,
                        fotoColetaRepository = fotoColetaRepository,
                        benfeitoriaRepository = benfeitoriaRepository,
                        fotoBenfeitoriaRepository = fotoBenfeitoriaRepository
                    )
                )

                DetalheColetaScreen(
                    viewModel = detalheViewModel,
                    exportacaoViewModel = exportacaoViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onDeleteSuccess = {
                        navController.navigate(Screen.Coletas.route) {
                            popUpTo(Screen.Coletas.route) {
                                inclusive = true
                            }
                        }
                    },
                    onContinueOrEditClick = { coletaId ->
                        navController.navigate(
                            Screen.EditarDadosGeraisColeta.createRoute(coletaId)
                        )
                    }
                )
            }
            composable(
                route = Screen.EditarDadosGeraisColeta.route,
                arguments = listOf(
                    navArgument("coletaId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val coletaId = backStackEntry.arguments?.getLong("coletaId") ?: 0L

                val editarDadosViewModel: EditarDadosGeraisColetaViewModel = viewModel(
                    factory = EditarDadosGeraisColetaViewModelFactory(
                        coletaId = coletaId,
                        coletaRepository = coletaRepository
                    )
                )

                val capturarGpsAutomaticamente by preferenciasRepository
                    .capturarGpsAutomaticamente
                    .collectAsState()

                EditarDadosGeraisColetaScreen(
                    viewModel = editarDadosViewModel,
                    capturarGpsAutomaticamente = capturarGpsAutomaticamente,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = { id ->
                        navController.navigate(
                            Screen.FormularioDinamicoColeta.createRoute(id)
                        )
                    }
                )
            }
            composable(Screen.Mapa.route) {
                val mapaColetasViewModel: MapaColetasViewModel = viewModel(
                    factory = MapaColetasViewModelFactory(
                        coletaRepository = coletaRepository
                    )
                )

                MapaColetasScreen(
                    viewModel = mapaColetasViewModel,
                    onAbrirColetaClick = { coletaId ->
                        navController.navigate(
                            Screen.DetalheColeta.createRoute(coletaId)
                        )
                    }
                )
            }
        }
    }
}