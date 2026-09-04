import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-callback',
  standalone: true,
  templateUrl: './callback.html',
  styleUrl: './callback.css'
})
export class Callback {

  private readonly oidcSecurityService =
    inject(OidcSecurityService);

  private readonly router =
    inject(Router);

  constructor() {

    this.oidcSecurityService
      .checkAuth()
      .subscribe(({ isAuthenticated }) => {

        if (isAuthenticated) {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/login']);
        }

      });
  }
}
