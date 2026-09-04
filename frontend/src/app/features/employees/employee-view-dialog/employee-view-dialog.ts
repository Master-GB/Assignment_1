import { Component, EventEmitter, Input, Output, inject, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { Employee } from '../../../core/models/employee.model';
import { EmployeeService } from '../../../core/services/employee.service';

@Component({
  selector: 'app-employee-view-dialog',
  standalone: true,
  imports: [NgClass],
  templateUrl: './employee-view-dialog.html',
})
export class EmployeeViewDialog {
  private readonly empService = inject(EmployeeService);

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
      },
      error: () => this.exportingPdf.set(false)
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
      },
      error: () => this.exportingExcel.set(false)
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
      },
      error: () => this.exportingHtml.set(false)
    });
  }
}
