import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EmployeeSearchParams, EmployeeStatus } from '../../../core/models/employee.model';

@Component({
  selector: 'app-employee-search',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './employee-search.html',
})
export class EmployeeSearch {
  @Output() search = new EventEmitter<EmployeeSearchParams>();
  @Output() reset = new EventEmitter<void>();

  employeeCode = '';
  nic = '';
  name = '';
  status: EmployeeStatus | 'ALL' = 'ALL';

  onSearch(): void {
    this.search.emit({
      employeeCode: this.employeeCode.trim(),
      nic: this.nic.trim(),
      name: this.name.trim(),
      status: this.status,
      page: 0,
      size: 10,
    });
  }

  onReset(): void {
    this.employeeCode = '';
    this.nic = '';
    this.name = '';
    this.status = 'ALL';
    this.reset.emit();
  }
}
