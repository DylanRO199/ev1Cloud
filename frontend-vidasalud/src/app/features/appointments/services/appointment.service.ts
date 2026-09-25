import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Appointment, AppointmentCreateRequest, AppointmentStatus, StatusUpdateRequest } from '../models/appointment.model';

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/appointments`;

  constructor(private readonly http: HttpClient) {}

  list(status?: AppointmentStatus | ''): Observable<Appointment[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    return this.http.get<Appointment[]>(this.baseUrl, { params });
  }

  create(payload: AppointmentCreateRequest): Observable<Appointment> {
    return this.http.post<Appointment>(this.baseUrl, payload);
  }

  updateStatus(id: number, payload: StatusUpdateRequest): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.baseUrl}/${id}/status`, payload);
  }
}
