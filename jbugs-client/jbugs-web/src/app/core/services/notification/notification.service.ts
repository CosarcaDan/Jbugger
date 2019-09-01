import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../models/user';
import {Notification} from '../../../core/models/notification';

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

  /**
   * Sends a post request to get all notifications of a user.
   * @param username - string; the user who has notifications.
   *
   * @return the url of the post request.
   * */
  public getMyNotification(username: string): Observable<any> {
    this.user.username = username;
    return this.http.post(this.baseUrl, this.user);
  }

  /**
   * Sends a delete request to set the notification to seen.
   * @param notification - Notification; the notification that was already seen.
   *
   * @return the url of the delete request.
   * */
  public seen(notification: Notification) {
    return this.http.delete(this.baseUrl + '/' + notification.id + '/seen');
  }
}

