import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Permission} from '../../models/permission';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  baseUrl: string = 'http://localhost:8080/jbugs/services/roles';

  constructor(private http: HttpClient) {
  }

  /**
   * Sends a get request for all the roles.
   *
   * @return the url of get request.
   * */
  public getRoles(): Observable<any> {
    return this.http.get<any>(this.baseUrl);
  }

  /**
   * Sends a post request to get all the permissions that aren't contain in the given role.
   * @param role - Role
   *
   * @return the url of post request.
   * */
  public getPermissionsNotInRole(role): Observable<Array<Permission>> {
    return this.http.post<Array<Permission>>('http://localhost:8080/jbugs/services/permissions/not-in-role', JSON.stringify(role));
  }

  /**
   * Sends a post request to get all the permissions that are contain in the given role.
   * @param role - Role
   *
   * @return the url of post request.
   * */
  public getPermissionsInRole(role): Observable<Array<Permission>> {
    return this.http.post<Array<Permission>>('http://localhost:8080/jbugs/services/permissions/in-role', JSON.stringify(role));
  }

  /**
   * Sends a post request to add a new permission to a role.
   * @param role - Role
   * @param permissions - Permission; the permission to add to the given role.
   *
   * @return the url of post request.
   * */
  public addPermissionToRole(role, permissions) {
    let body = new FormData();
    body.append('role', JSON.stringify(role));
    body.append('permission', JSON.stringify(permissions));
    this.http.post(this.baseUrl + '/add-permissions', body).subscribe();
  }

  /**
   * Sends a post request to remove a permission from a role.
   * @param role - Role
   * @param permissions - Permission; the permission to remove from the given role.
   *
   * @return the url of post request.
   * */
  public removePermissionToRole(role, permissions) {
    let body = new FormData();
    body.append('role', JSON.stringify(role));
    body.append('permission', JSON.stringify(permissions));
    this.http.post(this.baseUrl + '/remove-permissions', body).subscribe();
  }
}
