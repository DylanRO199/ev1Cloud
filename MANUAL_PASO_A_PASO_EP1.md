# Manual de Implementación y Ejecución: VidaSalud EP1 (DSY1107)

Este documento detalla el paso a paso completo para comprender, configurar y ejecutar la **Evaluación Parcial N° 1** de la plataforma **VidaSalud**, cumpliendo con la pauta de evaluación Duoc UC (100% de los criterios).

---

## 1. Arquitectura y Componentes del Sistema

El flujo completo de comunicación y seguridad está estructurado de la siguiente forma:

```
[Usuario / Navegador]
       │
       ▼ (1) Autenticación OAuth2 / OIDC con MSAL
[Microsoft Entra ID (Azure AD)] ───► Emite Access Token JWT con roles/scopes
       │
       ▼ (2) Bearer <access_token>
[Frontend Angular 19] (Puerto 4200)
       │
       ▼ (3) Petición HTTP con Authorization: Bearer
[AWS API Gateway + JWT Authorizer] (Nube)  /  [BFF Directo en Local]
       │
       ▼ (4) Valida Issuer, Audience, Vigencia y Autorización por Rol
[ms-vidasalud-bff] (Spring Boot - Puerto 8080)
       ├──► (5a) /api/appointments ──► [ms-vidasalud-appointments] (Puerto 8081) ──► DB H2 / Oracle
       └──► (5b) /api/catalog      ──► [ms-vidasalud-catalog] (Puerto 8082)      ──► DB H2 / Oracle
```

### Componentes Entregados en el Repositorio:
1. **`frontend-vidasalud`**: Angular modular con componentes de Login, Dashboard y Atenciones, Guards de autenticación y de roles, e interceptor HTTP MSAL.
2. **`ms-vidasalud-bff`**: Backend for Frontend con Spring Security, validación de token JWT de Entra ID, autorización granular según roles (`ADMIN`, `RECEPCIONISTA`, `PACIENTE`) y reenvío a microservicios internos.
3. **`ms-vidasalud-appointments`**: Microservicio de negocio de atenciones médicas. Maneja el ciclo de vida de los estados (`SOLICITADA` ➔ `CONFIRMADA` ➔ `EN_ESPERA` ➔ `EN_ATENCION` ➔ `CERRADA` / `CANCELADA`).
4. **`ms-vidasalud-catalog`**: Microservicio para catálogo de prestaciones médicas y boxes de atención con cupos.
5. **`infra`**: Archivos Dockerfile y docker-compose listos para despliegue en contenedor/EC2.

---

## 2. Configuración en Microsoft Entra ID (Azure Portal)

Para vincular la autenticación con tu cuenta institucional o Azure for Students:

### Paso 1: Crear Tenant (Opcional si ya tienes uno)
* Entra al portal de Azure (`portal.azure.com`).
* Ve a **Microsoft Entra ID**.
* Crea o utiliza el inquilino (Tenant) para VidaSalud. Anota el **Tenant ID**.

### Paso 2: Registrar el Backend (`vidasalud-api`)
1. Ve a **Registros de aplicaciones** > **Nuevo registro**.
2. Nombre: `vidasalud-api`.
3. Tipos de cuenta: *Solo las cuentas de este directorio organizativo (inquilino único)*.
4. Clic en **Registrar**. Anota el **Application (client) ID**.
5. Ve a **Exponer una API**:
   - Clic en **Agregar URI de ID de aplicación**: Deja el valor por defecto (`api://<client-id>`).
   - Clic en **Agregar un ámbito (Scope)**:
     - Nombre del ámbito: `access_as_user`
     - ¿Quién puede dar el consentimiento?: *Administradores y usuarios*
     - Nombre para mostrar y descripción: `Acceso a VidaSalud API`
     - Estado: **Habilitado**. Guardar.
6. Ve a **Roles de aplicación** > **Crear rol de aplicación**:
   - Crear roles: `ADMIN`, `RECEPCIONISTA`, `PACIENTE`.
   - Tipo de miembros permitidos: *Usuarios o grupos*.

