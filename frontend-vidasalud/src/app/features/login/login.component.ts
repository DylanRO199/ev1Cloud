import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  template: `
    <main class="login-shell">
      <section class="login-card">
        <div class="logo">+</div>
        <p class="eyebrow">Red VidaSalud</p>
        <h1>Gestión de atenciones</h1>
        <p class="description">Accede con tu cuenta corporativa para administrar y consultar atenciones.</p>

        @if (errorMessage) {
          <div class="alert alert-error">{{ errorMessage }}</div>
        }

        <button class="microsoft-btn" type="button" [disabled]="loading" (click)="login()">
          <span class="ms-icon"><i></i><i></i><i></i><i></i></span>
          {{ loading ? 'Iniciando sesión...' : 'Iniciar sesión con Microsoft' }}
        </button>
        <p class="footer-text">Autenticación protegida con Microsoft Entra ID.</p>
      </section>
    </main>
  `,
  styles: [`
    .login-shell { min-height:100vh; display:grid; place-items:center; padding:24px; background:linear-gradient(140deg,#eaf4ff 0%,#f7fbff 48%,#eef9f5 100%); }
    .login-card { width:min(440px,100%); background:#fff; border:1px solid #dbe7f2; border-radius:22px; padding:42px; box-shadow:0 24px 70px rgba(21,61,94,.13); text-align:center; }
    .logo { width:58px; height:58px; margin:0 auto 18px; display:grid; place-items:center; background:#0d6efd; color:#fff; border-radius:16px; font-size:40px; font-weight:300; line-height:1; }
    .eyebrow { text-transform:uppercase; letter-spacing:.12em; font-weight:800; font-size:11px; color:#0d6efd; margin:0 0 8px; }
    h1 { margin:0; font-size:30px; color:#17324d; }
    .description { color:#64748b; line-height:1.6; margin:14px 0 28px; }
    .microsoft-btn { width:100%; display:flex; align-items:center; justify-content:center; gap:12px; border:1px solid #cbd8e6; background:#fff; color:#243f57; border-radius:11px; padding:13px 16px; font-weight:750; }
    .microsoft-btn:hover:not(:disabled) { background:#f8fbff; border-color:#9eb6cb; }
    .ms-icon { display:grid; grid-template-columns:repeat(2,8px); grid-template-rows:repeat(2,8px); gap:2px; }
    .ms-icon i:nth-child(1){background:#f25022}.ms-icon i:nth-child(2){background:#7fba00}.ms-icon i:nth-child(3){background:#00a4ef}.ms-icon i:nth-child(4){background:#ffb900}
    .footer-text { margin:22px 0 0; color:#94a3b8; font-size:12px; }
  `]
})
export class LoginComponent {
  loading = false;
  errorMessage = '';

  constructor(private readonly auth: AuthService, private readonly router: Router) {
    if (auth.isAuthenticated) {
      void router.navigate(['/dashboard']);
    }
  }

  login(): void {
    this.loading = true;
    this.errorMessage = '';

    this.auth.login().subscribe({
      next: () => void this.router.navigate(['/dashboard']),
      error: () => {
        this.loading = false;
        this.errorMessage = 'No fue posible iniciar sesión. Verifica la configuración de Microsoft Entra ID.';
      },
      complete: () => this.loading = false
    });
  }
}
