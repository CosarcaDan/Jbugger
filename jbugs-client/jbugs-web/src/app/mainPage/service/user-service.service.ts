import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserLogin} from "../models/userLogin";

import { map, catchError, tap } from 'rxjs/operators';
import {Token} from "../models/token";
import {Header} from "../models/header";


@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  base_url:string='http://localhost:8080/jbugs/services/users';
  user :UserLogin;

  httpOptionsWithoutAuth = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
    })
  };

  public httpOptionsWithAuth:Header;


  constructor(private http: HttpClient) { }

  public login(user: UserLogin) : Observable<Token>  {
    // @ts-ignore
    return this.http.post<any>(this.base_url + '/login', user, this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  }

  public getUsers(): Observable<any> {
    return this.http.get(this.base_url,this.httpOptionsWithoutAuth).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || { };
  }
}
