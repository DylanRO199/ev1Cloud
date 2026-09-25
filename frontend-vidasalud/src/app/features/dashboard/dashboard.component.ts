import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { Appointment } from '../appointments/models/appointment.model';
import { AppointmentService } from '../appointments/services/appointment.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <main class="page">
      <div class="container">
        <div class="page-header">
          <div>
            <h1>Dashboard</h1>
            <p>Resumen operativo de la jornada.</p>
          </div>
          <a class="btn btn-primary" routerLink="/appointments">Ver atenciones</a>
        </div>

        @if (errorMessage) {
          <div class="alert alert-error">{{ errorMessage }}</div>
        }

        <section class="welcome card">
          <div>
            <span class="welcome-label">Sesión activa</span>
            <h2>{{ auth.displayName }}</h2>
            <p>{{ rolesText }}</p>
          </div>
          <div class="shield">✓</div>
        </section>

        <section class="stats">
          <article class="stat card"><span>Total</span><strong>{{ appointments.length }}</strong></article>
          <article class="stat card"><span>Solicitadas</span><strong>{{ count('SOLICITADA') }}</strong></article>
          <article class="stat card"><span>En espera</span><strong>{{ count('EN_ESPERA') }}</strong></article>
          <article class="stat card"><span>En atención</span><strong>{{ count('EN_ATENCION') }}</strong></article>
        </section>

        <section class="card recent">
          <div class="section-title">
            <h2>Atenciones recientes</h2>
            <span>{{ loading ? 'Cargando...' : 'Actualizado' }}</span>
          </div>
          <div class="table-wrap">
            <table>
              <thead><tr><th>Paciente</th><th>Fecha</th><th>Estado</th></tr></thead>
              <tbody>
                @for (item of appointments.slice(0, 5); track item.id) {
                  <tr>
                    <td>{{ item.patientName }}</td>
                    <td>{{ formatDate(item.scheduledAt) }}</td>
                    <td><span class="badge">{{ item.status }}</span></td>
                  </tr>
                } @empty {
                  <tr><td colspan="3">No hay atenciones para mostrar.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </main>
  `,
  styles: [`
    .welcome { padding:24px 26px; display:flex; align-items:center; justify-content:space-between; margin-bottom:22px; background:linear-gradient(120deg,#ffffff,#edf7ff); }
    .welcome-label { color:#0d6efd; font-weight:800; font-size:12px; text-transform:uppercase; letter-spacing:.08em; }
    .welcome h2 { margin:7px 0 5px; font-size:24px; }
    .welcome p { margin:0; color:#64748b; }
    .shield { display:grid; place-items:center; width:54px; height:54px; border-radius:16px; background:#dcfce7; color:#15803d; font-size:26px; font-weight:900; }
    .stats { display:grid; grid-template-columns:repeat(4,1fr); gap:16px; margin-bottom:22px; }
    .stat { padding:20px; display:grid; gap:8px; }
    .stat span { color:#64748b; font-size:13px; font-weight:700; }
    .stat strong { font-size:32px; color:#17324d; }
    .recent { padding:6px 0 0; overflow:hidden; }
    .section-title { display:flex; align-items:center; justify-content:space-between; padding:18px 20px 10px; }
    .section-title h2 { margin:0; font-size:18px; }
    .section-title span { color:#94a3b8; font-size:12px; }
    @media (max-width:800px){ .stats { grid-template-columns:repeat(2,1fr); } }
  `]
})
export class DashboardComponent implements OnInit {
  appointments: Appointment[] = [];
  rolesText = 'Usuario autenticado';
  loading = true;
  errorMessage = '';

  constructor(public readonly auth: AuthService, private readonly appointmentsService: AppointmentService) {}

  ngOnInit(): void {
    void this.loadRoles();
    this.appointmentsService.list().subscribe({
      next: items => this.appointments = items,
      error: () => this.errorMessage = 'No fue posible cargar el resumen de atenciones.',
      complete: () => this.loading = false
    });
  }

  count(status: string): number {
    return this.appointments.filter(item => item.status === status).length;
  }

  formatDate(value: string): string {
    return new Date(value).toLocaleString('es-CL');
  }

  private async loadRoles(): Promise<void> {
    const roles = await this.auth.getRoles().catch(() => []);
    this.rolesText = roles.length ? roles.join(' · ') : 'Usuario autenticado';
  }
}
