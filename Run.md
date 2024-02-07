# Pasos para ejecutar Spring Boot Blog App

## 1. Construir el proyecton con Maven
Si tienes instalado maven puedes ejecutar el siguiente comando
```
mvn clean package
```
Si no tienes instalado maven puedes ejecutar el siguiente comando
```
./mvnw clean package
 ```
> Nota: Debes estar en el directorio raíz del proyecto para ejecutar los comandos anteriores

 ## 2. Creación de la base de datos
Antes de ejecutar la aplicación, necesitas tener un servidor de Postgresql creado y ejecutar el script `sql/myblog-ddl-script.sql`.
Si usas Docker, puedes usar el siguiente comando:

```
docker-compose up -d 
```

Generará dos contenedores:
- Uno con Postgresql, que ejecutará el script mencionado al inicializar.
- Otro con pgAdmin

> Nota: Debes estar en el directorio raíz del proyecto para ejecutar los comandos anteriores

 ## 3. Ejecutar el proyecto de Spring Boot
Utiliza el comando siguiente para ejecutar la aplicación:
 ```
 mvn spring-boot:run
 ```
Una vez ejecuta la aplicación, Hibernate **validará** el esquema de la base de datos con las entidades definidas, para garantizar que no hay ningún error en la base de datos.

Entonces, la aplicación de Spring Boot estará lista para utilizarse.