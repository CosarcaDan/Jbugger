import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLogin} from '../../models/userLogin';
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

  /**
   * Sends a post request to login the user.
   * @param user - UserLogin
   *
   * @return the url of post request.
   * */
  public login(user: UserLogin): Observable<Token> {
    return this.http.post<Token>(this.baseUrl + '/login', user);
  }

  /**
   * Sends a post request to add new roles to a user.
   * @param user - User
   * @param roles - Roles; the roles to add to the given user.
   *
   * @return the url of post request.
   * */
  public add(user, roles): Observable<any> {
    let body = new FormData();
    body.append('user', JSON.stringify(user));
    body.append('roles', JSON.stringify(roles));
    return this.http.post(this.baseUrl + '/add', body);
  }

  /**
   * Sends a put request to edit the roles of an user.
   * @param user - User
   * @param roles - Roles; the roles to edit to the given user.
   *
   * @return the url of put request.
   * */
  public edit(user, roles) {
    let body = new FormData();
    body.append('user', JSON.stringify(user));
    body.append('roles', JSON.stringify(roles));
    return this.http.put(this.baseUrl + '/' + user.id + '/edit', body);
  }

  /**
   * Sends a put request to activate an user.
   * @param user - User
   *
   * @return the url of put request.
   * */
  public activate(user) {
    return this.http.put(this.baseUrl + '/' + user.id + '/activate', user);
  }

  /**
   * Sends a put request to deactivate an user.
   * @param user - User
   *
   * @return the url of put request.
   * */
  public deactivate(user) {
    return this.http.put(this.baseUrl + '/' + user.id + '/deactivate', user);
  }

  /**
   * Sends a get request to get all users.
   *
   * @return the url of get request.
   * */
  public getUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(this.baseUrl);
  }

  /**
   * Sends a get request to get an user.
   *
   * @return the url of get request.
   * */
  public getUser(id: number): Observable<any> {
    return this.http.get(this.baseUrl + '/' + id);
  }

  /**
   * Sends a get request to get an user by its username.
   * @param username - string; the username of the searched user.
   *
   * @return the url of get request.
   * */
  public getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(this.baseUrl + '/' + username + '/get');
  }

  /**
   * Sends a post request to get all roles of an given user.
   * @param id - number; the id of the user.
   *
   * @return the url of post request.
   * */
  public getUserRoles(id: number): Observable<Role[]> {
    return this.http.post<Role[]>(this.baseUrl + '/roles', JSON.stringify(id));
  }

  /**
   * Sends a put request to change the password of the given user.
   * @param user - User
   *
   * @return the url of put request.
   * */
  public changePassword(user): Observable<User> {
    return this.http.put<User>(this.baseUrl + '/changePassword', user);
  }
}
