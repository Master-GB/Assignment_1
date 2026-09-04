import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, EmployeeCreateRequest, EmployeeSearchParams } from '../models/employee.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private readonly http = inject(HttpClient);
  private readonly base = 'http://localhost:8082/api/employees';

  getAll(params: EmployeeSearchParams): Observable<Page<Employee>> {
    let httpParams = new HttpParams()
      .set('page', String(params.page ?? 0))
      .set('size', String(params.size ?? 10))
      .set('sort', 'firstName,asc');

    // Build search string from code / nic / name (backend uses a single 'search' param)
    const searchParts: string[] = [];
    if (params.employeeCode?.trim()) searchParts.push(params.employeeCode.trim());
    if (params.nic?.trim()) searchParts.push(params.nic.trim());
    if (params.name?.trim()) searchParts.push(params.name.trim());
    if (searchParts.length) {
      httpParams = httpParams.set('search', searchParts.join(' '));
    }
    if (params.status && params.status !== 'ALL') {
      httpParams = httpParams.set('status', params.status);
    }

    return this.http.get<Page<Employee>>(this.base, { params: httpParams });
  }

  getById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.base}/${id}`);
  }

  create(data: EmployeeCreateRequest, profileImage?: File): Observable<Employee> {
    const formData = this.buildFormData(data, profileImage);
    return this.http.post<Employee>(this.base, formData);
  }

  update(id: number, data: EmployeeCreateRequest, profileImage?: File): Observable<Employee> {
    const formData = this.buildFormData(data, profileImage);
    return this.http.put<Employee>(`${this.base}/${id}`, formData);
  }

  private buildFormData(data: EmployeeCreateRequest, profileImage?: File): FormData {
    const formData = new FormData();
    formData.append('employee', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    if (profileImage) {
      formData.append('profileImage', profileImage);
    }
    return formData;
  }
}
