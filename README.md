# VidaSalud - Evaluación Parcial 1

Solución base para la plataforma VidaSalud.

## Componentes

- `frontend-vidasalud`: Angular + MSAL.
- `ms-vidasalud-bff`: Spring Boot + Spring Security, validación JWT y autorización por rol.
- `ms-vidasalud-appointments`: gestión de atenciones y estados.
- `ms-vidasalud-catalog`: prestaciones, boxes y cupos.

## Flujo

Microsoft Entra ID → Angular + MSAL → AWS API Gateway → BFF → microservicios de dominio.

## Ejecución local

1. Levantar `ms-vidasalud-catalog` en `8082`.
2. Levantar `ms-vidasalud-appointments` en `8081`.
3. Levantar `ms-vidasalud-bff` en `8080`.
4. Configurar los datos de Microsoft Entra ID en `frontend-vidasalud/src/environments/environment.ts`.
5. Ejecutar el frontend con `npm install` y `npm start`.

Los servicios de dominio usan H2 en el perfil `local` y tienen un perfil `cloud` preparado para Oracle mediante variables de entorno.
