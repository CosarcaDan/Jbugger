import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../../models/userLogin';
import {map} from 'rxjs/operators';
import {Token} from '../../models/token';
import {User} from '../../models/user';
import {Role} from '../../models/role';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl: string = 'http://localhost:8080/jbugs/services/users';

  constructor(private http: HttpClient) {
  }

  public login(user: UserLogin): Observable<Token> {
    return this.http.post<Token>(this.baseUrl + '/login', user);
  }

  public add(user, roles): Observable<any> {
    let body = new FormData();
    body.append('user', JSON.stringify(user));
    body.append('roles', JSON.stringify(roles));
    console.log('body', body.get('user'));
    return this.http.post(this.baseUrl + '/add', body);
  }

  public edit(user, roles) {
    console.log(user.mobileNumber)
    let body = new FormData();
    body.append('user', JSON.stringify(user));
    body.append('roles', JSON.stringify(roles));
    console.log(body.get('user'))
    return this.http.put(this.baseUrl + '/' + user.id + '/edit', body);
    //this.displayDialog = false;
    //this.search();
  }

  public activate(user) {
    let body = new HttpParams()
      .set('user', JSON.stringify(user));
    return this.http.put(this.baseUrl + '/' + user.id + '/activate', body);
  }

  public deactivate(user) {
    let body = new HttpParams()
      .set('user', JSON.stringify(user));
    return this.http.put(this.baseUrl + '/' + user.id + '/deactivate', body);
  }

  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(this.baseUrl);
  }

  public getUser(id: number): Observable<any> {
    return this.http.get(this.baseUrl + '/' + id);
  }

  public getUserRoles(id: number): Observable<Role[]> {
    return this.http.post<Role[]>(this.baseUrl + '/roles', JSON.stringify(id));
  }


}
