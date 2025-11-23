# Paint-Spray - Sistema de Gestão de Oficina

Sistema desktop desenvolvido em JavaFX para gestão de oficina de pintura de motos.

##  Características

- **Interface Moderna**: Utiliza AtlantaFX para um visual clean e profissional
- **Dashboard Kanban**: Visualização intuitiva do status dos serviços
- **Gestão Completa**: Clientes, Veículos e Ordens de Serviço
- **Banco de Dados**: SQLite embarcado (sem necessidade de servidor)

## Requisitos

- Java 21 ou superior
- Maven 3.6+

## 🚀 Como Executar

### 1. Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- SQLite3 (para popular banco manualmente, opcional)

### 2. Executar a aplicação

```bash
mvn javafx:run
```

### 3. Credenciais de Acesso
Como o projeto ainda está em teste, temos apenas credenciais de teste. Clique em Primeiro Acesso, o usuário padrão vai ser criado.

**Usuário padrão:**
- Email: `admin`
- Senha: `admin`

## 📦 Gerar JAR Executável

```bash
mvn clean package
java -jar target/paintspray-1.0-SNAPSHOT.jar
```

## 🏗️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/paintspray/
│   │       ├── MainApplication.java         # Classe principal
│   │       ├── controller/                  # Controllers JavaFX
│   │       │   ├── LoginController.java
│   │       │   ├── MainController.java
│   │       │   └── SessionManager.java
│   │       ├── model/                       # Entidades
│   │       │   ├── Cliente.java
│   │       │   ├── Usuario.java
│   │       │   ├── Veiculo.java
│   │       │   └── Servico.java
│   │       ├── enums/                       # Enumerações
│   │       │   ├── StatusServico.java
│   │       │   ├── TipoServico.java
│   │       │   └── FormaPagamento.java
│   │       ├── repository/                  # Acesso ao banco
│   │       ├── service/                     # Lógica de negócio
│   │       ├── config/                      # Configurações
│   │       └── util/                        # Utilitários
│   └── resources/
│       └── com/paintspray/
│           ├── fxml/                        # Arquivos FXML
│           │   ├── login.fxml
│           │   └── main.fxml
│           ├── css/                         # Estilos
│           │   └── styles.css
│           └── images/                      # Imagens e ícones
```

## Banco de Dados

O sistema utiliza SQLite com as seguintes tabelas:

- **usuarios**: Dados do proprietário da oficina
- **clientes**: Cadastro de clientes
- **veiculos**: Veículos dos clientes (motos)
- **servicos**: Ordens de serviço (pintura)

## Status de Serviço (Pipeline)

1. **Pendente** → Serviço cadastrado, aguardando início
2. **Em Andamento** → Serviço sendo executado
3. **Aguardando Pagamento** → Serviço concluído, pendente pagamento
4. **Finalizado** → Serviço pago e concluído

## Tecnologias

- **JavaFX 21**: Framework de interface gráfica
- **SQLite**: Banco de dados embarcado


Desenvolvido para a disciplina de Engenharia de Software - UFERSA 2025.2

## 📝 Licença

Este projeto é parte de um trabalho acadêmico.
