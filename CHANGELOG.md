# Changelog

## v0.8.2 - Edição de benfeitorias

### Adicionado

- Adicionada edição de benfeitorias já cadastradas.
- Adicionada abertura da tela de edição ao clicar no card da benfeitoria.
- Adicionado carregamento dos dados existentes da benfeitoria na tela de edição.
- Adicionada opção para abrir fotos da benfeitoria pela tela de edição.
- Adicionada atualização dos dados da benfeitoria no banco local.

### Melhorado

- Melhorado o fluxo de cadastro e manutenção de benfeitorias.
- Melhorada a experiência do usuário ao permitir correções posteriores.
- Melhorada a tela de listagem de benfeitorias com acesso direto à edição.
- Melhorada a reutilização da tela de nova benfeitoria para criação e edição.

### Corrigido

- Corrigido erro de smart cast em `NovaBenfeitoriaScreen` ao acessar `benfeitoriaId`.
- Corrigido fluxo que não permitia editar dados de benfeitorias após o cadastro.

### Técnico

- Atualizado `BenfeitoriaDao`.
- Atualizado `BenfeitoriaRepository`.
- Atualizado `BenfeitoriasViewModel`.
- Atualizado `BenfeitoriasUiState`.
- Atualizado `BenfeitoriasScreen`.
- Atualizado `NovaBenfeitoriaScreen`.
- Atualizada navegação para suportar edição de benfeitoria.
- Atualizado `versionCode` para 12.
- Atualizado `versionName` para 0.8.2.

---

## v0.8.1 - Melhorias em fotos, legendas e observações

### Adicionado

- Adicionado campo de observação para fotos gerais da coleta.
- Adicionado campo de observação para fotos das benfeitorias.
- Adicionada edição posterior da legenda das fotos gerais.
- Adicionada edição posterior da observação das fotos gerais.
- Adicionada edição posterior da legenda das fotos das benfeitorias.
- Adicionada edição posterior da observação das fotos das benfeitorias.
- Adicionado componente reutilizável `FotoDescricaoDialog` para edição dos dados das fotos.
- Adicionada migration do Room para incluir o campo `observacao` nas tabelas de fotos.

### Melhorado

- Melhorada a tela de fotos gerais com campo de observação antes da captura.
- Melhorada a tela de fotos das benfeitorias com campo de observação antes da captura.
- Melhorada a apresentação dos cards de fotos com legenda e observação.
- Melhorada a organização dos dados descritivos das fotos para uso em relatórios e exportações futuras.
- Melhorada a segurança da evolução do banco, evitando perda de dados na atualização da versão atual.

### Técnico

- Atualizada `FotoColetaEntity` com o campo `observacao`.
- Atualizada `FotoBenfeitoriaEntity` com o campo `observacao`.
- Criada `DatabaseMigrations.MIGRATION_5_6`.
- Atualizado `AppDatabase` para versão 6 do banco Room.
- Atualizado `DatabaseProvider` para usar migration 5 para 6.
- Atualizado `FotoBenfeitoriaRepository` com método `atualizar`.
- Atualizado `FotosColetaViewModel`.
- Atualizado `FotosBenfeitoriaViewModel`.
- Atualizada `FotosColetaScreen`.
- Atualizada `FotosBenfeitoriaScreen`.
- Atualizado `versionCode` para 11.
- Atualizado `versionName` para 0.8.1.

---

## v0.8.0 - Backup e restauração dos dados

### Adicionado

- Adicionado recurso de backup completo dos dados locais do aplicativo.
- Adicionado recurso de restauração de backup a partir de arquivo ZIP.
- Adicionado `BackupService` para gerar e restaurar backups.
- Adicionado `BackupViewModel` para controlar o fluxo de backup e restauração.
- Adicionado `BackupCard` na tela de Preferências.
- Adicionada inclusão do banco Room no backup.
- Adicionada inclusão das fotos e arquivos internos no backup.
- Adicionada inclusão dos arquivos externos do aplicativo no backup.
- Adicionada inclusão das preferências locais no backup.
- Adicionada validação do arquivo `metadata.json` antes da restauração.
- Adicionada confirmação antes de substituir os dados atuais durante a restauração.

### Melhorado

- Melhorada a segurança dos dados coletados em campo.
- Melhorada a possibilidade de transferência dos dados para outro dispositivo.
- Melhorada a proteção contra perda de dados após reinstalação do aplicativo.
- Melhorada a preparação para futura sincronização com o Valora Web.

### Técnico

- Atualizado `DatabaseProvider` com `DATABASE_NAME` centralizado.
- Adicionado método `closeDatabase()` ao `DatabaseProvider` para permitir restauração segura.
- Adicionado checkpoint do banco antes da geração do backup.
- Adicionada extração segura de ZIP para evitar caminhos inválidos dentro do backup.
- Atualizado `versionCode` para 10.
- Atualizado `versionName` para 0.8.0.

---

## v0.7.2 - Logomarca no cabeçalho do relatório PDF

### Melhorado

