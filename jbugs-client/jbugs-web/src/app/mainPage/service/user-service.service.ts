import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserLogin} from "../models/userLogin";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  base_url:string='http://localhost:8080/user';
  user :UserLogin;

  constructor(private http: HttpClient) { }

  getTodos(): Observable<UserLogin[]>{
    console.log('getting all todos from the server');
    return this.http.get<UserLogin[]>(`${this.base_url}/users`);
  }

  login(username:string, password:string) {
    this.user = {username, password};
    this.http.post<UserLogin>(this.base_url,this.user)
  }
}
