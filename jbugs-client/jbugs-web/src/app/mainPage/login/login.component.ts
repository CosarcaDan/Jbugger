import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserServiceService} from '../service/user-service.service';
import {UserLogin} from '../models/userLogin';
import {Token} from '../models/token';
import {BackendError} from '../models/backendError';
import {LoginValidators} from './login.validators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [UserServiceService]
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  users: any [];
  userlogin: UserLogin;
  token: Token;
  backendError: BackendError;
  username: string;
  password: string;


  login() {
    this.username = this.form.get('username').value.toString();
    this.password = this.form.get('password').value.toString();
    this.userlogin = {username: this.username, password: this.password};
    //todo rest request if result ok redirect to main
    this.userService.login(this.userlogin).subscribe((data: {}) => {
      // @ts-ignore
      this.token = data;
      console.log(this.token.value);
      sessionStorage.setItem('token', this.token.value);
      this.router.navigate(['/dashboard']);
    }, (error1: {}) => {
      // @ts-ignore
      this.backendError = error1.error;
      console.log('Error', this.backendError);
      alert(this.backendError.detailMessage);
    });


  }


  constructor(private router: Router, private userService: UserServiceService, private fb: FormBuilder) {
    this.form = fb.group({
      username: [null, [Validators.required, LoginValidators.cannotContainSpace,
        LoginValidators.cannotContainUpperCaseLetter]],
      password: [null, [Validators.required]]
    });
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
