# RuralColeta

Aplicativo Android para coleta de dados de imóveis rurais, com foco na organização das informações de campo utilizadas em processos de avaliação rural.

O RuralColeta permite cadastrar grupos, variáveis e formulários personalizados, registrar dados de imóveis avaliandos e amostrais, capturar coordenadas geográficas, registrar fotos, organizar benfeitorias e visualizar as coletas em mapa.

---

## Versão atual

```kotlin
versionCode = 5
versionName = "0.5.0"
```

---

## Objetivo do projeto

O objetivo do RuralColeta é facilitar a coleta padronizada de informações de imóveis rurais diretamente em campo, permitindo que o usuário monte seus próprios formulários de pesquisa conforme a necessidade da avaliação.

O aplicativo não realiza a avaliação do imóvel nem calcula o valor de mercado. Ele atua na etapa de coleta, organização, edição e exportação dos dados coletados.

---

## Principais funcionalidades

* Cadastro de grupos de variáveis.
* Edição de grupos de variáveis.
* Cadastro de variáveis personalizadas.
* Edição de variáveis.
* Cadastro de opções para variáveis do tipo lista.
* Criação de formulários de pesquisa.
* Edição de formulários de pesquisa.
* Vinculação de variáveis aos formulários.
* Alteração das variáveis vinculadas a um formulário existente.
* Coleta de dados de imóvel avaliando.
* Coleta de dados amostrais.
* Registro de dados gerais da coleta.
* Captura de latitude e longitude.
* Edição de coletas já cadastradas.
* Campos dinâmicos por formulário.
* Suporte a campos de texto, número, lista, sim/não e data.
* DatePicker para campos do tipo data.
* Registro de fotos da coleta.
* Cadastro de benfeitorias.
* Registro de fotos por benfeitoria.
* Tela de resumo da coleta.
* Finalização da coleta.
* Histórico de coletas.
* Mapa com pontos georreferenciados.
* Filtros no mapa por tipo e status da coleta.
* BottomSheet com detalhes ao clicar no marcador do mapa.
* Preferências do aplicativo.
* Tutorial guiado de primeiro acesso.
* Botão para visualizar novamente o tutorial.
* Exibição da versão do app na Splash Screen.
* Ordenação das variáveis dentro do formulário de pesquisa.
* Numeração visual das variáveis selecionadas.
* Reordenação das variáveis com botões de subir e descer.
---

## Fluxo básico de uso

No primeiro uso, o usuário deve configurar a estrutura da coleta:

1. Criar um grupo de variáveis.
2. Criar as variáveis.
3. Criar o formulário de pesquisa.
4. Iniciar uma nova coleta.
5. Escolher o tipo da coleta: avaliando ou amostral.
6. Preencher os dados gerais.
7. Preencher os campos dinâmicos do formulário.
8. Registrar fotos.
9. Cadastrar benfeitorias, se houver.
10. Revisar o resumo.
11. Finalizar a coleta.

Caso necessário, o usuário também pode editar posteriormente:

* Grupos de variáveis.
* Variáveis.
* Formulários de pesquisa.
* Coletas já cadastradas.

---

## Tecnologias utilizadas

* Kotlin
* Android Studio
* Jetpack Compose
* MVVM
* Room Database
* Navigation Compose
* Kotlin Coroutines
* StateFlow
* OSMDroid
* Material Design 3
* Gradle Kotlin DSL

---

## Arquitetura

O projeto segue uma organização baseada em camadas, separando responsabilidades entre dados, repositórios, telas, estados e ViewModels.

Estrutura geral:

```text
app/src/main/java/br/com/agrobox/ruralcoleta
├── data
│   ├── local
│   │   ├── dao
│   │   ├── database
│   │   └── entity
│   └── repository
├── navigation
├── ui
│   ├── coleta
│   ├── components
│   ├── configuracoes
│   ├── dashboard
│   ├── grupo
│   ├── mapa
│   ├── modelo
│   ├── splash
│   └── variavel
└── util
```

---

## Módulos principais

### Dashboard

Tela inicial do aplicativo, exibindo indicadores das coletas, atalhos e informações resumidas.

### Configurações

Área onde o usuário acessa os cadastros estruturais do app:

* Grupos de variáveis.
* Variáveis.
* Formulários de pesquisa.
* Preferências.
* Sobre o app.

### Grupos de variáveis

Permite cadastrar e editar grupos usados para organizar os campos do formulário.

Exemplos:

* Dados do imóvel.
* Localização.
* Solo.
* Benfeitorias.
* Mercado.
* Produção.

### Variáveis

Permite cadastrar e editar os campos que serão preenchidos durante a coleta.

