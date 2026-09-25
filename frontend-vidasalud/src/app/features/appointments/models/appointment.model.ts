export type AppointmentStatus =
  | 'SOLICITADA'
  | 'CONFIRMADA'
  | 'EN_ESPERA'
  | 'EN_ATENCION'
  | 'CERRADA'
  | 'CANCELADA';

export interface Appointment {
  id: number;
  patientId: string;
  patientName: string;
  patientEmail: string;
  serviceId: number;
  boxId: number | null;
  scheduledAt: string;
  status: AppointmentStatus;
  createdAt: string;
  updatedAt: string;
}

export interface AppointmentCreateRequest {
  patientId?: string;
  patientName: string;
  patientEmail: string;
  serviceId: number;
  scheduledAt: string;
}

export interface StatusUpdateRequest {
  status: AppointmentStatus;
  boxId?: number | null;
}
