import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const roleGuard: CanActivateFn = async route => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const requiredRoles = (route.data['roles'] as string[] | undefined) ?? [];

  if (requiredRoles.length === 0) return true;

  try {
    const roles = await auth.getRoles();
    return requiredRoles.some(role => roles.includes(role))
      ? true
      : router.createUrlTree(['/dashboard']);
  } catch {
    return router.createUrlTree(['/login']);
  }
};
