# Melhorias do Sistema de Ficha

Este documento reúne ideias para evolução do sistema, abrangendo conteúdo de RPG,
experiência da TUI, arquitetura, banco de dados, qualidade e distribuição.

## Prioridades

- **P0 - Essencial:** evita perda de dados, erros graves ou bloqueios de evolução.
- **P1 - Importante:** melhora bastante o uso e a manutenção do sistema.
- **P2 - Evolução:** adiciona profundidade, conveniência ou personalização.

## P0 - Base Segura

- [ ] Salvar os dados em uma pasta própria do usuário, e não em `src/main/resources`.
- [ ] Implementar escrita atômica para impedir arquivos incompletos em caso de falha.
- [ ] Criar backup automático antes de substituir dados existentes.
- [ ] Validar todos os dados no service, sem depender apenas da interface.
- [ ] Garantir que equipamentos nunca permaneçam em estados incompatíveis.
- [ ] Adicionar testes para vida, atributos, slots, permissões e persistência.
- [ ] Tratar arquivos ausentes, vazios, antigos ou corrompidos sem encerrar o programa.
- [ ] Exibir um aviso quando houver alterações ainda não salvas.

## Conteúdo da Ficha

### Regras Básicas

- [ ] Calcular automaticamente modificadores dos atributos.
- [ ] Adicionar Classe de Armadura, iniciativa, deslocamento e percepção passiva.
- [ ] Adicionar testes de resistência por atributo.
- [ ] Criar perícias e permitir marcar proficiência ou especialização.
- [ ] Adicionar dados de vida, recuperações e testes contra a morte.
- [ ] Registrar inspiração e condições do personagem.
- [ ] Permitir bônus temporários e penalidades nos atributos.
- [ ] Mostrar valores derivados e explicar de onde cada bônus veio.

### Combate

- [ ] Criar uma tela resumida de combate.
- [ ] Adicionar ataques com bônus, dano, alcance e tipo de dano.
- [ ] Implementar rolagens como `1d20+3`, vantagem e desvantagem.
- [ ] Manter histórico das rolagens realizadas.
- [ ] Controlar munição, cargas e usos por descanso.
- [ ] Adicionar resistências, imunidades e vulnerabilidades.
- [ ] Aplicar bônus dos equipamentos nos valores derivados da ficha.

### Inventário e Equipamentos

- [ ] Calcular peso total do inventário.
- [ ] Definir capacidade de carga baseada em Força.
- [ ] Permitir excluir equipamentos.
- [ ] Permitir transferir um item para equipamento e vice-versa.
- [ ] Agrupar itens por tipo e permitir busca ou filtro.
- [ ] Adicionar preço, peso e raridade também aos itens comuns.
- [ ] Controlar itens consumíveis e reduzir a quantidade ao utilizar.
- [ ] Exibir Principal, Secundária e Armadura em um resumo separado.
- [ ] Permitir novos slots no futuro sem alterar regras espalhadas pelo código.

### Habilidades e Magias

- [ ] Separar habilidades, características de classe e magias.
- [ ] Adicionar espaços de magia por nível.
- [ ] Controlar concentração, componentes e tempo de conjuração.
- [ ] Marcar habilidades preparadas, disponíveis ou em recarga.
- [ ] Restaurar usos por descanso curto ou longo.
- [ ] Permitir categorias e filtros.

### Campanha

- [ ] Permitir criar e selecionar várias fichas.
- [ ] Adicionar experiência e progressão de nível.
- [ ] Criar diário de sessão com data e título.
- [ ] Adicionar objetivos, missões, NPCs e locais conhecidos.
- [ ] Registrar histórico de alterações de vida, moedas e inventário.
- [ ] Exportar uma ficha resumida para texto, Markdown ou PDF.

## Design e Experiência da TUI

- [ ] Criar um componente único para popup, confirmação e seleção.
- [ ] Padronizar teclas em todas as telas.
- [ ] Adicionar uma tela de ajuda acessível por `?` ou `F1`.
- [ ] Mostrar atalhos apenas quando estiverem disponíveis na tela atual.
- [ ] Adicionar rolagem para listas e formulários maiores que o terminal.
- [ ] Adaptar o layout automaticamente a terminais pequenos.
- [ ] Indicar posição e quantidade, por exemplo `3/18`.
- [ ] Exibir claramente quando um campo está sendo editado.
- [ ] Mostrar mensagens de sucesso por tempo limitado e erros até confirmação.
- [ ] Permitir temas, incluindo opção sem cores.
- [ ] Evitar depender apenas de cores para comunicar estado.
- [ ] Criar atalhos para busca, voltar ao topo e ir ao final da lista.
- [ ] Permitir cancelar qualquer operação com Esc sem salvar parcialmente.
- [ ] Confirmar ações destrutivas e a saída do sistema.

## Arquitetura

### Separação de Responsabilidades

- [ ] Dividir `FichaTui` em telas menores, uma classe por área.
- [ ] Criar um roteador de telas e uma pilha de navegação.
- [ ] Extrair componentes reutilizáveis de lista, formulário, rodapé e popup.
- [ ] Separar estado visual do estado persistido.
- [ ] Criar objetos de formulário para edição e adição.
- [ ] Centralizar validações de domínio nos services ou em regras específicas.
- [ ] Evitar lógica de negócio dentro das classes de interface.
- [ ] Remover menus clássicos quando a TUI cobrir todos os fluxos necessários.

