# Help Desk MVP

Um MVP de um sistema de abertura e acompanhamento de chamados (Help Desk) desenvolvido para um teste prático.

## Tecnologias Utilizadas
- **Frontend:** Angular 17
- **Backend:** Java 17 com Spring Boot 3
- **Banco de Dados:** PostgreSQL 15
- **Infraestrutura:** Docker e Docker Compose

## Justificativas de Arquitetura e Decisões
- **Spring Boot:** Escolhido pela agilidade em desenvolver APIs RESTful consistentes, utilizando Spring Data JPA para o banco de dados e facilitando a implementação de regras de negócio complexas.
- **Angular:** Framework robusto e componentizado, garantindo uma estrutura escalável para o frontend. A estilização será baseada em CSS padrão e Bootstrap (ou similar) para obter um design simples e intuitivo.
- **Docker/Docker Compose:** Utilizado para padronizar o ambiente, permitindo que a aplicação (backend + frontend + db) possa ser executada em qualquer máquina de forma previsível.

### Regra de Distribuição Automática
Para a funcionalidade de distribuição automática, foi criado um método no `TicketService` que:
1. Conta a quantidade de chamados em aberto (status `ABERTO` e `EM_ANDAMENTO`) por responsável.
2. Identifica todos os usuários do sistema. Se algum tiver `0` chamados em aberto, o sistema atribui o chamado a ele primeiro (para equilibrar melhor a carga).
3. Caso todos tenham chamados, atribui ao responsável com o menor número de chamados.
Foi definido que chamados nos status "ABERTO" e "EM_ANDAMENTO" contam como "não concluídos". Os status "RESOLVIDO" e "FECHADO" não pesam na carga de trabalho.

## Instruções de Instalação e Execução Local

### Pré-requisitos
- [Docker](https://www.docker.com/get-started) e Docker Compose instalados na máquina.

### Passos para Executar

1. Clone o repositório ou descompacte o código fonte.
2. Navegue até a raiz do projeto (onde está o arquivo `docker-compose.yml`):
   ```bash
   cd help-desk-mvp
   ```
3. Suba os containers usando o docker-compose:
   ```bash
   docker compose up --build
   ```
   > Observação: A primeira execução demorará um pouco, pois irá baixar as imagens do banco, compilar o backend (via maven no docker) e compilar o frontend (via npm no docker).

4. **Acessar a Aplicação:**
   - **Frontend:** http://localhost:80
   - **Backend API:** http://localhost:8080/api/tickets

### Banco de Dados
A criação e população do banco de dados ocorrem de maneira automatizada:
- O container do PostgreSQL já sobe um banco chamado `helpdesk`.
- O Spring Boot (`spring.jpa.hibernate.ddl-auto=update`) cria as tabelas `users` e `tickets` automaticamente na primeira execução.
- O arquivo `data.sql` no backend possui scripts de `INSERT ... ON CONFLICT DO NOTHING` para garantir que o banco já nasça com pelo menos 3 responsáveis (Alice, Bob e Carlos).

## Eventuais Trade-offs e Melhorias Futuras
- **Segurança e Autenticação:** Como é um MVP e o foco estava em uma regra de negócio específica (distribuição equilibrada) e em um design limpo, não foi implementado um sistema de Login/JWT. Os responsáveis são escolhidos livremente (mockando um cenário onde já haveria autenticação).
- **Testes:** Foram configurados testes automatizados básicos unitários, porém, testes E2E no Angular ou integração pesada no backend foram omitidos devido ao prazo e escopo do MVP. A base está pronta para fácil inclusão de JUnit/Mockito e Jasmine/Karma.
- **Design Simples:** Foi priorizada a usabilidade e a clareza para o usuário na interface, evitando overengineering visual no MVP.

## Bibliotecas Externas Adicionais
- **Lombok:** Para reduzir a verbosidade de getters e setters no Java.
- **Nginx (Docker):** Para servir o build de produção do Angular de forma leve.
