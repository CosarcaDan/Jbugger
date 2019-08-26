import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Permission} from "../../models/permission";
import {Role} from "../../models/role";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  base_url: string = 'http://localhost:8080/jbugs/services/roles';

  constructor(private http: HttpClient) {
  }

  public getRoles(): Observable<Array<Role>> {
    return this.http.get<Array<Permission>>(this.base_url);
  }

  public getPermissionsNotInRole(role): Observable<Array<Permission>> {
    return this.http.post<Array<Permission>>('http://localhost:8080/jbugs/services/permissions/not-in-role', JSON.stringify(role));
  }

  public getPermissionsInRole(role): Observable<Array<Permission>> {
    return this.http.post<Array<Permission>>('http://localhost:8080/jbugs/services/permissions/in-role', JSON.stringify(role));
  }

  public addPermissionToRole(role, permissions) {
    console.log(role, permissions);
    let body = new HttpParams()
      .set('role', JSON.stringify(role))
      .set('permissions', JSON.stringify(permissions));
    console.log('body', body.get('role'));
    this.http.post(this.base_url + '/add-permissions', body).subscribe();
  }

  removePermissionToRole(role, permissions) {
    console.log(role, permissions);
    let body = new HttpParams()
      .set('role', JSON.stringify(role))
      .set('permissions', JSON.stringify(permissions));
    console.log('body', body.get('role'));
    this.http.post(this.base_url + '/remove-permissions', body).subscribe();
  }
}
