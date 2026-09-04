export type EmployeeStatus = 'ACTIVE' | 'INACTIVE';

export interface Employee {
  id: number;
  employeeCode: string;
  firstName: string;
  lastName: string;
  address: string;
  nic: string;
  mobileNo: string;
  gender: string;
  email: string;
  designationId: number;
  designationName: string;
  profileImage: string | null;
  dateOfBirth: string;
  status: EmployeeStatus;
}

export interface EmployeeSearchParams {
  employeeCode?: string;
  nic?: string;
  name?: string;
  status?: EmployeeStatus | 'ALL';
  page?: number;
  size?: number;
}

export interface EmployeeCreateRequest {
  employeeCode: string;
  firstName: string;
  lastName: string;
  address: string;
  nic: string;
  mobileNo: string;
  gender: string;
  email: string;
  designationId: number;
  dateOfBirth: string;
  status: EmployeeStatus;
}
