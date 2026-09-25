# ms-vidasalud-bff

Microservicio **Backend-for-Frontend (BFF)** de la plataforma VidaSalud, desarrollado para la Evaluación Parcial 1 de **Desarrollo Cloud Native I (DSY1107)** - Duoc UC.

Actúa como punto de entrada de la arquitectura interna del backend, validando tokens JWT emitidos por Microsoft Entra ID, aplicando políticas de autorización por roles (`ADMIN`, `RECEPCIONISTA`, `PACIENTE`) y delegando las peticiones a los microservicios de dominio.

## 📌 Requisitos Previos

- **Java Development Kit (JDK)**: 17 LTS
- **Apache Maven**: 3.8+ (o Maven Wrapper)

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security 6** (OAuth2 Resource Server / JWT)
- **Spring Web / RestClient** para comunicación HTTP interna
- **Spring Boot Actuator** (Métricas y estado de salud)

## ⚙️ Variables de Entorno

Puedes definir las variables en un archivo `.env` o en las variables de sistema del entorno de ejecución:

| Variable | Descripción | Valor por defecto (Local) |
|---|---|---|
| `SERVER_PORT` | Puerto de escucha | `8080` |
| `ENTRA_TENANT_ID` | ID del inquilino de Azure Entra ID | Requerido |
| `ENTRA_API_CLIENT_ID`| ID de aplicación registrado para la API | Requerido |
| `APPOINTMENTS_URL` | URL del microservicio de atenciones | `http://localhost:8081` |
| `CATALOG_URL` | URL del microservicio de catálogo | `http://localhost:8082` |
| `CORS_ALLOWED_ORIGINS`| Orígenes permitidos para peticiones CORS | `http://localhost:4200` |

## 💻 Compilación y Ejecución

1. **Ejecutar pruebas unitarias:**
   ```bash
   mvn clean test
   ```

2. **Iniciar servicio en desarrollo:**
   ```bash
   mvn spring-boot:run
   ```

3. **Generar empaquetado JAR:**
   ```bash
   mvn clean package -DskipTests
   ```

## 🔒 Mecanismos de Seguridad Implementados

1. **Validación JWT**:
   - Comprobación de firma criptográfica mediante claves públicas JWKS de Microsoft (`/discovery/v2.0/keys`).
   - Verificación estricta de emisor (`issuer-uri`) y destinatario (`audience` = `api://<CLIENT_ID>`).
   - Comprobación de vigencia y fechas de expiración del token.
2. **Control de Acceso basado en Roles**:
   - `ADMIN`: Acceso total a consulta y modificación de atenciones y catálogo.
   - `RECEPCIONISTA`: Operaciones de gestión, cambio de estados de atenciones y confirmaciones.
   - `PACIENTE`: Consulta de atenciones propias y creación de solicitudes.
3. **Manejo de Errores Estandarizado**:
   - Código HTTP `401 Unauthorized` ante token faltante, expirado o con firma inválida.
   - Código HTTP `403 Forbidden` ante usuario autenticado sin el rol correspondiente.
