feat: adiciona edicao de grupos variaveis e formularios

# Changelog

## v0.4.0 - Edição de cadastros estruturais

### Adicionado
- Adicionada edição de grupos de variáveis.
- Adicionada edição de variáveis.
- Adicionada edição de formulários de pesquisa.
- Adicionadas rotas específicas para edição de grupo, variável e formulário.
- Adicionado carregamento dos dados existentes nas telas de edição.
- Adicionada atualização de registros existentes nos repositórios.
- Adicionada busca por ID para grupos, variáveis e formulários.
- Adicionada possibilidade de alterar as variáveis vinculadas a um formulário já criado.

### Melhorado
- Melhorado o fluxo dos cadastros estruturais do app.
- Melhorada a reutilização das telas de formulário para cadastro e edição.
- Melhorada a navegação entre listagem e edição.
- Melhorado o controle entre criação de novo registro e atualização de registro existente.

### Técnico
- Atualizado versionCode para 4.
- Atualizado versionName para 0.4.0.
- Mantida a estrutura atual do banco Room, sem necessidade de migration.

---

## v0.3.0 - Mapa estável, campos inteligentes e tutorial guiado

### Adicionado
- Adicionado módulo de mapa com OSMDroid para visualização das coletas georreferenciadas.
- Adicionados filtros no mapa para imóvel avaliando, dado amostral, rascunho e concluída.
- Adicionado BottomSheet ao clicar em um marcador no mapa.
- Adicionado botão para abrir a coleta diretamente pelo BottomSheet do mapa.
- Adicionado suporte a teclado numérico decimal em campos do tipo número.
- Adicionado suporte a teclado de telefone no campo de contato.
- Adicionado tratamento para latitude e longitude com valores negativos.
- Adicionado campo dinâmico do tipo DATA com DatePicker.
- Adicionado componente reutilizável para seleção de data.
- Adicionado AppInfo para recuperar a versão do app a partir do BuildConfig.
- Adicionada exibição da versão do app na Splash Screen.
- Adicionado tutorial guiado de primeiro acesso.
- Adicionado botão em Preferências para visualizar o tutorial novamente.
- Adicionado controle local para saber se o tutorial de primeiro acesso já foi concluído.

### Melhorado
- Melhorada a tela do mapa para exibir apenas coletas com latitude e longitude válidas.
- Melhorada a organização dos filtros do mapa no topo da tela.
- Melhorado o BottomSheet do mapa com mais informações da coleta.
- Melhorada a navegação entre mapa e tela de edição da coleta.
- Melhorada a entrada de dados nos formulários dinâmicos.
- Melhorada a reutilização de componentes no formulário dinâmico.
- Melhorada a Splash Screen com barra de progresso animada e versão do app.
- Melhorada a experiência inicial do usuário com orientação para criação de grupo, variáveis e formulário de pesquisa.

### Corrigido
- Corrigido travamento/ANR ao voltar da tela de dados da coleta para o mapa.
- Corrigida centralização repetida do mapa durante recomposição da tela.
- Corrigido problema de múltiplos puxadores no BottomSheet.
- Corrigido uso indevido de ViewModel diretamente dentro do componente CampoDinamico.
- Corrigida exibição da mãozinha do tutorial guiado.
- Corrigido erro de acesso ao BuildConfig habilitando o recurso buildConfig no Gradle.

### Removido
- Removida a implementação de markers personalizados no mapa.
- Removidos códigos relacionados a imagens PNG personalizadas para marcadores.
- Removida a tentativa de redimensionamento manual de markers no mapa.

### Técnico
- versionCode: 3
- versionName: 0.3.0
- Mantida a versão como fonte única no Gradle.
- Criado utilitário AppInfo para exibir a versão no app.
- Mantido o marker padrão do OSMDroid para estabilidade.