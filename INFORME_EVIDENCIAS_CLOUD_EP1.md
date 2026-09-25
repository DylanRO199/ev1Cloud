# INFORME DE EVIDENCIAS CLOUD Y ARQUITECTURA
## EvaluaciÃ³n Parcial NÂ° 1 - Desarrollo Cloud Native I (DSY1107)
**Proyecto**: Plataforma VidaSalud  
**Estudiante**: Dylan  
**InstituciÃ³n**: Duoc UC  

---

## 1. Diagrama de Arquitectura Multinube

La arquitectura implementada para la EvaluaciÃ³n Parcial 1 cumple con el modelo desacoplado y seguro exigido por la pauta:

```
+------------------------------------------------------------------------------------+
|                               MICROSOFT AZURE (IDaaS)                              |
|   Tenant: VidaSalud (vidasalud.onmicrosoft.com)                                    |
|   - vidasalud-frontend (Client ID: SPA)                                            |
|   - vidasalud-api      (Audience: api://vidasalud-api, Scopes: access_as_user)     |
|   - Roles: ADMIN, RECEPCIONISTA, PACIENTE                                          |
+-----------------------------------------+------------------------------------------+
                                          | EmisiÃ³n Access Token JWT
                                          v
+------------------------------------------------------------------------------------+
|                               CLIENTE / FRONTEND                                   |
|   - Angular 19 + MSAL Angular / MSAL Browser                                       |
|   - Vistas: Login, Dashboard, Atenciones                                           |
|   - Interceptor HTTP: InyecciÃ³n automÃ¡tica de cabecera Authorization: Bearer       |
+-----------------------------------------+------------------------------------------+
                                          | PeticiÃ³n HTTPS con Bearer Token
                                          v
+------------------------------------------------------------------------------------+
|                                AMAZON WEB SERVICES (AWS)                           |
|                                                                                    |
|   [AWS API Gateway - HTTP API]                                                     |
|   - JWT Authorizer configurado:                                                    |
|       * Issuer: https://login.microsoftonline.com/{TENANT_ID}/v2.0                 |
|       * Audience: api://{API_CLIENT_ID}                                            |
|       * Valida firma criptogrÃ¡fica y vigencia antes de entrar                      |
|                                         |                                          |
|                                         v (Proxy Privado)                          |
|   [Instancia AWS EC2 (Dockerized Backend)]                                         |
|   - ms-vidasalud-bff (Puerto 8080)                                                 |
|       * Spring Security Resource Server (Segunda validaciÃ³n JWT y Roles)           |
|       * Enrutamiento interno hacia microservicios de dominio                       |
|                                                                                    |
|       +-----------------------------------+-----------------------------------+    |
|       |                                   |                                   |    |
|       v                                   v                                   v    |
|  [ms-vidasalud-appointments:8081]    [ms-vidasalud-catalog:8082]     [Persistencia]|
|  - GestiÃ³n de atenciones             - Boxes y prestaciones          - H2 / OCI    |
|  - MÃ¡quina de estados JPA            - Capacidad y cupos                           |
+------------------------------------------------------------------------------------+
```

---

## 2. Evidencias de ConfiguraciÃ³n en Microsoft Entra ID (Azure)

### Evidencia 01: CreaciÃ³n del Tenant Institucional
- **Nombre de la OrganizaciÃ³n**: `VidaSalud`
- **Dominio Principal**: `VidaSaludDUOC1.onmicrosoft.com`
- **PaÃ­s**: Chile
- **Identificador de Tenant (Tenant ID)**: `d6edb470-bae6-41a0-b8c5-c2f55613b833`
- *PropÃ³sito*: Aislar los recursos de identidad del proyecto mÃ©dico fuera de las restricciones de administraciÃ³n general.

### Evidencia 02: Registro de AplicaciÃ³n Backend (`vidasalud-api`)
- **Application (client) ID**: `2590960d-413c-42a8-a1c7-fc1289f4fb44`
- **URI de ID de AplicaciÃ³n**: `api://vidasalud-api`
- **Ãmbito (Scope) expuesto**: `access_as_user`
  - *Consentimiento*: Administradores y usuarios
  - *Estado*: Habilitado
