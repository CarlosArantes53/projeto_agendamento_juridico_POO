# HERMES - Sistema de Agendamentos Jurídicos

Um sistema de gerenciamento para escritórios de advocacia.

## Visão Geral

Projeto de POO

## Funcionalidades Principais

- **Autenticação de Usuários**: Login seguro com email e senha
- **Cadastro de Novos Usuários**: Interface para registro de novos advogados e secretários
- **Validação de Dados**: Verificação de formato de email, requisitos de senha e formato de telefone
- **Interface Gráfica Intuitiva**: Desenvolvida com Java Swing

## Tecnologias Utilizadas

- **Linguagem**: Java
- **Interface Gráfica**: Swing
- **Banco de Dados**: MySQL
- **Conexão com BD**: JDBC
- **Padrão de Projeto**: DAO (Data Access Object)

## Requisitos para Execução

- Java JDK 8 ou superior
- MySQL Server 5.7 ou superior
- Driver MySQL Connector/J (disponível na pasta `/lib`)

## Configuração e Instalação

### 1. Configuração do Banco de Dados

Execute o script SQL abaixo para criar o banco de dados e a tabela necessária:

```sql
CREATE DATABASE IF NOT EXISTS advocacia_mvp;
USE advocacia_mvp;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    tipo_usuario ENUM('ADVOGADO', 'SECRETARIO') NOT NULL
);
```

### 2. Configuração da Conexão


```java
private static final String URL = "jdbc:mysql://localhost:3306/advocacia_mvp";
private static final String USUARIO = "root";
private static final String SENHA = "1234";
```

### 3. Bibliotecas Necessárias

O driver JDBC para MySQL está disponível na pasta `/lib` do projeto. Certifique-se de que esse JAR está incluído no classpath do projeto.

## Estrutura do Projeto

### Pacotes e Classes Principais

- **src/dao**: Classes de acesso a dados
  - `ConexaoDB.java`: Gerencia conexões com o banco de dados
  - `UsuarioDAO.java`: Operações CRUD para usuários
  
- **src/gui**: Interface gráfica
  - `LoginForm.java`: Tela de login
  - `CadastroUsuarioForm.java`: Formulário de cadastro
  - `HomeForm.java`: Tela principal após autenticação
  
- **src/modelo**: Classes de modelo
  - `Usuario.java`: Entidade que representa um usuário do sistema
  
- **src/util**: Classes utilitárias
  - `Validador.java`: Funções de validação para email, senha e telefone

- **src**: Package raiz
  - `MainApp.java`: Ponto de entrada da aplicação

## Como Usar o Sistema

1. Execute a classe `MainApp.java` para iniciar o sistema
2. Na tela de login, use credenciais existentes ou clique em "Criar Nova Conta"
3. Ao criar uma conta, preencha todos os campos obrigatórios:
   - Nome completo
   - Email (formato válido)
   - Senha (mínimo 8 caracteres, incluindo letras, números e caracteres especiais)
   - Telefone (mínimo 8 dígitos)
   - Tipo de usuário (Advogado ou Secretário)
4. Após o login bem-sucedido, você será redirecionado para a tela principal