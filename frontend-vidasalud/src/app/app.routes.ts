import { Routes } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';
import { LoginComponent } from './features/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { AppointmentsComponent } from './features/appointments/appointments.component';
import { roleGuard } from './core/auth/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [MsalGuard] },
  {
    path: 'appointments',
    component: AppointmentsComponent,
    canActivate: [MsalGuard, roleGuard],
    data: { roles: ['ADMIN', 'RECEPCIONISTA', 'PACIENTE'] }
  },
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: '**', redirectTo: 'dashboard' }
];