- **Roles de AplicaciÃ³n definidos**:
  1. `ADMIN`: Permiso total de administraciÃ³n clÃ­nica y catÃ¡logo.
  2. `RECEPCIONISTA`: ConfirmaciÃ³n, recepciÃ³n de pacientes y cambio de estados.
  3. `PACIENTE`: Agendamiento y consulta de citas mÃ©dicas propias.

### Evidencia 03: Registro de AplicaciÃ³n Frontend (`vidasalud-frontend`)
- **Tipo de Plataforma**: SPA (Single Page Application).
- **URI de RedirecciÃ³n**: `http://localhost:4200`
- **Permisos de API asignados**:
  - `vidasalud-api` âž” `access_as_user` (Permiso delegado).
  - Estado: *Consentimiento concedido*.

---

## 3. Evidencias de ConfiguraciÃ³n en AWS

### Evidencia 04: ConfiguraciÃ³n de AWS API Gateway (Capa de PerÃ­metro)
- **Tipo de API**: HTTP API
- **Nombre**: `vidasalud-api-gateway`
- **CORS Configuration**:
  - `Access-Control-Allow-Origin`: `http://localhost:4200`
  - `Access-Control-Allow-Methods`: `GET, POST, PUT, DELETE, OPTIONS`
  - `Access-Control-Allow-Headers`: `Authorization, Content-Type`
- **Authorizer JWT**:
  - *Name*: `EntraID-JWT-Authorizer`
  - *Identity source*: `$request.header.Authorization`
  - *Issuer URL*: `https://login.microsoftonline.com/{TENANT_ID}/v2.0`
  - *Audience*: `api://vidasalud-api`
- **Rutas configuradas**:
  - `ANY /api/appointments/{proxy+}` âž” IntegraciÃ³n HTTP con EC2 (Puerto 8080)
  - `ANY /api/catalog/{proxy+}` âž” IntegraciÃ³n HTTP con EC2 (Puerto 8080)

### Evidencia 05: Despliegue en AWS EC2 con Contenedores Docker
- **Instancia**: `t3.small` / Ubuntu 22.04 LTS
- **Security Group**:
  - Inbound Rule: Puerto 8080 abierto exclusivamente al trÃ¡fico del API Gateway.
  - Inbound Rule: Puerto 22 (SSH para mantenimiento con par de claves).
- **OrquestaciÃ³n**: Docker Compose con aislamiento de red interna `bridge`.

---

## 4. Matriz de Pruebas de Seguridad y Resultados Obtenidos

| Caso de Prueba | MÃ©todo y Endpoint | Token / Rol | CÃ³digo HTTP Obtenido | Resultado y ValidaciÃ³n |
|---|---|---|:---:|---|
| **PeticiÃ³n sin autenticaciÃ³n** | `GET /api/appointments` | Ninguno (Sin Header) | **401 Unauthorized** | Bloqueado exitosamente por el interceptor de seguridad. |
| **PeticiÃ³n con token expirado/falso** | `GET /api/appointments` | Token malformado | **401 Unauthorized** | Rechazado por validaciÃ³n criptogrÃ¡fica de firma JWKS. |
| **Acceso de Paciente a cambio admin** | `PUT /api/appointments/1/status` | Token Rol `PACIENTE` | **403 Forbidden** | Acceso denegado: rol insuficiente para cambiar estado. |
| **Acceso de Recepcionista a transiciÃ³n** | `PUT /api/appointments/1/status` | Token Rol `RECEPCIONISTA` | **200 OK** | TransiciÃ³n vÃ¡lida ejecutada exitosamente. |
| **Consulta autorizada** | `GET /api/catalog/services` | Token Rol `ADMIN` | **200 OK** | Responde listado JSON de prestaciones mÃ©dicas. |

---

## 5. Repositorios y CÃ³digo Fuente Entregado

Cada componente fue inicializado con repositorio Git independiente, `.gitignore` especÃ­fico y `README.md` explicativo:
1. `frontend-vidasalud` (Angular 19 + MSAL)
2. `ms-vidasalud-bff` (Spring Boot + Spring Security)
3. `ms-vidasalud-appointments` (Spring Boot + JPA)
4. `ms-vidasalud-catalog` (Spring Boot + JPA)
5. `infra` (Docker Compose y configuraciÃ³n Cloud)



