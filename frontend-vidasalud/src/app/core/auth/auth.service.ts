import { Injectable } from '@angular/core';
import { MsalService } from '@azure/msal-angular';
import { AccountInfo, AuthenticationResult } from '@azure/msal-browser';
import { from, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { decodeJwt } from './token.util';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private readonly msal: MsalService) {}

  login(): Observable<AuthenticationResult> {
    return from(this.msal.loginPopup({ scopes: [environment.apiScope] })).pipe(
      tap(result => this.msal.instance.setActiveAccount(result.account))
    );
  }

  logout(): void {
    this.msal.logoutPopup({
      account: this.activeAccount,
      mainWindowRedirectUri: environment.entra.postLogoutRedirectUri
    }).subscribe();
  }

  get isAuthenticated(): boolean {
    return !!this.activeAccount;
  }

  get activeAccount(): AccountInfo | null {
    return this.msal.instance.getActiveAccount() ?? this.msal.instance.getAllAccounts()[0] ?? null;
  }

  get displayName(): string {
    return this.activeAccount?.name ?? this.activeAccount?.username ?? 'Usuario';
  }

  async getAccessToken(): Promise<string> {
    const account = this.activeAccount;
    if (!account) throw new Error('No hay una sesión activa.');

    const result = await this.msal.instance.acquireTokenSilent({
      account,
      scopes: [environment.apiScope]
    });

    return result.accessToken;
  }

  async getAccessTokenClaims(): Promise<Record<string, unknown>> {
    return decodeJwt(await this.getAccessToken());
  }

  async getRoles(): Promise<string[]> {
    const claims = await this.getAccessTokenClaims();
    const roles = claims['roles'];
    return Array.isArray(roles) ? roles.map(String) : [];
  }
}