### Modelo de Domínio

- [ ] Usar identificadores estáveis para itens, equipamentos, habilidades e anotações.
- [ ] Substituir textos livres importantes por enums, como tipo e raridade.
- [ ] Representar peso e moedas com tipos adequados, evitando strings.
- [ ] Criar objetos para Dano, Rolagem, Dinheiro, Vida e Categoria.
- [ ] Definir invariantes nos modelos e services.
- [ ] Criar uma regra extensível para slots de equipamento.
- [ ] Evitar campos mutáveis quando a alteração precisar de validação.

### Interfaces e Dependências

- [ ] Criar interfaces para os repositories.
- [ ] Injetar dependências em vez de instanciá-las diretamente no `App`.
- [ ] Criar uma configuração central da aplicação.
- [ ] Padronizar exceções de validação, persistência e aplicação.
- [ ] Adicionar logs técnicos em arquivo, separados das mensagens mostradas ao usuário.
- [ ] Definir eventos para alterações importantes, como equipar e receber dano.

## Banco de Dados e Persistência

### Curto Prazo

- [ ] Manter importação dos CSVs atuais para não perder fichas existentes.
- [ ] Criar versão do formato dos dados e migrações.
- [ ] Escapar corretamente `;`, quebras de linha e caracteres especiais.
- [ ] Salvar todos os arquivos usando UTF-8 explicitamente.
- [ ] Usar diretórios diferentes para dados, backups, logs e configurações.

### SQLite Recomendado

- [ ] Migrar a persistência principal de CSV para SQLite.
- [ ] Criar transações para operações que alteram várias tabelas.
- [ ] Usar chaves estrangeiras e restrições para proteger relacionamentos.
- [ ] Criar uma tabela de versão do banco para migrações futuras.
- [ ] Manter exportação e importação em JSON como formato portátil.

Tabelas iniciais sugeridas:

- `personagens`
- `atributos`
- `itens`
- `equipamentos`
- `habilidades`
- `anotacoes`
- `permissoes_armadura`
- `historico_alteracoes`
- `configuracoes`

### Várias Fichas

- [ ] Relacionar todos os registros a um `personagem_id`.
- [ ] Permitir duplicar, arquivar e excluir uma ficha.
- [ ] Criar backup e restauração por personagem.
- [ ] Impedir que itens ou equipamentos de fichas diferentes sejam misturados.

## Testes e Qualidade

- [ ] Adicionar JUnit 5 ao projeto.
- [ ] Criar testes unitários para cada service.
- [ ] Usar diretórios temporários nos testes de repository.
- [ ] Testar migração de formatos antigos.
- [ ] Testar entradas UTF-8 e textos longos.
- [ ] Testar terminais com tamanhos diferentes.
- [ ] Testar navegação, Enter, Esc, setas, Backspace e Delete.
- [ ] Testar todos os cancelamentos para garantir que nada seja salvo.
- [ ] Criar testes de integração para fluxos completos.
- [ ] Adicionar cobertura de código no CI.
- [ ] Configurar formatação automática e análise estática.
- [ ] Revisar mensagens e nomes para manter todo o texto em PT-BR.

## Distribuição e Operação

- [ ] Gerar versões para Windows, macOS e Linux automaticamente.
- [ ] Publicar artefatos versionados em releases.
- [ ] Exibir a versão do programa na tela principal ou na ajuda.
- [ ] Criar notas de atualização por versão.
- [ ] Assinar os aplicativos com certificados de distribuição quando possível.
- [ ] Garantir que atualizar o programa não apague os dados do usuário.
- [ ] Criar diagnóstico para mostrar caminhos de dados, logs e versão do Java.
- [ ] Adicionar opção de restaurar o último backup.

## Roadmap Sugerido

### Etapa 1 - Confiabilidade

1. Mover os dados para uma pasta do usuário.
2. Implementar backup e escrita atômica.
3. Criar interfaces de repository.
4. Adicionar JUnit 5 e testes das regras atuais.
5. Corrigir estados inválidos existentes ao carregar os dados.

### Etapa 2 - Estrutura

1. Dividir `FichaTui` por tela e componente.
2. Criar formulários e navegação reutilizáveis.
3. Introduzir identificadores estáveis.
4. Migrar para SQLite com importação dos CSVs atuais.
5. Adicionar suporte a várias fichas.

### Etapa 3 - Conteúdo

1. Implementar modificadores, perícias e testes de resistência.
2. Criar a tela de combate e rolagens.
3. Melhorar inventário, peso e equipamentos.
4. Adicionar magias, usos e descansos.
5. Criar histórico e exportação da ficha.

### Etapa 4 - Produto

1. Melhorar acessibilidade, ajuda e temas.
2. Automatizar builds e releases multiplataforma.
3. Adicionar diagnóstico, logs e restauração de backup.
4. Revisar desempenho e experiência em terminais menores.

## Critérios para uma Melhoria Estar Pronta

- A regra está implementada fora da interface quando for regra de domínio.
- Entradas inválidas são tratadas sem encerrar a aplicação.
- Esc cancela a operação sem alterações parciais.
- Há teste automatizado para o comportamento principal.
- Textos estão em PT-BR e UTF-8.
- O funcionamento foi verificado no JAR e nos pacotes distribuídos.
- Dados de versões anteriores continuam sendo carregados ou migrados.
