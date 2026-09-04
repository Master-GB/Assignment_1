import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './topbar.html',
})
export class Topbar implements OnInit, OnDestroy {
  private readonly oidc = inject(OidcSecurityService);
  private readonly router = inject(Router);

  userName = signal('Admin');
  currentTime = signal('');
  currentDate = signal('');

  private timerInterval?: ReturnType<typeof setInterval>;
  private sub?: Subscription;

  ngOnInit(): void {
    this.sub = this.oidc.getUserData().subscribe((user: any) => {
      if (user?.preferred_username) this.userName.set(user.preferred_username);
      else if (user?.name) this.userName.set(user.name);
    });
    this.updateTime();
    this.timerInterval = setInterval(() => this.updateTime(), 1000);
  }

  ngOnDestroy(): void {
    clearInterval(this.timerInterval);
    this.sub?.unsubscribe();
  }

  private updateTime(): void {
    const now = new Date();
    this.currentTime.set(
      now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
    );
    this.currentDate.set(
      now.toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
    );
  }

  logout(): void {
    this.oidc.logoff().subscribe(() => this.router.navigate(['/login']));
  }

  get initials(): string {
    return this.userName()
      .split(' ')
      .map(w => w[0])
      .join('')
      .toUpperCase()
      .slice(0, 2);
  }
}
