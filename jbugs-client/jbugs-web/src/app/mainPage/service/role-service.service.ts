import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Header} from '../models/header';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RoleServiceService {

  base_url: string = 'http://localhost:8080/jbugs/services/roles';

  public httpOptionsWithAuth: Header;

  constructor(private http: HttpClient) {
  }

  public getRoles(): Observable<any> {
    return this.http.get(this.base_url, this.httpOptionsWithAuth).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
