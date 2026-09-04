import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import {
  provideAuth,
  authInterceptor,
  LogLevel
} from 'angular-auth-oidc-client';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        authInterceptor()
      ])
    ),

    provideAuth({
      config: {
        authority: 'http://localhost:8082',

        redirectUrl:
          window.location.origin + '/login/callback',

        postLogoutRedirectUri:
          'http://localhost:8082/login',

        clientId:
          'employee-management-angular',

        scope:
          'openid profile',

        responseType:
          'code',

        silentRenew:
          true,

        useRefreshToken:
          true,

        secureRoutes: [
          'http://localhost:8082/api/'
        ],

        logLevel:
          LogLevel.Debug
      }
    })
  ]
};
