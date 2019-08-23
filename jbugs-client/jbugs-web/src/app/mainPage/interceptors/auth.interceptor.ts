import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url == 'http://localhost:8080/jbugs/services/roles/add-permissions' ||
      req.url == 'http://localhost:8080/jbugs/services/roles/remove-permissions' ||
      req.url == 'http://localhost:8080/jbugs/services/users/add' ||
      req.url == 'http://localhost:8080/jbugs/services/bugs/add') {
      req = req.clone({
        setHeaders: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Accept': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Access-Control-Expose-Headers': 'Authorization',
        }
      });
    } else {
      if (req.url == 'http://localhost:8080/jbugs/services/files/upload') {
        req = req.clone({
          setHeaders: {
            // 'Content-Type': 'multipart/form-data',
            'Accept': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
            'Access-Control-Expose-Headers': 'Authorization, Content-Disposition',
          }
        });
      } else {

        req = req.clone({
          setHeaders: {
            'Content-Type': 'application/json; charset=utf-8',
            'Accept': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
            'Access-Control-Expose-Headers': 'Authorization',
          }
        });
      }
    }
    console.log('setting auth token', `Bearer ${sessionStorage.getItem('token')}`);
    return next.handle(req);
  }
}
