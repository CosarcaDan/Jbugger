import {Component, Injectable, Input} from '@angular/core';
import {UserLogin} from '../../models/userLogin';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {delay} from 'q';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MessageComponent} from '../../message/message.component';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router, private modalService: NgbModal) {
  }
  private cachedPermissions: string[] = null;
  private lastPermissionUpdate = Date.now();
  private requestSent: boolean = false;
  getToken() {
    let token = localStorage.getItem('token');
    if (!token)
      return 'Bearer ';
    // if(this.isTokenExpired(token))
    // {
    //   this.renew(this.decodeToken(token).subject);
    //   token=localStorage.getItem('token');
    // }
    return 'Bearer ' + token;
  }
  getUsername() {
    return this.decodeToken(localStorage.getItem('token')).sub;
  }
  public login(user: UserLogin) {
    this.http.post<any>('http://localhost:8080/jbugs/services/users/login', user).subscribe(async (data) => {
      console.log('data', data);
      localStorage.setItem('token', data.value);
      localStorage.setItem('language', 'en');
      const modalRef = this.modalService.open(NgbdWelcomeModalContent);
      this.getPermissions();
      await delay(1000);
      modalRef.close();
      this.router.navigate(['dashboard']);
    }, (error1:HttpErrorResponse) => {
      console.log('Error', error1.error.errorCode);
      const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
      if(error1.error.errorCode == 'msg - 001')
        modalRef.componentInstance.message = "Username or password Incorrect!";
      if(error1.error.errorCode == 'msg - 002')
        modalRef.componentInstance.message = "You entered an incorrect password 5 times, your User got deactivated. Please contact your administrator!";
      if(error1.error.errorCode == 'msg - 003')
        modalRef.componentInstance.message = "Your User is deactivated. Please contact your administrator!";
    });
  }
  public logout() {
    console.log('un: ', this.getUsername());
    this.http.post<any>('http://localhost:8080/jbugs/services/users/logout', {username: this.getUsername()}).subscribe((data) => {
      localStorage.clear();
      this.router.navigate(['login']);
    }, (error1) => {
      console.log('Error', error1.error);
    });
  }
  public renew(username: string) {
    this.http.post<any>('http://localhost:8080/jbugs/services/users/renew', {username: username}).subscribe((data) => {
      console.log(data);
      localStorage.setItem('token', data.value);
    }, (error1) => {
      console.log('Error', error1.error);
    });
  }
  public getPermissions() {
    console.log('DING!');
    this.http.post<any>('http://localhost:8080/jbugs/services/users/permissions', {username: this.getUsername()}).subscribe((data) => {
      localStorage.setItem('permissionsNotInRole', JSON.stringify(data.map(p => p.type)));
      this.cachedPermissions = data.map(p => p.type);
      this.requestSent = false;
      return data.map(p => p.type);
    });
  }
  public hasPermission(permission: string) {
    // console.log('isSet: ',this.cachedPermissions,'last:',this.lastPermissionUpdate,'now:',Date.now(),'reqSent:',this.requestSent);
    if ((this.cachedPermissions == null || this.lastPermissionUpdate + 60000 < Date.now()) && !this.requestSent) {
      this.requestSent = true;
      this.getPermissions();
      this.lastPermissionUpdate = Date.now();
    }
    return JSON.parse(localStorage.getItem('permissionsNotInRole')).filter(p => p == permission).length != 0;
  }

  private b64c: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';   // base64 dictionary
  private b64u: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_';  // base64url dictionary
  private b64pad: string = '=';
  /* base64_charIndex
   * Internal helper to translate a base64 character to its integer index.
   */
  private base64_charIndex(c) {
    if (c == '+') {
      return 62;
    }
    if (c == '/') {
      return 63;
    }
    return this.b64u.indexOf(c)
  }
  /* base64_decode
   * Decode a base64 or base64url string to a JavaScript string.
   * Input is assumed to be a base64/base64url encoded UTF-8 string.
   * Returned result is a JavaScript (UCS-2) string.
   */
  private base64Decode(data: string) {
    let dst = "";
    let i = 0, a, b, c, d, z;
    let len = data.length;
    for (; i < len - 3; i += 4) {
      a = this.base64_charIndex(data.charAt(i + 0));
      b = this.base64_charIndex(data.charAt(i + 1));
      c = this.base64_charIndex(data.charAt(i + 2));
      d = this.base64_charIndex(data.charAt(i + 3));
      dst += String.fromCharCode((a << 2) | b >>> 4);
      if (data.charAt(i + 2) != this.b64pad) {
        dst += String.fromCharCode(((b << 4) & 0xF0) | ((c >>> 2) & 0x0F));
      }
      if (data.charAt(i + 3) != this.b64pad) {
        dst += String.fromCharCode(((c << 6) & 0xC0) | d);
      }
    }
    return decodeURIComponent(dst);
  }
  // decode token
  private decodeToken(token: string) {
    let parts = token.split('.');
    if (parts.length !== 3) {
      throw new Error('JWT must have 3 parts');
    }
    let decoded = this.base64Decode(parts[1]);
    if (decoded[decoded.length - 1] != '}') {
      decoded += '}';
    }
    if (!decoded) {
      throw new Error('Cannot decode the token');
    }
    return JSON.parse(decoded);
  }
  private getTokenExpirationDate(token: string) {
    let decoded: any;
    decoded = this.decodeToken(token);
    if (typeof decoded.exp === 'undefined') {
      return null;
    }
    let date = new Date(0); // The 0 here is the key, which sets the date to the epoch
    date.setUTCSeconds(decoded.exp);
    return date;
  }
  private isTokenExpired(token: string, offsetSeconds?: number) {
    let date = this.getTokenExpirationDate(token);
    offsetSeconds = offsetSeconds || 0;
    if (date === null) {
      return false;
    }
    // Token expired?
    return !(date.valueOf() > (new Date().valueOf() + (offsetSeconds * 1000)));
  }

}
@Component({
  selector: 'ngbd-modal-content',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">Hi there!</h4>
      <span aria-hidden="true">&times;</span>
    </div>
    <div class="modal-body">
      <p>Welcome to Jbugger!</p>
    </div>
  `
})
export class NgbdWelcomeModalContent {
  @Input() name;
  constructor(public activeModal: NgbActiveModal) {
  }
}
