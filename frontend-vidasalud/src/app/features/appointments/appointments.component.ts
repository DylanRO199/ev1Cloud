import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth/auth.service';
import { Appointment, AppointmentStatus } from './models/appointment.model';
import { AppointmentService } from './services/appointment.service';
import { CatalogService } from '../catalog/services/catalog.service';
import { Box, MedicalService } from '../catalog/models/catalog.model';

@Component({
  selector: 'app-appointments',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <main class="page">
      <div class="container">
        <div class="page-header">
          <div>
            <h1>Atenciones</h1>
            <p>Gestión de solicitudes y estados de atención.</p>
          </div>
          <button class="btn btn-primary" type="button" (click)="showForm = !showForm">{{ showForm ? 'Cerrar formulario' : 'Nueva atención' }}</button>
        </div>

        @if (errorMessage) { <div class="alert alert-error">{{ errorMessage }}</div> }
        @if (successMessage) { <div class="alert alert-success">{{ successMessage }}</div> }

        @if (showForm) {
          <section class="card form-card">
            <h2>Nueva atención</h2>
            <form [formGroup]="form" (ngSubmit)="createAppointment()">
              <div class="form-grid">
                <div class="form-field"><label>Nombre del paciente</label><input class="form-control" formControlName="patientName"></div>
                <div class="form-field"><label>Correo</label><input class="form-control" type="email" formControlName="patientEmail"></div>
                <div class="form-field">
                  <label>Prestación</label>
                  <select class="form-control" formControlName="serviceId">
                    <option value="">Seleccione</option>
                    @for (service of services; track service.id) { <option [value]="service.id">{{ service.name }}</option> }
                  </select>
                </div>
                <div class="form-field"><label>Fecha y hora</label><input class="form-control" type="datetime-local" formControlName="scheduledAt"></div>
              </div>
              <div class="form-actions">
                <button class="btn btn-primary" type="submit" [disabled]="form.invalid || saving">{{ saving ? 'Guardando...' : 'Crear atención' }}</button>
              </div>
            </form>
          </section>
        }

        <section class="card list-card">
          <div class="toolbar">
            <div>
              <strong>Listado</strong>
              <span>{{ appointments.length }} registro(s)</span>
            </div>
            <select class="form-control filter" [value]="selectedStatus" (change)="onStatusFilterChange($event)">
              <option value="">Todos los estados</option>
              @for (status of statuses; track status) { <option [value]="status">{{ status }}</option> }
            </select>
          </div>

          <div class="table-wrap">
            <table>
              <thead><tr><th>ID</th><th>Paciente</th><th>Prestación</th><th>Fecha</th><th>Estado</th><th>Acciones</th></tr></thead>
              <tbody>
                @for (item of appointments; track item.id) {
                  <tr>
                    <td>#{{ item.id }}</td>
                    <td><strong>{{ item.patientName }}</strong><br><small>{{ item.patientEmail }}</small></td>
                    <td>{{ serviceName(item.serviceId) }}</td>
                    <td>{{ formatDate(item.scheduledAt) }}</td>
                    <td><span class="badge">{{ item.status }}</span></td>
                    <td>
                      @if (canManageStatuses) {
                        <div class="actions">
                          <select class="mini-select" #statusSelect>
                            @for (status of nextStatuses(item.status); track status) { <option [value]="status">{{ status }}</option> }
                          </select>
                          @if (statusSelect.value === 'CONFIRMADA') {
                            <select class="mini-select" #boxSelect>
                              <option value="">Box</option>
                              @for (box of availableBoxes(item.serviceId); track box.id) { <option [value]="box.id">{{ box.name }} ({{ box.availableSlots }})</option> }
                            </select>
                            <button class="btn btn-secondary" type="button" (click)="changeStatus(item, statusSelect.value, boxSelect.value)">Aplicar</button>
                          } @else {
                            <button class="btn btn-secondary" type="button" [disabled]="nextStatuses(item.status).length === 0" (click)="changeStatus(item, statusSelect.value)">Aplicar</button>
                          }
                        </div>
                      } @else {
                        <span class="muted">Solo lectura</span>
                      }
                    </td>
                  </tr>
                } @empty {
                  <tr><td colspan="6">No hay atenciones registradas.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </main>
  `,
  styles: [`
    .form-card { padding:22px; margin-bottom:22px; }
    .form-card h2 { margin:0 0 18px; font-size:18px; }
    .form-grid { display:grid; grid-template-columns:repeat(2,1fr); gap:16px; }
    .form-actions { display:flex; justify-content:flex-end; margin-top:18px; }
    .list-card { overflow:hidden; }
    .toolbar { display:flex; align-items:center; justify-content:space-between; gap:14px; padding:18px 20px; border-bottom:1px solid #e6edf5; }
    .toolbar > div { display:grid; gap:3px; }
    .toolbar span { color:#94a3b8; font-size:12px; }
    .filter { width:210px; }
    small,.muted { color:#94a3b8; }
    .actions { display:flex; align-items:center; flex-wrap:wrap; gap:7px; }
    .mini-select { max-width:150px; border:1px solid #cbd8e6; border-radius:8px; padding:7px; background:white; color:#38546d; }
    @media(max-width:760px){ .form-grid { grid-template-columns:1fr; } .toolbar { align-items:flex-start; flex-direction:column; } .filter { width:100%; } }
  `]
})
export class AppointmentsComponent implements OnInit {
  appointments: Appointment[] = [];
  services: MedicalService[] = [];
  boxes: Box[] = [];
  roles: string[] = [];
  showForm = false;
  saving = false;
  errorMessage = '';
  successMessage = '';
  selectedStatus: AppointmentStatus | '' = '';
  readonly statuses: AppointmentStatus[] = ['SOLICITADA', 'CONFIRMADA', 'EN_ESPERA', 'EN_ATENCION', 'CERRADA', 'CANCELADA'];

  readonly form = this.fb.nonNullable.group({
    patientName: ['', [Validators.required, Validators.maxLength(120)]],
    patientEmail: ['', [Validators.required, Validators.email]],
    serviceId: ['', Validators.required],
    scheduledAt: ['', Validators.required]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly appointmentsService: AppointmentService,
    private readonly catalogService: CatalogService
  ) {}

  ngOnInit(): void {
    void this.loadRoles();
    this.loadCatalog();
    this.loadAppointments();
  }

  get canManageStatuses(): boolean {
    return this.roles.includes('ADMIN') || this.roles.includes('RECEPCIONISTA');
  }

  onStatusFilterChange(event: Event): void {
    this.selectedStatus = (event.target as HTMLSelectElement).value as AppointmentStatus | '';
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.errorMessage = '';
    this.appointmentsService.list(this.selectedStatus).subscribe({
      next: items => this.appointments = items,
      error: () => this.errorMessage = 'No fue posible cargar las atenciones.'
    });
  }

  createAppointment(): void {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    this.saving = true;
    this.clearMessages();

    this.appointmentsService.create({
      patientName: value.patientName,
      patientEmail: value.patientEmail,
      serviceId: Number(value.serviceId),
      scheduledAt: new Date(value.scheduledAt).toISOString()
    }).subscribe({
      next: () => {
        this.successMessage = 'Atención creada correctamente.';
        this.form.reset();
        this.showForm = false;
        this.loadAppointments();
      },
      error: error => this.errorMessage = error?.error?.message ?? 'No fue posible crear la atención.',
      complete: () => this.saving = false
    });
  }

  changeStatus(item: Appointment, statusValue: string, boxValue?: string): void {
    if (!statusValue) return;
    this.clearMessages();
    this.appointmentsService.updateStatus(item.id, {
      status: statusValue as AppointmentStatus,
      boxId: boxValue ? Number(boxValue) : undefined
    }).subscribe({
      next: () => {
        this.successMessage = `La atención #${item.id} fue actualizada.`;
        this.loadAppointments();
        this.loadCatalog();
      },
      error: error => this.errorMessage = error?.error?.message ?? 'No fue posible actualizar el estado.'
    });
  }

  nextStatuses(current: AppointmentStatus): AppointmentStatus[] {
    const transitions: Record<AppointmentStatus, AppointmentStatus[]> = {
      SOLICITADA: ['CONFIRMADA', 'CANCELADA'],
      CONFIRMADA: ['EN_ESPERA', 'CANCELADA'],
      EN_ESPERA: ['EN_ATENCION', 'CANCELADA'],
      EN_ATENCION: ['CERRADA'],
      CERRADA: [],
      CANCELADA: []
    };
    return transitions[current];
  }

  serviceName(serviceId: number): string {
    return this.services.find(item => item.id === serviceId)?.name ?? `#${serviceId}`;
  }

  availableBoxes(serviceId: number): Box[] {
    return this.boxes.filter(box => box.serviceId === serviceId && box.active && box.availableSlots > 0);
  }

  formatDate(value: string): string {
    return new Date(value).toLocaleString('es-CL');
  }

  private loadCatalog(): void {
    this.catalogService.listServices().subscribe({
      next: items => this.services = items
    });
    this.catalogService.listBoxes().subscribe({
      next: items => this.boxes = items,
      error: () => this.boxes = []
    });
  }

  private async loadRoles(): Promise<void> {
    this.roles = await this.auth.getRoles().catch(() => []);
    if (this.canManageStatuses && this.boxes.length === 0) {
      this.catalogService.listBoxes().subscribe({ next: items => this.boxes = items });
    }
  }

  private clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
