import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild, AfterViewInit } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { NgClass } from '@angular/common';
import { EmployeeService } from '../../../core/services/employee.service';
import { DesignationService } from '../../../core/services/designation.service';
import { ToastService } from '../../../core/services/toast.service';
import { Designation } from '../../../core/models/designation.model';
import { Employee } from '../../../core/models/employee.model';
import { ElementRef } from '@angular/core';

@Component({
  selector: 'app-employee-form-dialog',
  standalone: true,
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './employee-form-dialog.html',
})
export class EmployeeFormDialog implements OnInit, OnChanges, AfterViewInit {
  @Input() visible = false;
  @Input() employee: Employee | null = null;   // null = create mode
  @Output() closed = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>();

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  private readonly fb = inject(FormBuilder);
  private readonly empService = inject(EmployeeService);
  private readonly desgService = inject(DesignationService);
  private readonly toast = inject(ToastService);

  designations: Designation[] = [];
  imagePreview: string | null = null;
  selectedFile: File | null = null;
  submitting = false;
  errorMessage = '';
  private objectUrl: string | null = null;

  form = this.fb.group({
    employeeCode:  ['', [Validators.required, Validators.pattern(/^[A-Za-z0-9\-]+$/)]],
    firstName:     ['', [Validators.required, Validators.minLength(2)]],
    lastName:      ['', [Validators.required, Validators.minLength(2)]],
    address:       ['', Validators.required],
    nic:           ['', [Validators.required, Validators.pattern(/^\d{9}[VvXx]$|^\d{12}$/)]],
    mobileNo:      ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
    gender:        ['', Validators.required],
    email:         ['', [Validators.required, Validators.email]],
    designationId: [null as number | null, Validators.required],
    dateOfBirth:   ['', Validators.required],
    status:        ['ACTIVE', Validators.required],
  });

  get isEdit(): boolean { return !!this.employee; }
  get title(): string { return this.isEdit ? 'Edit Employee' : 'Register New Employee'; }

  get maxDateOfBirth(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.desgService.getAll().subscribe(d => this.designations = d);
    
    // Add date of birth validator
    this.form.get('dateOfBirth')?.addValidators(this.dateOfBirthValidator());
  }

  private dateOfBirthValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
      
      const dob = new Date(control.value);
      const today = new Date();
      
      // Check if date is in the future
      if (dob > today) {
        return { futureDate: true };
      }
      
      // Check if person is at least 18 years old
      const minDate = new Date();
      minDate.setFullYear(today.getFullYear() - 18);
      
      if (dob > minDate) {
        return { underage: true };
      }
      
      return null;
    };
  }

  ngAfterViewInit(): void {
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['employee'] || changes['visible']) {
      if (this.visible) {
        this.errorMessage = '';
        this.selectedFile = null;
        this.imagePreview = null;
        if (this.objectUrl) {
          URL.revokeObjectURL(this.objectUrl);
          this.objectUrl = null;
        }
        if (this.employee) {
          this.form.patchValue({
            ...this.employee,
            designationId: this.employee.designationId,
            dateOfBirth: this.employee.dateOfBirth ?? '',
            status: this.employee.status,
          });
          this.imagePreview = this.employee.profileImage ?? null;
        } else {
          this.form.reset({ status: 'ACTIVE', gender: '' });
        }
      }
    }
  }

  isInvalid(name: string): boolean {
    const ctrl = this.form.get(name);
    return !!(ctrl?.invalid && ctrl?.touched);
  }

  errorFor(name: string): string {
    const ctrl = this.form.get(name) as AbstractControl;
    if (!ctrl?.errors || !ctrl.touched) return '';
    if (ctrl.errors['required']) return 'This field is required.';
    if (ctrl.errors['email']) return 'Enter a valid email address.';
    if (ctrl.errors['minlength']) return `Minimum ${ctrl.errors['minlength'].requiredLength} characters.`;
    if (ctrl.errors['pattern']) {
      if (name === 'nic') return 'Enter a valid NIC (e.g. 199512345678 or 952345678V).';
      if (name === 'mobileNo') return 'Enter a 10-digit mobile number.';
      if (name === 'employeeCode') return 'Only letters, numbers and hyphens allowed.';
    }
    if (ctrl.errors['futureDate']) return 'Date of birth cannot be in the future.';
    if (ctrl.errors['underage']) return 'Employee must be at least 18 years old.';
    return 'Invalid value.';
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    if (!file.type.startsWith('image/')) { this.errorMessage = 'Only image files are allowed.'; return; }
    if (file.size > 5 * 1024 * 1024) { this.errorMessage = 'Image must be less than 5 MB.'; return; }
    
    if (this.objectUrl) {
      URL.revokeObjectURL(this.objectUrl);
    }
    
    this.selectedFile = file;
    this.objectUrl = URL.createObjectURL(file);
    this.imagePreview = this.objectUrl;
  }

  submit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    this.submitting = true;
    this.errorMessage = '';
    const val = this.form.value as any;
    const request = { ...val, designationId: Number(val.designationId) };

    const call = this.isEdit
      ? this.empService.update(this.employee!.id, request, this.selectedFile ?? undefined)
      : this.empService.create(request, this.selectedFile ?? undefined);

    call.subscribe({
      next: () => {
        this.submitting = false;
        const message = this.isEdit ? 'Employee updated successfully' : 'Employee created successfully';
        this.toast.success(message);
        this.saved.emit();
        this.close();
      },
      error: (err) => {
        this.submitting = false;
        const errorMessage = err.error?.message ?? 'Failed to save employee. Please try again.';
        this.errorMessage = errorMessage;
        this.toast.error(errorMessage);
      },
    });
  }

  close(): void { this.closed.emit(); }
}