### Paso 3: Registrar el Frontend (`vidasalud-frontend`)
1. Ve a **Registros de aplicaciones** > **Nuevo registro**.
2. Nombre: `vidasalud-frontend`.
3. URI de redirección: Selecciona plataforma **SPA (Aplicación de página única)** y coloca `http://localhost:4200`.
4. Clic en **Registrar**. Anota el **Application (client) ID**.
5. Ve a **Permisos de API**:
   - Clic en **Agregar un permiso** > **Mis API** > Selecciona `vidasalud-api`.
   - Selecciona el permiso delegado: `access_as_user`.
   - Guarda y, si eres administrador, pulsa **Conceder consentimiento de administrador**.

### Paso 4: Asignar Roles a tus Usuarios de Prueba
1. Ve a **Aplicaciones empresariales** en Entra ID.
2. Abre `vidasalud-api`.
3. Ve a **Usuarios y grupos** > **Agregar usuario/grupo**.
4. Asigna un usuario de prueba con el rol `ADMIN` o `RECEPCIONISTA`.

---

## 3. Configuración de Variables en el Código

Una vez obtenidos tus IDs de Azure Entra ID:

### Frontend (`frontend-vidasalud/src/environments/environment.ts`):
```typescript
export const environment = {
  production: false,
  entra: {
    clientId: '<ID_CLIENTE_FRONTEND>',
    tenantId: '<ID_TENANT>',
    redirectUri: 'http://localhost:4200',
    postLogoutRedirectUri: 'http://localhost:4200/login'
  },
  apiBaseUrl: 'http://localhost:8080',
  apiScope: 'api://<ID_CLIENTE_API>/access_as_user'
};
```

### BFF (`ms-vidasalud-bff/src/main/resources/application.yml` o vía variables de entorno):
* `ENTRA_TENANT_ID`: `<ID_TENANT>`
* `ENTRA_API_CLIENT_ID`: `<ID_CLIENTE_API>`

---

## 4. Instalación de Java / Maven (Prerrequisito Backend)

Tu computador cuenta con **Node.js v26.4.0**, **npm 11.17.0** y **Git 2.55**, pero requiere Java para compilar Spring Boot:

1. **Instalar JDK 17 o 21**:
   Puedes instalarlo rápidamente abriendo PowerShell como Administrador y ejecutando:
   ```powershell
   winget install EclipseAdoptium.Temurin.17.JDK
   # o
   winget install Amazon.Corretto.17
   ```
2. Reiniciar la terminal para que reconozca el comando `java` y `javac`.

---

## 5. Puesta en Marcha Local

### Levantar los Microservicios Spring Boot:
Abre una terminal para cada uno (o usa Visual Studio Code):

1. **Catálogo**:
   ```bash
   cd ms-vidasalud-catalog
   ./mvnw spring-boot:run
   ```
   *(Disponible en: http://localhost:8082)*

2. **Atenciones (Appointments)**:
   ```bash
   cd ms-vidasalud-appointments
   ./mvnw spring-boot:run
   ```
   *(Disponible en: http://localhost:8081)*

3. **BFF (Backend for Frontend con Seguridad)**:
   ```bash
   cd ms-vidasalud-bff
   ./mvnw spring-boot:run
   ```
   *(Disponible en: http://localhost:8080)*

### Levantar el Frontend Angular:
```bash
cd frontend-vidasalud
npm start
```
*(Se abrirá automáticamente en: http://localhost:4200)*

---

## 6. Pruebas de la Pauta de Evaluación (Criterios del Profesor)

| Criterio | Acción a Realizar | Resultado Esperado |
|---|---|---|
| **Login MSAL** | Clic en "Iniciar sesión con Microsoft" | Redirige al login de Microsoft, ingresas credenciales y vuelve al Dashboard con tu nombre y foto. |
| **Token en Header** | Abrir la pestaña *Network (Red)* en F12 del navegador al cargar o consultar citas | La petición HTTP al BFF lleva el encabezado `Authorization: Bearer eyJ...` |
| **Protección por Roles** | Iniciar sesión como `PACIENTE` e intentar cancelar o cambiar estado administrativo | El BFF responde con código HTTP `403 Forbidden` y el frontend bloquea la acción. |
| **Cierre de Sesión** | Clic en "Cerrar sesión" | Limpia la sesión en Entra ID y redirige a la pantalla pública de `/login`. |
| **Petición sin Token** | Enviar `GET http://localhost:8080/api/appointments` desde Postman sin Bearer | Retorna HTTP `401 Unauthorized`. |
