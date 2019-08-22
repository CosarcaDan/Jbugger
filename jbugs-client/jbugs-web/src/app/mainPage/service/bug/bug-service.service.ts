import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Bug} from "../../models/bug";

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

  public getBugsAfterSearchCriteria(bugCriteria: Bug) {
    return this.http.post<any>(this.base_url, bugCriteria).pipe(map(this.extractData));
  }

  public deleteBugAfterId(id:number){
    return this.http.delete<any>(this.base_url + "/" +id).pipe(map(this.extractData));
  }




}
