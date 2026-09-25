import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    @if (auth.isAuthenticated) {
      <header class="nav-shell">
        <div class="container nav-inner">
          <a class="brand" routerLink="/dashboard">
            <span class="brand-mark">+</span>
            <span>VidaSalud</span>
          </a>
          <nav>
            <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
            <a routerLink="/appointments" routerLinkActive="active">Atenciones</a>
          </nav>
          <div class="user-area">
            <span>{{ auth.displayName }}</span>
            <button class="logout" type="button" (click)="auth.logout()">Cerrar sesión</button>
          </div>
        </div>
      </header>
    }
  `,
  styles: [`
    .nav-shell { background:#fff; border-bottom:1px solid #dbe4ef; position:sticky; top:0; z-index:10; }
    .nav-inner { height:68px; display:flex; align-items:center; gap:30px; }
    .brand { display:flex; align-items:center; gap:10px; font-weight:800; color:#0f456d; font-size:19px; }
    .brand-mark { display:grid; place-items:center; width:30px; height:30px; border-radius:9px; background:#0d6efd; color:white; font-size:22px; }
    nav { display:flex; align-items:center; gap:8px; flex:1; }
    nav a { padding:9px 12px; border-radius:9px; color:#526b82; font-weight:650; }
    nav a.active { background:#edf5ff; color:#0d6efd; }
    .user-area { display:flex; align-items:center; gap:12px; color:#526b82; font-size:13px; }
    .logout { border:0; background:#edf2f7; color:#29455f; border-radius:9px; padding:8px 11px; font-weight:700; }
    @media (max-width:760px) { .user-area span { display:none; } nav a { padding:8px; } }
  `]
})
export class NavbarComponent {
  constructor(public readonly auth: AuthService) {}
}
