import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Bug} from '../../models/bug';

@Injectable({
  providedIn: 'root'
})
export class BugServiceService {
  baseUrl: string = 'http://localhost:8080/jbugs/services/bugs';

  httpOptionsWithoutAuth = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  public add(bug, attachment): Observable<Bug> {
    console.log(bug);
    let body = new HttpParams()
      .set('bug', JSON.stringify(bug))
      .set('attachment', JSON.stringify(attachment));
    console.log('body', body.get('bug'));
    console.log('body', body.get('attachment'));
    return this.http.post<Bug>(this.baseUrl + '/add', body);
  }

  constructor(private http: HttpClient) {
  }

  public getBugs(): Observable<Bug> {
    // @ts-ignore
    return this.http.get<Bug>(this.baseUrl, this.httpOptionsWithoutAuth);
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }

  public getBugsAfterSearchCriteria(bugCriteria: Bug) {
    return this.http.post<any>(this.baseUrl, bugCriteria);
  }
  public deleteBugAfterId(id: number) {
    return this.http.delete<any>(this.baseUrl + '/' + id);
  }

  public exportInPdf(bug: Bug) {
    return this.http.post<any>(this.baseUrl + '/getPDF', bug);
  }

  public saveEditBug(bug: Bug, attachment) {
    console.log(bug);
    let body = new HttpParams()
      .set('bug', JSON.stringify(bug))
      .set('attachment', JSON.stringify(attachment));
    console.log('body', body.get('bug'));
    console.log('body', body.get('attachment'));
    return this.http.put(this.baseUrl + '/' + bug.id + '/' + 'edit', body);
  }
}
