import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router, private authService: AuthService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (localStorage.getItem('token')) {
      if (state.url == '/dashboard/permissions')
        if (!this.authService.hasPermission('PERMISSION_MANAGEMENT')) {
          this.router.navigate(['dashboard']);
          return false;
        }
      if (state.url.match('/dashboard/bugs/[1-9]'))
        if (!this.authService.hasPermission('BUG_MANAGEMENT')) {
          this.router.navigate(['dashboard']);
          return false;
        }
      if (state.url == '/dashboard/bugs/{id}') {
        if (!this.authService.hasPermission('BUG_MANAGEMENT')) {
          this.router.navigate(['dashboard']);
          return false;
        }
      }
      if (state.url == '/dashboard/users')
        if (!this.authService.hasPermission('USER_MANAGEMENT')) {
          this.router.navigate(['dashboard']);
          return false;
        }
      return true;
    }

    // navigate to not found page
    this.router.navigate(['/login']);
    return false;
  }

}
