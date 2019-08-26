import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../../models/userLogin';

import {map} from 'rxjs/operators';
import {Token} from '../../models/token';
import {User} from "../../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  base_url: string = 'http://localhost:8080/jbugs/services/users';

  constructor(private http: HttpClient) {
  }

  public login(user: UserLogin): Observable<Token> {
    return this.http.post<Token>(this.base_url + '/findByUsernameAndPassword', user);
  }

  public add(user, roles) {
    console.log(user, roles);
    let body = new HttpParams()
      .set('user', JSON.stringify(user))
      .set('roles', JSON.stringify(roles));
    console.log('body', body.get('user'));
    this.http.post(this.base_url + '/add', body).subscribe();
  }

  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(this.base_url);
  }

}
