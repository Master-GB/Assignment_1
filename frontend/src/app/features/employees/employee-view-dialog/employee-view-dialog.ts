import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgClass } from '@angular/common';
import { Employee } from '../../../core/models/employee.model';

@Component({
  selector: 'app-employee-view-dialog',
  standalone: true,
  imports: [NgClass],
  templateUrl: './employee-view-dialog.html',
})
export class EmployeeViewDialog {
  @Input() visible = false;
  @Input() employee: Employee | null = null;
  @Output() closed = new EventEmitter<void>();

  close(): void { this.closed.emit(); }

  get fullName(): string {
    if (!this.employee) return '';
    return `${this.employee.firstName} ${this.employee.lastName}`;
  }

  exportPDF(): void {
    if (!this.employee) return;
    const printContents = document.getElementById('view-print-area')?.innerHTML;
    if (!printContents) return;
    const win = window.open('', '_blank', 'width=800,height=600');
    if (!win) return;
    win.document.write(`
      <html><head><title>Employee Details - ${this.fullName}</title>
      <style>
        body { font-family: Arial, sans-serif; padding: 24px; color: #1e293b; }
        h1 { color: #4f46e5; margin-bottom: 4px; }
        .sub { color: #64748b; margin-bottom: 24px; }
        table { width: 100%; border-collapse: collapse; }
        td { padding: 10px 16px; border-bottom: 1px solid #e2e8f0; }
        td:first-child { font-weight: 600; width: 200px; color: #475569; }
        .badge { display: inline-block; padding: 3px 10px; border-radius: 999px;
                 font-size: 12px; font-weight: 600; }
        .badge.active { background: #d1fae5; color: #065f46; }
        .badge.inactive { background: #fee2e2; color: #991b1b; }
      </style></head><body>${printContents}</body></html>
    `);
    win.document.close();
    win.focus();
    setTimeout(() => { win.print(); win.close(); }, 500);
  }

  exportExcel(): void {
    if (!this.employee) return;
    const e = this.employee;
    const rows = [
      ['Employee Code', e.employeeCode],
      ['First Name', e.firstName],
      ['Last Name', e.lastName],
      ['Address', e.address],
      ['NIC', e.nic],
      ['Mobile No', e.mobileNo],
      ['Gender', e.gender],
      ['Email', e.email],
      ['Designation', e.designationName],
      ['Date of Birth', e.dateOfBirth],
      ['Status', e.status],
    ];
    const csv = rows.map(r => r.map(c => `"${c ?? ''}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `employee_${e.employeeCode}.csv`;
    link.click();
  }
}
