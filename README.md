# Otus Highload Homework 1

Novopashin Alexey Alexandrovich

## Startup

1) Start PostgreSQL database at `localhost:5432` with username `posgresql` and password `pass` (there
   are `docker-compose.yml` attached using ARM image architecture).
2) Create database `social_network`
3) Execute DDL script located at `src/main/resources/seed.sql` in database `social_network`.
4) Run Gradle (next command)

```shell
# if unix-based (linux, mac)
./gradlew run
```

OR

```shell
# if windows
gradlew.bat run
```

Swagger will be hosted at [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

## Security

Project using hashing algorithm `PBKDF2WithHmacSHA1` using salt specified
in `src/main/resources/application.properties`. To change it, look into `security.*` section.