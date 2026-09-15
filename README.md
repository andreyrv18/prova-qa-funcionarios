# Como Rodar o Projeto

## Clonar projeto

Primeiro clone o projeto usando o comando

```bash
 git clone git@github.com:andreyrv18/prova-qa-funcionarios.git
```

Após clonar o repositório acesse o diretório:

````bash
  cd prova-qa-funcionarios
````

aqui terão algums diretórios sendo eles `frontend`, `backend` e `qa`

## Rodar com Docker

```bash
  docker compose up --build -d
```

Aguarde ser iniciado os servicos:

- db_qa_prova
- frontend_qa-prova
- backend_qa_prova

Ao concluir aparacerá essa mensagem

````plaintext
[+] Running 7/7
 ✔ 10-09-prova-qa-funcionarios-backend               Built                                                         0.0s
 ✔ 10-09-prova-qa-funcionarios-frontend              Built                                                         0.0s
 ✔ Network 10-09-prova-qa-funcionarios_qa-prova_net  Created                                                       0.1s
 ✔ Volume "10-09-prova-qa-funcionarios_db_data"      Created                                                       0.0s
 ✔ Container db_qa_prova                             Healthy                                                      11.7s
 ✔ Container backend_qa_prova                        Started                                                      11.8s
 ✔ Container frontend_qa-prova                       Started  
````

Os serviços dependem um do outro, caso o Banco de dados falhe o docker compose foi configurado para não rodar os outros container.
é utlizado no compose o Heath Check para validar se o **Banco** está saudavél `test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]` Quando tem o retorno do banco de dados continua subindo os seviços mas o backend tem a condição de subir apenas se o **banco** estiver nessa condição `condition: service_healthy`. Uma vez que o backend subir o frontend que depende dele sobe

Para remover os serviços:

````shell
docker compose down -v
````

Aguarde a mensagem:

```plaintext
[+] Running 5/5
 ✔ Container frontend_qa-prova                       Removed                                                       0.5s
 ✔ Container backend_qa_prova                        Removed                                                       0.5s
 ✔ Container db_qa_prova                             Removed                                                       2.2s
 ✔ Volume 10-09-prova-qa-funcionarios_db_data        Removed                                                       0.1s
 ✔ Network 10-09-prova-qa-funcionarios_qa-prova_net  Removed                                                       0.4s

```

---

## Rodar Localmente

Para rodar localmente pode subir somente o container do banco de dados usando:

Docker DB
````bash
  docker compose up db --build -d
````

Agurade o Banco de dados subir
````plaintext
[+] Running 3/3
 ✔ Network 10-09-prova-qa-funcionarios_qa-prova_net  Created
 ✔ Volume "10-09-prova-qa-funcionarios_db_data"      Created
 ✔ Container db_qa_prova                             Started
````


Pode criar um novo banco no Mysql Workbench ou Dbaver usando os **scripts sql** do flyway `backend/src/main/resources/db/migration` aqui tem a criação das tabelas **V1__** os dados iniciais **V2__** e dos dados de seed para teste **V999__**

se usar as mesmas credenciais que estão no `.env` **DB_URL, DB_USER, DB_PASSWORD** vai funcionar também.


Backend:

estando na raiz do projeto acesse

````shell
    cd backend
````

Depois rode o spring boot:
````shell
    mvn spring-boot:run
````

Ao rodar o comando vai aparecer esses dados:

````plaintext
[INFO] Scanning for projects...
[INFO]
[INFO] -------------------------< org.backend:prova >--------------------------
[INFO] Building backend 0.0.5
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] >>> spring-boot:4.1.1:run (default-cli) > test-compile @ prova >>>
[INFO]
[INFO] --- resources:3.5.0:resources (default-resources) @ prova ---
[INFO] Copying 1 resource from src\main\resources to target\classes
[INFO] Copying 5 resources from src\main\resources to target\classes
[INFO]
[INFO] --- compiler:3.15.0:compile (default-compile) @ prova ---
[INFO] Nothing to compile - all classes are up to date.
[INFO]
[INFO] --- resources:3.5.0:testResources (default-testResources) @ prova ---
[INFO] skip non existing resourceDirectory C:\Users\Andrerv\WebstormProjects\10-09-prova-qa-funcionarios\backend\src\test\resources
[INFO]
[INFO] --- compiler:3.15.0:testCompile (default-testCompile) @ prova ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 3 source files with javac [debug parameters release 21] to target\test-classes
[INFO]
[INFO] <<< spring-boot:4.1.1:run (default-cli) < test-compile @ prova <<<
[INFO]
[INFO]
[INFO] --- spring-boot:4.1.1:run (default-cli) @ prova ---
[INFO] Attaching agents: []

