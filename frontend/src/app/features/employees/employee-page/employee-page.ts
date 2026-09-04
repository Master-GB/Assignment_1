import { Component, inject, OnInit, signal } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { ToastService } from '../../../core/services/toast.service';
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
  private readonly toast = inject(ToastService);

  pageData = signal<Page<Employee> | null>(null);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  formDialogVisible = signal(false);
  viewDialogVisible = signal(false);
  deleteDialogVisible = signal(false);

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

  confirmDelete(emp: Employee): void {
    this.selectedEmployee.set(emp);
    this.deleteDialogVisible.set(true);
  }

  executeDelete(): void {
    const emp = this.selectedEmployee();
    if (!emp) return;
    
    this.loading.set(true);
    this.empService.delete(emp.id).subscribe({
      next: () => {
        this.deleteDialogVisible.set(false);
        this.toast.success('Employee deleted successfully');
        this.loadEmployees();
      },
      error: (err) => {
        const errorMessage = err.error?.message ?? 'Failed to delete employee.';
        this.errorMessage.set(errorMessage);
        this.toast.error(errorMessage);
        this.loading.set(false);
        this.deleteDialogVisible.set(false);
      }
    });
  }

  exportPDF(emp: Employee): void {
    this.empService.exportPDF(emp.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `employee_${emp.id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.toast.success('PDF exported successfully');
      },
      error: (err) => {
        const errorMessage = err.error?.message ?? 'Failed to export PDF';
        this.toast.error(errorMessage);
      }
    });
  }

  exportExcel(emp: Employee): void {
    this.empService.exportExcel(emp.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `employee_${emp.id}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
        this.toast.success('Excel exported successfully');
      },
      error: (err) => {
        const errorMessage = err.error?.message ?? 'Failed to export Excel';
        this.toast.error(errorMessage);
      }
    });
  }

  onSaved(): void {
    this.formDialogVisible.set(false);
    this.loadEmployees();
  }
}