Tipos suportados:

* Texto.
* Número.
* Lista.
* Sim/Não.
* Data.

### Formulários de pesquisa

Permite criar e editar formulários personalizados, selecionando quais variáveis farão parte da coleta.

O formulário define quais campos serão exibidos na etapa de preenchimento dos dados dinâmicos.

### Coleta

Fluxo principal para criação e edição de coletas.

Inclui:

* Tipo de coleta.
* Dados gerais.
* Formulário dinâmico.
* Fotos.
* Benfeitorias.
* Resumo.
* Conclusão.

### Mapa

Exibe as coletas com coordenadas válidas em um mapa usando OSMDroid.

Permite filtrar por:

* Avaliando.
* Amostral.
* Rascunho.
* Concluída.

Ao clicar em um marcador, o app exibe um BottomSheet com detalhes da coleta e opção para abrir o registro.

### Preferências

Permite configurar comportamentos do app, como:

* Captura automática de GPS.
* Exibição de rascunhos no Dashboard.
* Período de atividades recentes.
* Reexibição do tutorial de primeiro acesso.

---

## Tutorial guiado

O aplicativo possui um onboarding guiado para orientar o usuário no primeiro uso.

O tutorial ensina a sequência correta:

1. Criar grupo.
2. Criar variáveis.
3. Criar formulário de pesquisa.

O tutorial pode ser acessado novamente em:

```text
Configurações > Preferências > Ver tutorial novamente
```

---

## Banco de dados local

O aplicativo utiliza Room Database para persistência local dos dados.

As informações são armazenadas no próprio dispositivo, permitindo uso offline.

Principais entidades:

* GrupoVariavel
* Variavel
* OpcaoVariavel
* ModeloColeta
* ModeloColetaVariavel
* Coleta
* RespostaColeta
* FotoColeta
* Benfeitoria

---

## Mapa

O mapa utiliza OSMDroid.

As coletas só aparecem no mapa quando possuem:

```text
latitude válida
longitude válida
```

Foi mantido o marker padrão do OSMDroid para maior estabilidade do aplicativo.

---

## Entrada de dados

O aplicativo utiliza tipos de teclado adequados conforme o campo:

* Texto: teclado normal.
* Número: teclado decimal.
* Contato: teclado de telefone.
* Latitude e longitude: teclado decimal com suporte a valores negativos.
* Data: seletor de data com DatePicker.

---

## Versionamento

A versão do aplicativo é definida no Gradle do módulo app.

Arquivo:

```text
app/build.gradle.kts
```

Exemplo:

```kotlin
defaultConfig {
    applicationId = "br.com.agrobox.ruralcoleta"
    minSdk = 26
    targetSdk = 36
    versionCode = 4
    versionName = "0.4.0"
}
```

A versão exibida no app é recuperada automaticamente através do `BuildConfig`.

---

## Como executar o projeto

1. Clonar o repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Abrir o projeto no Android Studio.

3. Aguardar a sincronização do Gradle.

4. Executar o app em um emulador ou dispositivo Android.

---

## Requisitos mínimos

* Android Studio atualizado.
* JDK compatível com o projeto.
* Dispositivo ou emulador Android.
* Android SDK configurado.

Configuração atual:

```text
minSdk: 26
targetSdk: 36
```

---

## Build do projeto

Para gerar o build debug:

```bash
./gradlew assembleDebug
```

No Windows:

```bash
.\gradlew assembleDebug
```

Para limpar e compilar:

```bash
.\gradlew clean build
```

Caso o terminal não reconheça o Java, configure o `JAVA_HOME` apontando para o Java do Android Studio, normalmente em:

```text
C:\Program Files\Android\Android Studio\jbr
```

---

## Status atual

Versão atual: v0.5.0

Esta versão inclui:
* Ordenação das variáveis no formulário de pesquisa.
* Edição de grupos, variáveis e formulários.
* Mapa estável com OSMDroid.
* Campos inteligentes e DatePicker.
* Tutorial guiado de primeiro acesso.
---

## Próximas melhorias previstas

* Melhorar exportação dos dados coletados.
* Exportar fotos originais em ZIP.
* Melhorar relatórios em Excel.
* Implementar relatório PDF.
* Melhorar tela de histórico.
* Melhorar tela de detalhes da coleta.
* Criar backup/restauração local dos dados.
* Avaliar integração futura com backend web.

---

## Autor

Projeto desenvolvido por Adriano Medeiros.

---

## Observação

O RuralColeta está em desenvolvimento e pode receber ajustes de estrutura, interface e funcionalidades conforme a evolução do projeto.
