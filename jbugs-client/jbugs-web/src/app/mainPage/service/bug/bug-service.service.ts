import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class BugServiceService {
  base_url: string = 'http://localhost:8080/jbugs/services/bugs';


  constructor(private http: HttpClient) { }

  //public getUsers(): Observable<any> {
    //return this.http.get(this.base_url, this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  //}

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
