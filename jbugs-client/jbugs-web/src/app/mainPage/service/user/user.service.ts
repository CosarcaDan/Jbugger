import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../../models/userLogin';

import {map} from 'rxjs/operators';
import {Token} from '../../models/token';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  base_url: string = 'http://localhost:8080/jbugs/services/users';

  constructor(private http: HttpClient) {
  }

  public login(user: UserLogin): Observable<Token> {
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/login', user).pipe(map(this.extractData));
  }

  public add(user, roles) {
    console.log(user, roles);
    let body = new HttpParams()
      .set('user', JSON.stringify(user))
      .set('roles', JSON.stringify(roles));
    console.log('body', body.get('user'));
    this.http.post(this.base_url + '/add', body).subscribe();
  }

  public getUsers(): Observable<any> {
    return this.http.get(this.base_url).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
