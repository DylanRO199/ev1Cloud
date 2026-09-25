import { ApplicationConfig } from '@angular/core';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import {
  InteractionType,
  IPublicClientApplication,
  PublicClientApplication
} from '@azure/msal-browser';
import {
  MSAL_GUARD_CONFIG,
  MSAL_INSTANCE,
  MSAL_INTERCEPTOR_CONFIG,
  MsalBroadcastService,
  MsalGuard,
  MsalGuardConfiguration,
  MsalInterceptor,
  MsalInterceptorConfiguration,
  MsalService
} from '@azure/msal-angular';
import { routes } from './app.routes';
import { environment } from '../environments/environment';

export function msalInstanceFactory(): IPublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: environment.entra.clientId,
      authority: `https://login.microsoftonline.com/${environment.entra.tenantId}`,
      redirectUri: environment.entra.redirectUri,
      postLogoutRedirectUri: environment.entra.postLogoutRedirectUri
    },
    cache: {
      cacheLocation: 'localStorage'
    }
  });
}

export function msalGuardConfigFactory(): MsalGuardConfiguration {
  return {
    interactionType: InteractionType.Popup,
    authRequest: { scopes: [environment.apiScope] }
  };
}

export function msalInterceptorConfigFactory(): MsalInterceptorConfiguration {
  const protectedResourceMap = new Map<string, string[]>();
  protectedResourceMap.set(environment.apiBaseUrl, [environment.apiScope]);
  protectedResourceMap.set(`${environment.apiBaseUrl}/*`, [environment.apiScope]);

  return {
    interactionType: InteractionType.Popup,
    protectedResourceMap
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    { provide: MSAL_INSTANCE, useFactory: msalInstanceFactory },
    { provide: MSAL_GUARD_CONFIG, useFactory: msalGuardConfigFactory },
    { provide: MSAL_INTERCEPTOR_CONFIG, useFactory: msalInterceptorConfigFactory },
    { provide: HTTP_INTERCEPTORS, useClass: MsalInterceptor, multi: true },
    MsalService,
    MsalGuard,
    MsalBroadcastService
  ]
};
