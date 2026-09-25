export interface MedicalService {
  id: number;
  name: string;
  description: string;
  price: number;
  active: boolean;
}

export interface Box {
  id: number;
  name: string;
  serviceId: number;
  availableSlots: number;
  active: boolean;
}
