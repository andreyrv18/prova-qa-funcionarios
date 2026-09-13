# Como Rodar o Projeto

## Rodar com Docker

```bash
docker compose up --build -d
```

Aguarde ser iniciado os servicos:
- db_qa_prova
- frontend_qa-prova
- backend_qa_prova

Ao concluir aparacerá essa mensagem
````shell
[+] Running 7/7
 ✔ 10-09-prova-qa-funcionarios-backend               Built                                                         0.0s
 ✔ 10-09-prova-qa-funcionarios-frontend              Built                                                         0.0s
 ✔ Network 10-09-prova-qa-funcionarios_qa-prova_net  Created                                                       0.1s
 ✔ Volume "10-09-prova-qa-funcionarios_db_data"      Created                                                       0.0s
 ✔ Container db_qa_prova                             Healthy                                                      11.7s
 ✔ Container backend_qa_prova                        Started                                                      11.8s
 ✔ Container frontend_qa-prova                       Started  
````


Para remover os serviços:

````shell
docker compose down -v
````

Aguarde a mensagem:

```shell
[+] Running 5/5
 ✔ Container frontend_qa-prova                       Removed                                                       0.5s
 ✔ Container backend_qa_prova                        Removed                                                       0.5s
 ✔ Container db_qa_prova                             Removed                                                       2.2s
 ✔ Volume 10-09-prova-qa-funcionarios_db_data        Removed                                                       0.1s
 ✔ Network 10-09-prova-qa-funcionarios_qa-prova_net  Removed                                                       0.4s

```



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


## Rodar Localmente


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


# Referencias

1. Fomatador de codigo Java: https://github.com/google/google-java-format/blob/master/README.md#intellij-jre-config
2. Prettier
3. StyleLint
4. 
