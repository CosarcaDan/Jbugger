import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      req = req.clone({
        setHeaders: {
          'Accept': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("token")}`,
          'Access-Control-Expose-Headers': 'Authorization, Content-Disposition',
        }
      });
    console.log("setting auth token", `Bearer ${sessionStorage.getItem("token")}`);
    return next.handle(req);
  }
}
