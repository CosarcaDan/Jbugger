import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  base_url: string = 'http://localhost:8080/jbugs/services/roles';

  constructor(private http: HttpClient) {
  }

  public getRoles(): Observable<any> {
    return this.http.get<Observable<any>>(this.base_url);
  }

  public getPermissionsNotInRole(role): Observable<any> {
    return this.http.post('http://localhost:8080/jbugs/services/permissions/not-in-role', JSON.stringify(role));
  }

  public getPermissionsInRole(role): Observable<any> {
    return this.http.post('http://localhost:8080/jbugs/services/permissions/in-role', JSON.stringify(role));
  }

  public addPermissionToRole(role, permissions) {
    console.log(role, permissions);
    let body = new HttpParams()
      .set('role', JSON.stringify(role))
      .set('permissions', JSON.stringify(permissions));
    console.log('body', body.get('role'));
    // http://localhost:8080/jbugs/services/roles/add-permissions
    this.http.post(this.base_url + '/add-permissions', body).subscribe();
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
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
