# Cadastro de Livros 📚
Sistema para cadastro e consulta de livros com relatórios.

### Pré-requisitos 📋
Para executar este projeto é necessário ter instalado em sua máquina o 
- [ ] Docker;
- [ ] Docker Compose;
- [ ] Maven 3;
- [ ] Java 17;

## Começando 🚀

### Criado o Container de Banco de Dados 🐳
Após realizar o clone deste projeto, certifique-se de ter instalado o Docker e o Docker Compose em sua máquina. 
Após isso execute o comando abaixo para subir o banco de dados PostgreSQL.

```
docker-compose up -d --build
```

Este comando ira realizar a criação de um container com o banco de dados PostgreSQL, com as configurações definidas no arquivo `docker-compose.yaml`.

### Testes Automatizados 🔩
Para a execução dos testes automatizados, primeiro crie um banco de dados com o nome `livrosdb_teste`. Somente depois disto execute o comando abaixo:

```
mvn test
```

### Executando a aplicação 📦
Para executar a aplicação, execute o comando abaixo:

```
mvn spring-boot:run
```

Após a execução do comando acima, a aplicação estará disponível no endereço `http://localhost:8080`.

## Documentação de Referencia 📖
Para referência adicional, considere as seguintes seções:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.0/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.0/reference/actuator/index.html)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.0/reference/web/spring-security.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/3.4.0/reference/io/validation.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.0/reference/using/devtools.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.0/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Docker Compose Support](https://docs.spring.io/spring-boot/3.4.0/reference/features/dev-services.html#features.dev-services.docker-compose)

### Guias Extras 📋
Os guias a seguir ilustram como usar alguns recursos concretamente:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
