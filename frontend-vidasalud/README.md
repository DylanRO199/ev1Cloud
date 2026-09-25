# frontend-vidasalud

Proyecto Frontend para el sistema de gestión médica **VidaSalud**, desarrollado como parte de la Evaluación Parcial 1 de la asignatura **Desarrollo Cloud Native I (DSY1107)** - Duoc UC.

## 📌 Requisitos Previos

- **Node.js**: v18 o superior (probado en v20 / v26)
- **NPM**: v9 o superior
- **Angular CLI**: v19

## 🚀 Tecnologías Utilizadas

- **Angular 19** (Standalone Components)
- **@azure/msal-browser** y **@azure/msal-angular** (Autenticación OAuth 2.0 / OIDC con Microsoft Entra ID)
- **RxJS** para manejo de flujos asíncronos y estado reactivo
- **HTML5 / CSS3** responsivo sin dependencias de librerías CSS pesadas

## ⚙️ Configuración del Entorno

Antes de iniciar la aplicación en desarrollo, se debe configurar el archivo de entorno en `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  entra: {
    clientId: 'TU_CLIENT_ID_FRONTEND',          // ID de aplicación registrado en Entra ID
    tenantId: 'TU_TENANT_ID',                  // ID del inquilino (Directorio)
    redirectUri: 'http://localhost:4200',
    postLogoutRedirectUri: 'http://localhost:4200/login'
  },
  apiBaseUrl: 'http://localhost:8080',         // Endpoint del BFF o AWS API Gateway
  apiScope: 'api://TU_CLIENT_ID_API/access_as_user' // Scope expuesto por el backend
};
```

## 💻 Instalación y Ejecución

1. **Instalar dependencias del proyecto:**
   ```bash
   npm install
   ```

2. **Levantar servidor local de desarrollo:**
   ```bash
   npm start
   ```
   La aplicación se abrirá automáticamente en `http://localhost:4200/`.

3. **Compilar para producción:**
   ```bash
   npm run build
   ```

## 🔒 Estructura y Seguridad

- **`core/auth`**:
  - `auth.service.ts`: Gestión de sesión activa con MSAL, obtención silenciosa de tokens (`acquireTokenSilent`) y decodificación de roles (`ADMIN`, `RECEPCIONISTA`, `PACIENTE`).
  - `role.guard.ts`: Control de acceso a rutas protegidas basado en roles del token JWT.
- **`features`**:
  - `login`: Vista pública de autenticación con botón institucional Microsoft.
  - `dashboard`: Resumen de métricas de atenciones, perfil de usuario activo y accesos rápidos.
  - `appointments`: Módulo funcional de gestión de atenciones con listado filtrable por estados y formulario reactivo para nuevas atenciones.
