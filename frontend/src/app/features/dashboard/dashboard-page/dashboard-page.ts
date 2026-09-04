import { Component, inject, OnInit, signal } from '@angular/core';
import { NgClass, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../../core/services/dashboard.service';
import { DashboardSummary } from '../../../core/models/dashboard.model';
import { StatCard } from '../stat-card/stat-card';
import { WelcomeBanner } from '../welcome-banner/welcome-banner';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [NgClass, DecimalPipe, StatCard, WelcomeBanner, RouterLink],
  templateUrl: './dashboard-page.html',
})
export class DashboardPage implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly oidc = inject(OidcSecurityService);

  summary = signal<DashboardSummary | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);
  userName = signal('Admin');

  readonly cards = [
    {
      key: 'totalEmployees' as keyof DashboardSummary,
      label: 'Total Employees',
      subtitle: 'All registered employees',
      gradient: 'from-indigo-500 to-purple-600',
      icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-7 h-7"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>`,
    },
    {
      key: 'activeEmployees' as keyof DashboardSummary,
      label: 'Active Employees',
      subtitle: 'Currently employed',
      gradient: 'from-emerald-500 to-teal-600',
      icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-7 h-7"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
    },
    {
      key: 'inactiveEmployees' as keyof DashboardSummary,
      label: 'Inactive Employees',
      subtitle: 'Deactivated accounts',
      gradient: 'from-rose-500 to-pink-600',
      icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-7 h-7"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>`,
    },
    {
      key: 'totalDesignations' as keyof DashboardSummary,
      label: 'Designations',
      subtitle: 'Available job roles',
      gradient: 'from-amber-500 to-orange-600',
      icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-7 h-7"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"/></svg>`,
    },
  ];

  ngOnInit(): void {
    this.oidc.getUserData().subscribe((user: any) => {
      if (user?.preferred_username) this.userName.set(user.preferred_username);
      else if (user?.name) this.userName.set(user.name);
    });

    this.dashboardService.getSummary().subscribe({
      next: (data) => { this.summary.set(data); this.loading.set(false); },
      error: (err) => { this.error.set('Failed to load dashboard data.'); this.loading.set(false); },
    });
  }

  getCount(key: keyof DashboardSummary): number | null {
    const s = this.summary();
    return s ? s[key] : null;
  }
}