- Substituído o texto `RC` pela logomarca do RuralColeta no cabeçalho do relatório PDF.
- Melhorada a identidade visual do relatório PDF.
- Melhorada a apresentação profissional do cabeçalho do PDF.
- Mantido fallback com texto `RC` caso a logomarca não seja carregada.

### Técnico

- Atualizado `PdfExportService`.
- Adicionado carregamento da logomarca a partir de `R.mipmap.ic_logo`.
- Atualizado `versionCode` para 9.
- Atualizado `versionName` para 0.7.2.

---

## v0.7.1 - Melhorias visuais do relatório PDF

### Melhorado

- Melhorado o cabeçalho visual do relatório PDF.
- Melhorada a apresentação do resumo inicial da coleta.
- Melhorada a organização das seções do relatório.
- Melhorada a apresentação dos dados gerais em formato de tabela.
- Melhorada a apresentação das variáveis respondidas.
- Melhorada a apresentação das benfeitorias em cards.
- Melhorada a organização das fotos em grade de duas colunas.
- Melhorado o rodapé com data de geração e paginação.
- Melhorado o espaçamento geral do documento.
- Melhorada a aparência profissional do PDF gerado.

### Técnico

- Atualizado `PdfExportService`.
- Mantida a arquitetura centralizada de exportação criada na v0.6.0.
- Mantido o fluxo de PDF criado na v0.7.0.
- Atualizado `versionCode` para 8.
- Atualizado `versionName` para 0.7.1.

---

---

## v0.7.0 - Relatório PDF da coleta

### Adicionado

- Adicionada exportação de relatório PDF da coleta.
- Adicionado `PdfExportService` para geração de PDF nativo no Android.
- Adicionada opção "Relatório PDF" no diálogo de exportação.
- Adicionado suporte para exportação PDF individual pela tela de detalhe da coleta.
- Adicionado suporte para exportação PDF em lote pela tela de exportação.
- Adicionado o arquivo `Relatorio_Coleta.pdf` dentro do pacote ZIP quando houver uma única coleta.
- Adicionado o arquivo `Relatorio_Coletas.pdf` dentro do pacote ZIP quando houver várias coletas.

### Melhorado

- Melhorado o pacote ZIP para incluir Excel, PDF e fotos organizadas.
- Melhorado o fluxo centralizado de exportação usando o mesmo `ExportacaoUseCase`.
- Melhorada a geração de relatórios para conter dados gerais, variáveis, benfeitorias e fotos.

### Técnico

- Criado `PdfExportService` em `data/export`.
- Atualizado `ExportacaoUseCase` para tratar `TipoExportacao.PDF`.
- Atualizado `ZipExportService` para incluir o PDF no pacote completo.
- Atualizado `ExportacaoDialog` para habilitar a opção de PDF.
- Atualizado `versionCode` para 7.
- Atualizado `versionName` para 0.7.0.

---

## v0.6.0 - Refatoração do módulo de exportação

### Adicionado

- Adicionado módulo centralizado de exportação.
- Adicionado `ExportacaoUseCase` para concentrar a lógica de exportação.
- Adicionado `ExportacaoDialog` reutilizável.
- Adicionado `ExportacaoViewModel` compartilhado entre telas.
- Adicionado suporte para exportação individual pela tela de detalhe da coleta.
- Adicionado suporte para exportação em lote pela tela de exportação.
- Adicionado serviço de exportação ZIP.
- Adicionada estrutura de pacote completo ZIP com:

  - `Dados_Coleta.xlsx`
  - `Fotos_Gerais`
  - `Benfeitorias`
- Adicionada preparação estrutural para exportação futura em PDF.

### Melhorado

- Refatorada a exportação para evitar duplicação de código.
- Melhorada a integração entre exportação Excel e ZIP.
- Melhorado o compartilhamento de arquivos exportados.
- Melhorada a organização dos arquivos exportados.
- Melhorada a tela de detalhe da coleta com opção de exportação.
- Melhorada a tela de exportação em lote com uso do mesmo fluxo de exportação.

### Técnico

- Criada camada `domain/exportacao`.
- Movida a lógica de exportação para serviços especializados.
- Centralizado o uso de `ShareHelper`.
- Corrigido uso do `FileProvider` para compartilhamento de arquivos.
- Atualizado `versionCode` para 6.
- Atualizado `versionName` para 0.6.0.

### Observação

- O relatório PDF ainda não foi implementado; a estrutura foi preparada para inclusão futura.

---

## v0.5.0 - Ordenação de variáveis no formulário

### Adicionado
- Adicionada ordenação das variáveis na criação e edição do formulário de pesquisa.
- Adicionada numeração visual das variáveis selecionadas.
- Adicionados botões para mover variável para cima e para baixo.
- Mantida a ordem de seleção das variáveis no formulário.

### Melhorado
- Melhorada a tela de criação/edição de formulário.
- Melhorada a organização das variáveis no formulário dinâmico.

### Técnico
- Atualizado versionCode para 5.
- Atualizado versionName para 0.5.0.
- A ordenação usa o campo `ordem` já existente em `ModeloColetaVariavelEntity`.
- Não requer migration do Room.

---

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