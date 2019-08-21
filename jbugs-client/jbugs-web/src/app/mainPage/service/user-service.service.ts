import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../models/userLogin';

import {map} from 'rxjs/operators';
import {Token} from '../models/token';
import {Header} from '../models/header';
import {UserAdd} from '../../user-management/models/userAdd';
import {Role} from '../../user-management/models/role';


@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  base_url: string = 'http://localhost:8080/jbugs/services/users';
  user :UserLogin;

  httpOptionsWithoutAuth = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  public httpOptionsWithAuth: Header;

  constructor(private http: HttpClient) { }

  public login(user: UserLogin): Observable<Token> {
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/login', user, this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  }

  //todo de verificat daca merge /add user, roles
  public add(user: UserAdd, roles: Role[]): Observable<UserAdd> {
    console.log(user, roles, this.httpOptionsWithAuth);
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/add', user, roles, this.httpOptionsWithAuth).pipe(map(this.extractData));
  }

  public getUsers(): Observable<any> {
    return this.http.get(this.base_url, this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
