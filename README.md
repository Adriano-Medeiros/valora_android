# RuralColeta

Aplicativo Android para coleta de dados de imóveis rurais em campo, com foco em avaliações rurais pelo método comparativo direto de dados de mercado.

O RuralColeta permite cadastrar formulários de pesquisa, registrar imóveis avaliandos e dados amostrais, coletar variáveis, fotos, benfeitorias, coordenadas geográficas e gerar exportações em Excel, ZIP e PDF.

---

## Status atual

Versão atual: `v0.7.2`

```kotlin
versionCode = 9
versionName = "0.7.2"
```

Esta versão inclui:

* Relatório PDF com logomarca do RuralColeta no cabeçalho.
* Relatório PDF com layout visual melhorado.
* Exportação individual pela tela de detalhe da coleta.
* Exportação em lote pela tela de exportação.
* Exportação em Excel.
* Exportação em pacote completo ZIP.
* Inclusão do PDF dentro do pacote ZIP.
* Estrutura ZIP organizada com Excel, PDF, fotos gerais e fotos de benfeitorias.
* Módulo centralizado de exportação com `ExportacaoUseCase`.
* Diálogo reutilizável para escolha do tipo de exportação.
* Ordenação das variáveis no formulário de pesquisa.
* Edição de grupos, variáveis e formulários.
* Mapa estável com OSMDroid.
* Campos inteligentes com teclado adequado por tipo de dado.
* Campo de data com DatePicker.
* Tutorial guiado de primeiro acesso.
* Exibição da versão do aplicativo na Splash Screen.

---

## Objetivo do aplicativo

O RuralColeta foi desenvolvido para auxiliar avaliadores de imóveis rurais na coleta organizada de dados em campo.

O aplicativo não realiza a avaliação monetária do imóvel. Seu objetivo é coletar, organizar e exportar dados para posterior análise técnica, inclusive em sistemas externos ou plataformas web, como o Valora.

Principais objetivos:

* Coletar dados de imóveis rurais avaliandos.
* Coletar dados amostrais comparáveis.
* Registrar variáveis personalizadas.
* Registrar fotos gerais do imóvel.
* Registrar benfeitorias e fotos vinculadas.
* Registrar coordenadas geográficas.
* Exportar os dados coletados em Excel.
* Exportar pacote completo com Excel, PDF e fotos.
* Gerar relatório PDF da coleta.
* Facilitar o trabalho de campo do avaliador rural.

---

## Identificação do projeto

```text
Nome do aplicativo: RuralColeta
Package: br.com.agrobox.ruralcoleta
Plataforma: Android
Linguagem: Kotlin
Interface: Jetpack Compose
Banco local: Room
Arquitetura: MVVM
```

---

## Tecnologias utilizadas

* Kotlin
* Android Studio
* Jetpack Compose
* Material 3
* Room Database
* ViewModel
* StateFlow
* Navigation Compose
* Coroutines
* OSMDroid
* Apache POI
* FileProvider
* SharedPreferences
* Android Graphics PDF nativo

---

## Arquitetura geral

O projeto segue uma estrutura organizada por camadas:

```text
app/src/main/java/br/com/agrobox/ruralcoleta
│
├── data
│   ├── export
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   └── entity
│   └── repository
│
├── domain
│   └── exportacao
│
├── ui
│   ├── components
│   ├── coleta
│   ├── dashboard
│   ├── exportacao
│   ├── mapa
│   ├── modelo
│   ├── preferencias
│   ├── splash
│   └── variavel
│
└── util
```

---

## Principais módulos

### Dashboard

Tela inicial do aplicativo.

Exibe resumo das coletas cadastradas, incluindo:

* Coletas recentes.
* Rascunhos.
* Coletas concluídas.
* Imóveis avaliandos.
* Dados amostrais.
* Acesso rápido às principais funcionalidades.

---

### Configurações

Área responsável pelos cadastros estruturais do aplicativo.

Permite acessar:

* Grupos de variáveis.
* Variáveis.
* Formulários de pesquisa.
* Preferências.
* Tutorial guiado.

---

### Grupos de variáveis

Permite cadastrar e editar grupos usados para organizar variáveis.

Exemplos:

* Dados do imóvel.
* Localização.
* Acesso.
* Solo.
* Topografia.
* Benfeitorias.
* Produção.