██████╗ ██████╗  ██████╗ ██╗   ██╗ █████╗     ██████╗ ██╗██╗  ██╗██╗
██╔══██╗██╔══██╗██╔═══██╗██║   ██║██╔══██╗    ██╔══██╗██║╚██╗██╔╝██║
██████╔╝██████╔╝██║   ██║██║   ██║███████║    ██║  ██║██║ ╚███╔╝ ██║
██╔═══╝ ██╔══██╗██║   ██║╚██╗ ██╔╝██╔══██║    ██║  ██║██║ ██╔██╗ ██║
██║     ██║  ██║╚██████╔╝ ╚████╔╝ ██║  ██║    ██████╔╝██║██╔╝ ██╗██║
╚═╝     ╚═╝  ╚═╝ ╚═════╝   ╚═══╝  ╚═╝  ╚═╝    ╚═════╝ ╚═╝╚═╝  ╚═╝╚═╝


````


Após rodar o Backend volte para a pasta raiz:

````shell
    cd ..
````

Depois vamos acessar o Frontend (considerando que está na raiz do projeto):

````shell
    cd frontend
````

e rodar o comando:

````shell
    npm run dev
````

Ao rodar o comando vai aparecer esses dados:

````plaintext

> frontend@0.0.0 dev
> vite

Port 5173 is in use, trying another one...

  VITE v8.2.2  ready in 349 ms

  ➜  Local:   http://localhost:5174/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help

````

--- 

## Decições Técnicas

explicar sobre:

1. backend
    - .env
    - flyway
    - Spring segurity
    - cors
    - dto
    - MVC
    - GlobalExceptionAdvice
    - diretoório de util
    - java google formatter
    -
2. Frontend
    - Internacionalização
    - prettier, style lint

---

# Rotas disponívels

Disponível também na coleção Postman em:

````shell

````

## Api

Todas rotas da API usam por padrão o prefixo: /api

1. Swagger UI:
    - `http://localhost:8080/api/swagger-ui/index.html#/`
2. Cargos
    1. Listar e listar com paginação
        - `http://localhost:8080/cargos/list`
        - `http://localhost:8080/cargos/paginado`
    2. Criar
        - `http://localhost:8080/cargos/criar`
    3. Editar
        - `http://localhost:8080/cargos/editar/QA`
    4. Deletar
        - `http://localhost:8080/cargos/deletar/QA`
    5. Atualizar
        - `http://localhost:8080`
    6. Pesquisar com parametros
        - `http://localhost:8080/cargos/pesquisar?descricaoDoCargo=A`
3. Departamentos
    1. Listar e listar com paginação
        - `http://localhost:8080/departamentos/list`
        - `http://localhost:8080/departamentos/paginado`
    2. Criar
        - `http://localhost:8080/departamentos/criar`
    3. Editar
        - `http://localhost:8080/departamentos/editar/QA`
    4. Deletar
        - `http://localhost:8080/departamentos/deletar/QA`
    5. Atualizar
        - `http://localhost:8080`
    6. Pesquisar com parametros
        - `http://localhost:8080/departamentos/pesquisar?descricaoDoCargo=A`

---

### Comandos Docker

- "dev": "docker compose up",
    - "dev:build": "docker compose up --build",
    - "build": "docker compose build",
    - "build:no-cache": "docker compose build --no-cache",
    - "down": "docker compose down",
    - "logs": "docker compose logs -f",
    - "ps": "docker compose ps",

> build container somente frontend
>> docker build --no-cache -t teste-frontend ./frontend

---

# Referencias

Padroes e Convenções Utilizadas
1. Convencional Commits https://www.conventionalcommits.org/pt-br/v1.0.0-beta.4/
2. Semantic Versioning 2.0.0  https://semver.org/


Formatadores de Código:
1. Fomatador de codigo para Java: https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config
2. Formatador de código Prettier https://prettier.io/
3. Formatador de Estilos css StyleLint https://stylelint.io/
