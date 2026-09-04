import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Designation } from '../models/designation.model';

@Injectable({ providedIn: 'root' })
export class DesignationService {
  private readonly http = inject(HttpClient);
  private readonly base = 'http://localhost:8082/api/designations';

  getAll(): Observable<Designation[]> {
    return this.http.get<Designation[]>(this.base);
  }
}