---

### Variáveis

Permite cadastrar e editar variáveis utilizadas nos formulários de pesquisa.

Tipos de campo suportados:

* Texto.
* Número.
* Lista de opções.
* Data.

As variáveis podem conter:

* Nome.
* Grupo.
* Tipo de campo.
* Unidade.
* Obrigatoriedade.
* Dica de preenchimento.
* Opções, quando forem do tipo lista.

---

### Formulários de pesquisa

Permite criar e editar formulários de coleta.

Cada formulário pode conter variáveis selecionadas pelo usuário.

Recursos disponíveis:

* Seleção de variáveis.
* Ordenação das variáveis.
* Numeração visual das variáveis selecionadas.
* Botões para mover variável para cima ou para baixo.
* Edição posterior do formulário.
* Uso do formulário em novas coletas.

Observação: internamente o projeto ainda usa a nomenclatura `ModeloColeta`, mas na interface do usuário o termo adotado é `Formulário de pesquisa`.

---

## Coleta de dados

O fluxo de coleta permite registrar dados de imóveis rurais avaliandos ou dados amostrais.

Tipos de coleta:

* Imóvel avaliando.
* Dado amostral.

Status da coleta:

* Rascunho.
* Concluída.

Principais informações coletadas:

* Nome de referência.
* Município.
* UF.
* Informante.
* Contato do informante.
* Latitude.
* Longitude.
* Respostas das variáveis.
* Fotos gerais.
* Benfeitorias.
* Fotos das benfeitorias.

---

## Dados gerais da coleta

A tela de dados gerais permite cadastrar informações básicas da coleta.

Campos principais:

* Tipo da coleta.
* Formulário de pesquisa.
* Nome de referência.
* Município.
* UF.
* Informante.
* Contato do informante.
* Latitude.
* Longitude.

O aplicativo permite obter coordenadas geográficas quando o GPS estiver disponível.

---

## Formulário dinâmico

O formulário dinâmico é gerado com base nas variáveis vinculadas ao formulário de pesquisa selecionado.

Recursos disponíveis:

* Campos de texto.
* Campos numéricos.
* Campos de lista.
* Campos de data com DatePicker.
* Teclado adequado para número decimal.
* Teclado adequado para telefone.
* Tratamento de latitude e longitude com valores negativos.
* Validação de campos obrigatórios.
* Carregamento das respostas ao editar coleta existente.

---

## Fotos gerais

O aplicativo permite registrar fotos gerais da coleta.

As fotos podem ser usadas para:

* Documentar características gerais do imóvel.
* Apoiar a vistoria.
* Integrar o pacote ZIP.
* Integrar o relatório PDF.
* Integrar exportações futuras.

---

## Benfeitorias

O aplicativo permite cadastrar benfeitorias vinculadas à coleta.

Cada benfeitoria pode conter:

* Nome.
* Categoria.
* Descrição.
* Fotos vinculadas.

Exemplos de benfeitorias:

* Casa.
* Curral.
* Cerca.
* Galpão.
* Poço.
* Estrada interna.
* Açude.

---

## Mapa

O RuralColeta possui uma tela de mapa com OSMDroid.

Recursos disponíveis:

* Exibição das coletas georreferenciadas.
* Filtro por tipo de coleta.
* Filtro por status.
* Marcadores no mapa.
* BottomSheet com informações da coleta.
* Abertura da coleta a partir do mapa.

Filtros disponíveis:

* Imóvel avaliando.
* Dado amostral.
* Rascunho.
* Concluída.

A implementação atual utiliza o marcador padrão do OSMDroid para garantir estabilidade.

---

## Exportação

O RuralColeta possui um módulo centralizado de exportação, utilizado tanto pela tela de detalhe da coleta quanto pela tela de exportação em lote.

Tipos disponíveis:

* Excel.
* Pacote completo ZIP.
* Relatório PDF.

Arquitetura da exportação:

```text
Tela de detalhe da coleta
Tela de exportação em lote
        ↓
ExportacaoDialog
        ↓
ExportacaoViewModel
        ↓
ExportacaoUseCase
        ↓
ExcelExportService
PdfExportService
ZipExportService
```

---

## Exportação Excel

