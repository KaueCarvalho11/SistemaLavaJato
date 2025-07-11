# Sistema Lava Jato

Este é um projeto Java para gerenciamento de um sistema de lava jato.

## Estrutura do Projeto

O projeto segue as convenções padrão Maven/Java:

```
SistemaLavaJato/
├── pom.xml                          # Configuração Maven
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── ufersa/
│   │               └── sistemalavajato/
│   │                   ├── Program.java           # Classe principal
│   │                   ├── model/                 # Modelos de dados
│   │                   │   ├── Usuario.java       # Classe abstrata base
│   │                   │   ├── Funcionario.java   # Herda de Usuario
│   │                   │   ├── Cliente.java       # Cliente do lava jato
│   │                   │   ├── Veiculo.java       # Veículo a ser lavado
│   │                   │   └── Servico.java       # Serviço prestado
│   │                   ├── repository/            # Camada de dados
│   │                   ├── service/               # Lógica de negócio
│   │                   └── ui/                    # Interface do usuário
│   └── test/
│       └── java/                                  # Testes unitários
└── .vscode/
    └── settings.json                              # Configurações do VS Code
```

## Packages

- `com.ufersa.sistemalavajato` - Package raiz
- `com.ufersa.sistemalavajato.model` - Classes de modelo (entidades)
- `com.ufersa.sistemalavajato.repository` - Camada de persistência de dados
- `com.ufersa.sistemalavajato.service` - Lógica de negócio
- `com.ufersa.sistemalavajato.ui` - Interface do usuário

## Principais Correções Realizadas

1. **Estrutura de diretórios**: Migração de `src/main/sistemalavajato/` para `src/main/java/com/ufersa/sistemalavajato/`
2. **Packages**: Correção de todas as declarações de package para `com.ufersa.sistemalavajato.*`
3. **Configuração Maven**: Adição do arquivo `pom.xml` para gerenciamento de dependências
4. **Configuração VS Code**: Arquivo `.vscode/settings.json` para reconhecimento correto dos source folders
5. **Herança**: Implementação correta da herança entre `Funcionario` e `Usuario`

## Como Executar

1. Certifique-se de ter o Java 11+ instalado
2. Execute: `mvn compile` para compilar o projeto
3. Execute: `mvn exec:java -Dexec.mainClass="com.ufersa.sistemalavajato.Program"` para executar

## Desenvolvimento

- Use VS Code com a extensão Java Extension Pack
- O projeto agora está configurado corretamente com Maven
- As declarações de package estão corretas e seguem as convenções Java
