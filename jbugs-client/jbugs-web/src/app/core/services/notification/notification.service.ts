import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../../models/user";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  baseUrl: string = 'http://localhost:8080/jbugs/services/users/notifications';

  user: User = {
    id: 0,
    failedLoginAttempt: 0,
    firstName: '',
    lastName: '',
    email: '',
    mobileNumber: '',
    password: '',
    username: '',
    status: null,
  };

  constructor(private http: HttpClient) {
  }

  public getMyNotification(username: string): Observable<any> {
    this.user.username = username;
    console.log('usernameForNotification', username);
    return this.http.post(this.baseUrl, this.user);
  }
}