A exportação Excel gera uma planilha com os dados das coletas selecionadas.

O Excel contém:

* Dados gerais da coleta.
* Tipo da coleta.
* Status.
* Município.
* UF.
* Informante.
* Contato.
* Coordenadas.
* Variáveis respondidas.
* Referências a fotos.
* Dados de benfeitorias, quando aplicável.

Arquivo gerado:

```text
Dados_Coleta.xlsx
```

---

## Exportação PDF

A exportação PDF gera um relatório visual da coleta.

O PDF contém:

* Cabeçalho com logomarca do RuralColeta.
* Título do relatório.
* Resumo inicial da coleta.
* Dados gerais.
* Variáveis respondidas.
* Benfeitorias.
* Fotos gerais.
* Fotos das benfeitorias.
* Rodapé com data de geração e paginação.

O relatório PDF pode ser gerado para:

* Uma coleta individual.
* Várias coletas em lote.

Arquivos gerados:

```text
Relatorio_Coleta.pdf
Relatorio_Coletas.pdf
```

---

## Pacote completo ZIP

O pacote ZIP reúne os principais arquivos da exportação.

Para uma única coleta, a estrutura é:

```text
RuralColeta_2026-06-27_145530.zip
│
├── Dados_Coleta.xlsx
├── Relatorio_Coleta.pdf
├── Fotos_Gerais
│
└── Benfeitorias
    ├── Casa
    ├── Curral
    └── Cerca
```

Para várias coletas, a estrutura é:

```text
RuralColeta_2026-06-27_145530.zip
│
├── Dados_Coleta.xlsx
├── Relatorio_Coletas.pdf
│
├── 01_Fazenda_Santa_Maria
│   ├── Fotos_Gerais
│   └── Benfeitorias
│       ├── Casa
│       └── Curral
│
└── 02_Sitio_Boa_Vista
    ├── Fotos_Gerais
    └── Benfeitorias
        └── Cerca
```

---

## Tutorial guiado

O aplicativo possui tutorial guiado de primeiro acesso.

O tutorial orienta o usuário na sequência inicial de uso:

* Criar grupo de variáveis.
* Criar variáveis.
* Criar formulário de pesquisa.
* Iniciar o uso do aplicativo.

Também existe opção para visualizar o tutorial novamente pela tela de Preferências.

---

## Preferências

A tela de preferências permite configurar comportamentos do aplicativo.

Recursos disponíveis:

* Visualizar tutorial novamente.
* Preferências locais do usuário.
* Ajustes futuros de exibição e comportamento.

---

## Splash Screen

A Splash Screen exibe:

* Imagem de abertura do aplicativo.
* Barra de progresso animada.
* Versão atual do aplicativo.

A versão é recuperada a partir do `BuildConfig`, mantendo o Gradle como fonte única de versionamento.

---

## Controle de versão

A versão do aplicativo é controlada no arquivo:

```text
app/build.gradle.kts
```

Exemplo atual:

```kotlin
defaultConfig {
    applicationId = "br.com.agrobox.ruralcoleta"
    minSdk = 26
    targetSdk = 36
    versionCode = 9
    versionName = "0.7.2"
}
```

---

## Histórico de versões

### v0.7.2

* Adicionada logomarca do RuralColeta no cabeçalho do relatório PDF.
* Substituído o texto `RC` pela logo do aplicativo.
* Melhorada a identidade visual do PDF.
* Mantido fallback com texto `RC` caso a logomarca não seja carregada.

### v0.7.1

* Melhorado o layout visual do relatório PDF.
* Melhorado o cabeçalho.
* Melhorado o resumo inicial da coleta.
* Melhorada a apresentação dos dados gerais.
* Melhorada a apresentação das variáveis.
* Melhorada a apresentação das benfeitorias.
* Melhorada a organização das fotos.
* Melhorado o rodapé com data e paginação.

### v0.7.0

* Adicionada exportação de relatório PDF.
* Adicionado `PdfExportService`.
* Adicionada opção de PDF no diálogo de exportação.
* Adicionado PDF individual.
* Adicionado PDF em lote.
* Adicionado PDF dentro do pacote ZIP.

### v0.6.0

