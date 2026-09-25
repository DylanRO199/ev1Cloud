# ms-vidasalud-appointments

Microservicio de negocio responsable de la administración y ciclo de vida de las **Atenciones Médicas** de VidaSalud (Evaluación Parcial 1 - DSY1107 - Duoc UC).

## 📌 Requisitos Previos

- **Java JDK**: 17 LTS
- **Apache Maven**: 3.8+

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Data JPA & Hibernate**
- **Base de datos H2** (en memoria para perfil `local`) y soporte **Oracle Database** (perfil `cloud`)
- **Jakarta Bean Validation**

## ⚙️ Reglas de Transición de Estados

El microservicio implementa una máquina de estados estricta en el dominio para evitar inconsistencias operativas:

- `SOLICITADA` ➔ `CONFIRMADA`
- `CONFIRMADA` ➔ `EN_ESPERA`
- `EN_ESPERA` ➔ `EN_ATENCION`
- `EN_ATENCION` ➔ `CERRADA`
- `SOLICITADA` / `CONFIRMADA` ➔ `CANCELADA`

Cualquier transición fuera de este flujo retorna un error HTTP `400 Bad Request`.

## 💻 Endpoints Principales

- `GET /api/appointments`: Listado de atenciones (soporta parámetro de filtro `?status=...`).
- `GET /api/appointments/{id}`: Detalle de una atención por su ID.
- `POST /api/appointments`: Creación de una nueva cita médica.
- `PUT /api/appointments/{id}/status`: Actualización del estado de una atención según reglas de negocio.

## 💻 Ejecución

```bash
# Ejecutar pruebas unitarias
mvn clean test

# Iniciar microservicio (Puerto 8081)
mvn spring-boot:run
```
