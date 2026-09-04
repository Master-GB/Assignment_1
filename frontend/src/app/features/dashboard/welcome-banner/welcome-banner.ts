import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-welcome-banner',
  standalone: true,
  templateUrl: './welcome-banner.html',
})
export class WelcomeBanner {
  @Input() userName = 'Admin';

  get greeting(): string {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good Morning';
    if (hour < 17) return 'Good Afternoon';
    return 'Good Evening';
  }

  get formattedDate(): string {
    return new Date().toLocaleDateString('en-US', {
      weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',
    });
  }
}
