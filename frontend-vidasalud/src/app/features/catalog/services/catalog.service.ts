import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Box, MedicalService } from '../models/catalog.model';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/catalog`;

  constructor(private readonly http: HttpClient) {}

  listServices(): Observable<MedicalService[]> {
    return this.http.get<MedicalService[]>(`${this.baseUrl}/services`);
  }

  listBoxes(): Observable<Box[]> {
    return this.http.get<Box[]>(`${this.baseUrl}/boxes`);
  }
}
