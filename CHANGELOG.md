# Changelog

## v0.3.0 - Mapa estável, campos inteligentes e tutorial guiado

### Adicionado
- Adicionado módulo de mapa com OSMDroid para visualização das coletas georreferenciadas.
- Adicionados filtros no mapa para:
    - Imóvel avaliando
    - Dado amostral
    - Rascunho
    - Concluída
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
- Melhorada a experiência inicial do usuário com orientação para criação de:
    - Grupo de variáveis
    - Variáveis
    - Formulário de pesquisa

### Corrigido
- Corrigido travamento/ANR ao voltar da tela de dados da coleta para o mapa.
- Corrigida centralização repetida do mapa durante recomposição da tela.
- Corrigido problema de múltiplos puxadores no BottomSheet.
- Corrigido uso indevido de ViewModel diretamente dentro do componente CampoDinamico.
- Corrigida exibição da mãozinha do tutorial guiado, ajustando sua posição em relação ao botão destacado.
- Corrigido erro de acesso ao BuildConfig habilitando o recurso buildConfig no Gradle.

### Removido
- Removida a implementação de markers personalizados no mapa.
- Removidos códigos relacionados a imagens PNG personalizadas para marcadores.
- Removida a tentativa de redimensionamento manual de markers no mapa.

### Técnico
- Versão Android atualizada para:
    - versionCode: 3
    - versionName: 0.3.0
- Mantida a versão como fonte única no Gradle.
- Criado utilitário AppInfo para exibir a versão no app.
- Mantido o marker padrão do OSMDroid para estabilidade.