import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../models/userLogin';

import {map} from 'rxjs/operators';
import {Token} from '../models/token';
import {UserAdd} from '../../user-management/models/userAdd';
import {Role} from '../../user-management/models/role';


@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  base_url: string = 'http://localhost:8080/jbugs/services/users';

  constructor(private http: HttpClient) { }

  public login(user: UserLogin): Observable<Token> {
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/login', user).pipe(map(this.extractData));
  }

  //todo de verificat daca merge /add user, roles
  public add(user: UserAdd, roles: Role[]): Observable<UserAdd> {
    console.log(user, roles);
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/add', user, roles, this.httpOptionsWithAuth).pipe(map(this.extractData));
  }

  public getUsers(): Observable<any> {
    return this.http.get(this.base_url).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
