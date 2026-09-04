import { Component, inject } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private readonly oidcSecurityService =
    inject(OidcSecurityService);

  login(): void {
    this.oidcSecurityService.authorize();
  }
}
