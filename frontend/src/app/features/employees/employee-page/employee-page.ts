import { Component, inject, OnInit, signal } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { Employee, EmployeeSearchParams } from '../../../core/models/employee.model';
import { Page } from '../../../core/models/page.model';
import { EmployeeSearch } from '../employee-search/employee-search';
import { EmployeeTable } from '../employee-table/employee-table';
import { EmployeeFormDialog } from '../employee-form-dialog/employee-form-dialog';
import { EmployeeViewDialog } from '../employee-view-dialog/employee-view-dialog';

@Component({
  selector: 'app-employee-page',
  standalone: true,
  imports: [EmployeeSearch, EmployeeTable, EmployeeFormDialog, EmployeeViewDialog],
  templateUrl: './employee-page.html',
})
export class EmployeePage implements OnInit {
  private readonly empService = inject(EmployeeService);

  pageData = signal<Page<Employee> | null>(null);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  formDialogVisible = signal(false);
  viewDialogVisible = signal(false);

  selectedEmployee = signal<Employee | null>(null);   // for edit / view

  private currentParams: EmployeeSearchParams = { page: 0, size: 10 };

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.empService.getAll(this.currentParams).subscribe({
      next: (data) => { this.pageData.set(data); this.loading.set(false); },
      error: () => { this.errorMessage.set('Failed to load employees.'); this.loading.set(false); },
    });
  }

  onSearch(params: EmployeeSearchParams): void {
    this.currentParams = { ...params, page: 0, size: 10 };
    this.loadEmployees();
  }

  onReset(): void {
    this.currentParams = { page: 0, size: 10 };
    this.loadEmployees();
  }

  onPageChange(page: number): void {
    this.currentParams = { ...this.currentParams, page };
    this.loadEmployees();
  }

  openNewForm(): void {
    this.selectedEmployee.set(null);
    this.formDialogVisible.set(true);
  }

  openEditForm(emp: Employee): void {
    this.selectedEmployee.set(emp);
    this.formDialogVisible.set(true);
  }

  openView(emp: Employee): void {
    this.selectedEmployee.set(emp);
    this.viewDialogVisible.set(true);
  }

  onSaved(): void {
    this.formDialogVisible.set(false);
    this.loadEmployees();
  }
}
