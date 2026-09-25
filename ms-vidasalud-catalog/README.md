# ms-vidasalud-catalog

Microservicio de negocio encargado de administrar el catálogo de prestaciones médicas, boxes de atención y cupos disponibles de VidaSalud (Evaluación Parcial 1 - DSY1107 - Duoc UC).

## 📌 Requisitos Previos

- **Java JDK**: 17 LTS
- **Apache Maven**: 3.8+

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA & Hibernate**
- **H2 Database** (Desarrollo local con datos semilla) y **Oracle Database** (Perfil nube)

## 💻 Endpoints Principales

- `GET /api/catalog/services`: Listado de prestaciones médicas activas con nombre, descripción y aranceles.
- `POST /api/catalog/services`: Registro de una nueva prestación médica.
- `GET /api/catalog/boxes`: Listado de boxes clínicos y capacidad disponible.
- `POST /api/catalog/boxes`: Registro y parametrización de boxes de atención.

## 💻 Ejecución

```bash
# Ejecutar pruebas unitarias
mvn clean test

# Iniciar microservicio (Puerto 8082)
mvn spring-boot:run
```
