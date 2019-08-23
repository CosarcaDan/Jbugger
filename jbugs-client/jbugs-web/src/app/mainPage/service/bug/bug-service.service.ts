import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Bug} from '../../models/bug';

@Injectable({
  providedIn: 'root'
})
export class BugServiceService {
  base_url: string = 'http://localhost:8080/jbugs/services/bugs';

  httpOptionsWithoutAuth = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  public add(bug) {
    console.log(bug);
    let body = new HttpParams()
      .set('bug', JSON.stringify(bug));
    console.log('body', body.get('bug'));
    this.http.post(this.base_url + '/add', body).subscribe();
  }

  constructor(private http: HttpClient) {
  }

  public getBugs(): Observable<Bug> {
    // @ts-ignore
    return this.http.get<Bug>(this.base_url, this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }


}
