import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgClass, DecimalPipe } from '@angular/common';
import { Employee } from '../../../core/models/employee.model';
import { Page } from '../../../core/models/page.model';

@Component({
  selector: 'app-employee-table',
  standalone: true,
  imports: [NgClass, DecimalPipe],
  templateUrl: './employee-table.html',
})
export class EmployeeTable {
  @Input() page: Page<Employee> | null = null;
  @Input() loading = false;

  @Output() view = new EventEmitter<Employee>();
  @Output() edit = new EventEmitter<Employee>();
  @Output() pageChange = new EventEmitter<number>();

  get employees(): Employee[] {
    return this.page?.content ?? [];
  }

  get totalPages(): number {
    return this.page?.totalPages ?? 0;
  }

  get currentPage(): number {
    return this.page?.number ?? 0;
  }

  get totalElements(): number {
    return this.page?.totalElements ?? 0;
  }

  get pageNumbers(): number[] {
    const total = this.totalPages;
    const current = this.currentPage;
    const delta = 2;
    const range: number[] = [];
    for (let i = Math.max(0, current - delta); i <= Math.min(total - 1, current + delta); i++) {
      range.push(i);
    }
    return range;
  }

  prevPage(): void {
    if (this.currentPage > 0) this.pageChange.emit(this.currentPage - 1);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) this.pageChange.emit(this.currentPage + 1);
  }

  goToPage(page: number): void {
    this.pageChange.emit(page);
  }

  skeletonRows = Array(6);
}
