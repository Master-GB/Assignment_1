import { Routes } from '@angular/router';
import { autoLoginPartialRoutesGuard } from 'angular-auth-oidc-client';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login').then(m => m.Login),
  },
  {
    path: 'login/callback',
    loadComponent: () =>
      import('./features/auth/callback/callback').then(m => m.Callback),
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/shell/shell').then(m => m.Shell),
    canActivate: [autoLoginPartialRoutesGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard-page/dashboard-page').then(m => m.DashboardPage),
      },
      {
        path: 'employees',
        loadComponent: () =>
          import('./features/employees/employee-page/employee-page').then(m => m.EmployeePage),
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
    ],
  },
];
