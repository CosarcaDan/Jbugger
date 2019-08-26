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
    // @ts-ignore
    return this.http.post<Token>(this.baseUrl + '/login', user).pipe(map(this.extractData));
  }

  public add(user, roles): Observable<any> {
    let body = new HttpParams()
      .set('user', JSON.stringify(user))
      .set('roles', JSON.stringify(roles));
    console.log('body', body.get('user'));
    return this.http.post(this.baseUrl + '/add', body).pipe(map(this.extractData));
  }

  public edit(user, roles) {
    let body = new HttpParams()
      .set('user', JSON.stringify(user))
      .set('roles', JSON.stringify(roles));
    return this.http.put(this.baseUrl + '/' + user.id + '/edit', body).pipe(map(this.extractData));
    //this.displayDialog = false;
    //this.search();
  }

  public activate(user) {
    let body = new HttpParams()
      .set('user', JSON.stringify(user));
    return this.http.put(this.baseUrl + '/' + user.id + '/activate', body).pipe(map(this.extractData));
  }

  public deactivate(user) {
    let body = new HttpParams()
      .set('user', JSON.stringify(user));
    return this.http.put(this.baseUrl + '/' + user.id + '/deactivate', body).pipe(map(this.extractData));
  }

  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(this.baseUrl);
  }

  public getUser(id: number): Observable<any> {
    return this.http.get(this.baseUrl + '/' + id).pipe(map(this.extractData));
  }

  public getUserRoles(id: number): Observable<Role[]> {
    return this.http.post<Role[]>(this.baseUrl + '/roles', JSON.stringify(id));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }

}
