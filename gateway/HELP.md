# 1) (option HTTPS) définir l'auteur local et supprimer les identifiants Windows
git config user.name "IliasRabii"
git config user.email "your-email@example.com"

# Supprimer l'identifiant enregistré pour github.com depuis Windows Credential Manager (ou ouvrez GUI Credential Manager et supprimez l'entrée git:github.com)
cmdkey /delete:git:https://github.com

# Retenter le push (on vous demandera le nom d'utilisateur et le PAT)
git push -u origin main

# 2) (option SSH) générer une clé SSH, l'ajouter à l'agent, changer le remote et pousser
ssh-keygen -t ed25519 -C "your-email@example.com"     # suivre les invites
eval $(ssh-agent -s)
ssh-add ~/.ssh/id_ed25519

# Copier le contenu de ~/.ssh/id_ed25519.pub et l'ajouter à https://github.com/settings/ssh-keys
git remote set-url origin git@github.com:IliasRabii/TravelBookingSystem.git
git push -u origin main# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.7/maven-plugin/build-image.html)
* [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/reference/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.7/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.7/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Declarative REST calls with Spring Cloud OpenFeign sample](https://github.com/spring-cloud-samples/feign-eureka)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

