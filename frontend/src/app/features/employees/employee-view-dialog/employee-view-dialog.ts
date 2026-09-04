import { Component, EventEmitter, Input, Output, inject, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { Employee } from '../../../core/models/employee.model';
import { EmployeeService } from '../../../core/services/employee.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-employee-view-dialog',
  standalone: true,
  imports: [NgClass],
  templateUrl: './employee-view-dialog.html',
})
export class EmployeeViewDialog {
  private readonly empService = inject(EmployeeService);
  private readonly toast = inject(ToastService);

  @Input() visible = false;
  @Input() employee: Employee | null = null;
  @Output() closed = new EventEmitter<void>();

  exportingPdf = signal(false);
  exportingExcel = signal(false);
  exportingHtml = signal(false);

  close(): void { this.closed.emit(); }

  get fullName(): string {
    if (!this.employee) return '';
    return `${this.employee.firstName} ${this.employee.lastName}`;
  }

  exportPDF(): void {
    if (!this.employee) return;
    this.exportingPdf.set(true);
    const empCode = this.employee.employeeCode;
    this.empService.exportPDF(this.employee.id).subscribe({
      next: (blob) => {
        this.exportingPdf.set(false);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `employee_${empCode}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.toast.success('PDF exported successfully');
      },
      error: (err) => {
        this.exportingPdf.set(false);
        const errorMessage = err.error?.message ?? 'Failed to export PDF';
        this.toast.error(errorMessage);
      }
    });
  }

  exportExcel(): void {
    if (!this.employee) return;
    this.exportingExcel.set(true);
    const empCode = this.employee.employeeCode;
    this.empService.exportExcel(this.employee.id).subscribe({
      next: (blob) => {
        this.exportingExcel.set(false);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = `employee_${empCode}.xlsx`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.toast.success('Excel exported successfully');
      },
      error: (err) => {
        this.exportingExcel.set(false);
        const errorMessage = err.error?.message ?? 'Failed to export Excel';
        this.toast.error(errorMessage);
      }
    });
  }

  previewHTML(): void {
    if (!this.employee) return;
    this.exportingHtml.set(true);
    this.empService.exportHTML(this.employee.id).subscribe({
      next: (htmlContent) => {
        this.exportingHtml.set(false);
        const win = window.open('', '_blank');
        if (win) {
          win.document.write(htmlContent);
          win.document.close();
        }
        this.toast.success('HTML preview opened successfully');
      },
      error: (err) => {
        this.exportingHtml.set(false);
        const errorMessage = err.error?.message ?? 'Failed to generate HTML preview';
        this.toast.error(errorMessage);
      }
    });
  }
}
