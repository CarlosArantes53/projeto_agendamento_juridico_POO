# HERMES - Sistema de Agendamentos Jurídicos

HERMES é um sistema desktop desenvolvido em Java para gerenciamento de escritórios de advocacia, com foco em cadastro de clientes, gestão de usuários e agendamentos de compromissos jurídicos.

## Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração do Banco de Dados](#configuração-do-banco-de-dados)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Como Executar](#como-executar)
- [Detalhamento dos Pacotes e Classes](#detalhamento-dos-pacotes-e-classes)

## Visão Geral

O sistema HERMES é uma aplicação desktop para escritórios de advocacia que facilita o gerenciamento de clientes, controle de usuários (advogados e secretários) e agendamentos. Desenvolvido com uma interface gráfica amigável usando o Swing do Java, o sistema se conecta a um banco de dados MySQL para persistir as informações.

## Tecnologias Utilizadas

- **Linguagem**: Java 8+
- **Interface Gráfica**: Java Swing
- **Banco de Dados**: MySQL
- **Conexão com DB**: JDBC
- **Padrões de Projeto**: DAO (Data Access Object), MVC (Model-View-Controller)

## Estrutura do Projeto

```
src/
├── app/           # Ponto de entrada da aplicação
├── config/        # Configurações do sistema
├── dao/           # Classes de acesso a dados
│   └── base/      # Classe base para DAOs
├── gui/           # Interface gráfica
│   ├── cliente/   # Interfaces relacionadas a clientes
│   ├── common/    # Componentes comuns de interface
│   ├── usuario/   # Interfaces relacionadas a usuários
│   └── util/      # Utilitários para interface
├── modelo/        # Classes de modelo (entidades)
└── util/          # Classes utilitárias
```

## Configuração do Banco de Dados

O sistema utiliza um banco de dados MySQL com as seguintes configurações:

- **Nome do banco**: `advocacia_mvp`
- **URL**: `jdbc:mysql://localhost:3306/advocacia_mvp`
- **Usuário**: `root`
- **Senha**: `1234`

### Tabelas Principais

- **usuarios**: Armazena os dados dos usuários do sistema (advogados e secretários)
- **clientes**: Armazena os dados dos clientes do escritório

## Funcionalidades Principais

1. **Autenticação de Usuários**
   - Login e Logout
   - Recuperação de senha
   - Gerenciamento de perfis

2. **Gerenciamento de Clientes**
   - Cadastro de novos clientes
   - Edição de dados de clientes
   - Listagem de clientes
   - Exclusão de clientes

3. **Dashboard**
   - Visualização de informações gerais

4. **Interface Responsiva**
   - Menu lateral retrátil
   - Painéis dinâmicos com design moderno

## Como Executar

1. Certifique-se de ter o Java 8 ou superior instalado
2. Configure o banco de dados MySQL:
   - Crie um banco de dados chamado `advocacia_mvp`
   - As tabelas serão criadas automaticamente na primeira execução (ou use os scripts SQL fornecidos)
3. Compile o projeto:
   ```
   javac -d bin src/app/MainApp.java
   ```
4. Execute o aplicativo:
   ```
   java -cp bin app.MainApp
   ```

## Detalhamento dos Pacotes e Classes

### Pacote `app`

- **MainApp.java**: Ponto de entrada da aplicação, contém o método `main` que inicia a interface gráfica.

### Pacote `config`

- **DatabaseConfig.java**: Gerencia a conexão com o banco de dados MySQL, contendo métodos para estabelecer e fechar conexões.

### Pacote `dao`

- **ClienteDAO.java**: Implementa operações CRUD para a entidade Cliente.
- **UsuarioDAO.java**: Implementa operações CRUD para a entidade Usuario, incluindo autenticação.

#### Subpacote `dao.base`

- **BaseDAO.java**: Classe abstrata que fornece métodos genéricos para acesso a dados, utilizando padrões funcionais do Java 8.

### Pacote `gui`

- **HomePanel.java**: Painel principal após o login, contendo o menu lateral e área de conteúdo dinâmico.
- **MainFrame.java**: Janela principal da aplicação, responsável por gerenciar a navegação entre os diferentes painéis.

#### Subpacote `gui.cliente`

- **CadastroClientePanel.java**: Formulário para cadastro de novos clientes.
- **ClientesListPanel.java**: Lista todos os clientes cadastrados com opções para editar e excluir.
- **EditarClientePanel.java**: Formulário para edição de dados de clientes existentes.

#### Subpacote `gui.common`

- **ActionPanel.java**: Painel reutilizável para botões de ação.
- **FormPanel.java**: Painel reutilizável para formulários.
- **FormValidator.java**: Utilitário para validação de campos de formulários.
- **HeaderPanel.java**: Painel reutilizável para cabeçalhos de telas.
- **TableActionListener.java**: Interface para lidar com ações em tabelas.

#### Subpacote `gui.usuario`

- **CadastroUsuarioPanel.java**: Formulário para cadastro de novos usuários.
- **EditarPerfilPanel.java**: Formulário para edição do perfil do usuário logado.
- **LoginPanel.java**: Tela de login do sistema.
- **RecuperarSenhaPanel.java**: Formulário para recuperação de senha.

#### Subpacote `gui.util`

- **IconManager.java**: Gerencia os ícones utilizados na interface.
- **TableActionCellEditor.java**: Editor personalizado para células de ação em tabelas.
- **TableActionCellRenderer.java**: Renderizador personalizado para células de ação em tabelas.
- **UIConstants.java**: Constantes e utilitários para UI, centralizando definições de estilos visuais.

### Pacote `modelo`

- **Cliente.java**: Entidade que representa um cliente do escritório.
- **Usuario.java**: Entidade que representa um usuário do sistema, podendo ser advogado ou secretário.

### Pacote `util`

- **ValidadorUtil.java**: Contém métodos para validação de dados como CPF, CNPJ, email, senha e telefone.

## Considerações de Design

O sistema foi desenvolvido seguindo princípios de clean code e padrões de projeto:

1. **Data Access Object (DAO)**: Separa a lógica de acesso a dados das regras de negócio
2. **Model-View-Controller (MVC)**: Separa a aplicação em três componentes principais:
   - Model: Classes de modelo e DAOs
   - View: Painéis GUI
   - Controller: Lógica que conecta o modelo e a visualização

3. **Componentes Reutilizáveis**: A interface gráfica utiliza componentes reutilizáveis para manter consistência visual e reduzir duplicação de código.

4. **Lambda Expressions**: Utiliza recursos do Java 8 como expressões lambda e interfaces funcionais para tornar o código mais conciso.

5. **Validação de Dados**: Implementa validações completas para garantir a integridade dos dados.

## Licença

Este projeto é de código aberto e está disponível sob a licença MIT.