* Refatorado o módulo de exportação.
* Adicionado `ExportacaoUseCase`.
* Adicionado `ExportacaoDialog`.
* Adicionado `ExportacaoViewModel`.
* Adicionado ZIP completo.
* Centralizada a exportação individual e em lote.

### v0.5.0

* Adicionada ordenação de variáveis no formulário.
* Adicionada numeração das variáveis selecionadas.
* Adicionados botões para mover variável para cima e para baixo.

### v0.4.0

* Adicionada edição de grupos.
* Adicionada edição de variáveis.
* Adicionada edição de formulários de pesquisa.

### v0.3.0

* Adicionado mapa com OSMDroid.
* Adicionados campos inteligentes.
* Adicionado DatePicker.
* Adicionado tutorial guiado.
* Adicionada versão na Splash Screen.

---

## Como compilar

No terminal, dentro da pasta do projeto:

```powershell
.\gradlew assembleDebug
```

O APK de debug será gerado em:

```text
app/build/outputs/apk/debug/
```

---

## Como executar no Android Studio

* Abrir o projeto no Android Studio.
* Aguardar o Gradle Sync.
* Selecionar o módulo `app`.
* Selecionar um emulador ou dispositivo físico.
* Clicar em `Run`.

---

## Requisitos mínimos

* Android Studio instalado.
* JDK configurado.
* Gradle Sync funcionando.
* Emulador Android ou dispositivo físico.
* Android 8.0 ou superior.

Configuração do aplicativo:

```text
minSdk = 26
targetSdk = 36
```

---

## Branches recomendadas

Fluxo recomendado de trabalho:

```text
master
feature/nome-da-funcionalidade
```

Exemplos:

```text
feature/exportacao
feature/pdf-relatorio-coleta
feature/pdf-layout-v071
feature/backup-restauracao
```

---

## Comandos úteis de Git

Criar nova branch:

```powershell
git checkout -b feature/nome-da-funcionalidade
```

Enviar branch para o GitHub:

```powershell
git push -u origin feature/nome-da-funcionalidade
```

Commit:

```powershell
git status
git add .
git commit -m "feat: descricao da funcionalidade"
git push
```

Merge na master:

```powershell
git checkout master
git pull origin master
git merge feature/nome-da-funcionalidade
git push origin master
```

Criar tag de versão:

```powershell
git tag -a v0.7.2 -m "v0.7.2 - Logomarca no cabeçalho do relatório PDF"
git push origin v0.7.2
```

---

## Próximas etapas planejadas

### v0.8.0 - Backup e restauração

Objetivo:

* Gerar backup completo do banco local.
* Incluir fotos no backup.
* Permitir restauração em outro dispositivo.
* Proteger dados do usuário em campo.

Estrutura prevista:

```text
RuralColeta_Backup_2026-06-27_153000.zip
│
├── ruralcoleta.db
├── ruralcoleta.db-shm
├── ruralcoleta.db-wal
├── fotos
├── metadata.json
└── versao.txt
```

### v0.8.1 - Melhorias em fotos

Objetivo:

* Melhorar organização das fotos.
* Adicionar legendas.
* Adicionar observações.
* Melhorar qualidade e tamanho dos arquivos.
* Preparar uso das fotos em relatórios mais avançados.

### v0.9.0 - Preparação para integração com Valora Web

Objetivo:

* Preparar sincronização futura.
* Estruturar autenticação.
* Preparar envio de coletas via API.
* Preparar integração com Django REST Framework.

### v1.0.0 - MVP estável

Objetivo:

* Consolidar funcionalidades principais.
* Estabilizar coleta.
* Estabilizar exportações.
* Estabilizar PDF.
* Estabilizar backup.
* Preparar uso real em campo.

---

## Observações técnicas

* O aplicativo funciona localmente, sem depender de internet para a coleta.
* A internet pode ser necessária apenas para mapas, compartilhamentos e recursos externos.
* Os dados são armazenados localmente com Room.
* As fotos são salvas no armazenamento do aplicativo.
* As exportações usam `FileProvider` para compartilhamento seguro.
* O PDF é gerado com recursos nativos do Android.
* O Excel é gerado com Apache POI.
* O ZIP é gerado com APIs nativas de compactação do Java/Kotlin.

---

## Licença

Projeto em desenvolvimento.

Uso interno e comercial condicionado às definições do proprietário do código-fonte.
