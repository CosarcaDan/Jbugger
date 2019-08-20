import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserServiceService} from "../service/user-service.service";
import {UserLogin} from "../models/userLogin";
import {Token} from "../models/token";
import {HttpHeaders} from "@angular/common/http";
import {BackendError} from "../models/backendError";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [UserServiceService]
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;

  users: any [];
  userlogin: UserLogin;
  token: Token;
  backendError:BackendError;


  login(form) {
    this.username = form.value.username;
    this.password = form.value.password;
    this.userlogin = {username: this.username, password: this.password};
    //todo rest request if result ok redirect to main
    this.userService.login(this.userlogin).subscribe((data: {}) => {
      // @ts-ignore
      this.token = data;
      console.log(this.token.value);

      this.userService.httpOptionsWithAuth = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': this.token.value,
        })
      };
      sessionStorage.setItem("token",this.token.value);
      this.router.navigate(['/dashboard']);
    },(error1:{}) => {
        // @ts-ignore
      this.backendError = error1.error;
      console.log("Error",this.backendError);
      alert(this.backendError.detailMessage);
    })


  }


  constructor(private router: Router, private userService: UserServiceService) {
  }

  ngOnInit() {
    this.getUseres();
  }

  getUseres() {
    this.users = [];
    this.userService.getUsers().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.users = data;
    });
  }






}
