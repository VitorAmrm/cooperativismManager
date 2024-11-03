# cooperativismManager
A aplicação faz o gerenciamento de Assembleias, sendo possivel cadastrar pautas, abrir sessão de votação para essas pautas, etc.

![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![SpringBoot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200.svg?style=for-the-badge&logo=Flyway&logoColor=white)
![Jmeter](https://img.shields.io/badge/Apache%20JMeter-D22128.svg?style=for-the-badge&logo=Apache-JMeter&logoColor=white)

# Tabela de Variáveis

| Nome da Variável                           | Tipo        | Descrição                                             | Valor Padrão                        |
|--------------------------------------------|-------------|-------------------------------------------------------|-------------------------------------|
| `spring.application.name`                  | `String`    | Nome da aplicação                                     | `cooperativism.manager`             |
| `server.port`                              | `int`       | Porta do servidor                                     | `8080`                              |
| `spring.docker.compose.enabled`            | `boolean`   | Habilita o uso do Docker Compose                      | `false`                             |
| `flyway.baselineOnMigrate`                | `boolean`   | Define se a baseline deve ser aplicada ao migrar     | `true`                              |
| `flyway.baselineVersion`                   | `int`       | Versão da baseline do Flyway                          | `1`                                 |
| `spring.datasource.url`                    | `String`    | URL do banco de dados PostgreSQL                      | `jdbc:postgresql://localhost:5432/postgres` |
| `spring.datasource.username`                | `String`    | Nome de usuário para o banco de dados                 | `postgres`                          |
| `spring.datasource.password`                | `String`    | Senha para o banco de dados                           | `root`                              |
| `spring.jpa.database-platform`              | `String`    | Dialeto do Hibernate para JPA                         | `org.hibernate.dialect.PostgreSQLDialect` |
| `spring.jpa.hibernate.ddl-auto`            | `String`    | Configuração de DDL do Hibernate                      | `none`                              |
| `spring.jpa.properties.hibernate.show_sql` | `boolean`   | Mostra as SQL geradas pelo Hibernate                  | `true`                              |
| `spring.jpa.properties.hibernate.format_sql` | `boolean` | Formata as SQL geradas pelo Hibernate                 | `true`                              |
| `kafka.voting.result.topic`                | `String`    | Tópico do Kafka para resultados de votação            | `voting-result`                     |
| `spring.kafka.bootstrap-servers`           | `String`    | Endereço do servidor Kafka                            | `localhost:9092`                   |
| `cpf.validator.url`                        | `String`    | URL do validador de CPF                               | `http://localhost:3001/`           |
| `management.endpoints.web.exposure.include` | `String`   | Endpoints de gerenciamento expostos                   | `health,info`                      |
| `management.endpoint.health.show-details`  | `String`    | Exibe detalhes da saúde do aplicativo                 | `always`                            |
| `feign.backoffperiod.value`                | `int`       | Período de espera entre tentativas do Feign          | `1000`                              |
| `feign.retries.qtt`                        | `int`       | Número de tentativas do Feign                         | `3`                                 |
| `request.limit.filter.value`               | `int`       | Limite de requisições do filtro                       | `150`                               |

# Como subir a Stack
:warning:

:rotating_light: :rotating_light: :rotating_light:
Antes de tudo é importante que o java da máquina que irá executar o `passo 3º` seja o Java 21
:rotating_light: :rotating_light: :rotating_light:

- `1º` Realizar o `git clone -b v1.0.0.0 https://github.com/VitorAmrm/cooperativismManager.git`
- `2º` Rodar no terminal `cd cooperativismManager`
- `3º` Rodar no terminal `mvn compile jib:dockerBuild`
- `4º` Rodar no terminal `cd doc/stack`
- `5º` Rodar no terminal `docker compose up -d`

Após os passos acima todas as aplicações necessarias para a aplicação rodar já estarão criadas e a aplicação estará rodando.

Então fica a cargo do testador como testar.

### [Aqui está disponivel a documentação Swagger](https://github.com/VitorAmrm/cooperativismManager/blob/v1.0.0.0/doc/swagger/openapi_cooperativism.yml)

### [Ou se preferir pode baixar a coleção de requisições do postman](https://github.com/VitorAmrm/cooperativismManager/blob/v1.0.0.0/doc/postman/Cooperatvism%20Manager.postman_collection.json)

## Informações Importantes

### Mockoon
Como não foi possivel realizar requisições para o link `https://user-info.herokuapp.com/users/`, listado na Documentação, tomei a liberdade de criar uma API mockada que retorna `UNABLE_TO_VOTE` caso no cpf não tenha o digito 8 e `ABLE_TO_VOTE` caso contrario, entretanto a url de requisição pode ser facilmente alterada apenas modificando a variavel `cpf.validator.url`, caso o link `https://user-info.herokuapp.com/users/` esteja funcionando fique a vontade para alterar e testar.

### Jmeter
Para o teste de stress tive que utilizar um plugin do Jmeter dentro do plano que foi o [ConcurrencyThreadGroup](https://jmeter-plugins.org/wiki/ConcurrencyThreadGroup/) para importar o arquivo jmx com sucesso, esse plugin teria que ser instalado.
