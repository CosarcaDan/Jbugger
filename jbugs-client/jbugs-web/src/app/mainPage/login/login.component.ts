import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserService} from '../service/user/user.service';
import {UserLogin} from '../models/userLogin';
import {Token} from '../models/token';
import {BackendError} from '../models/backendError';
import {LoginValidators} from './login.validators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [UserService]
})
export class LoginComponent implements OnInit {

  form: FormGroup;
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
      sessionStorage.setItem('username', this.username);
      this.router.navigate(['/dashboard']);
    }, (error1: {}) => {
      // @ts-ignore
      this.backendError = error1.error;
      console.log('Error', this.backendError);
      alert(this.backendError.detailMessage);
    });
  }


  constructor(private router: Router, private userService: UserService, private fb: FormBuilder) {
    this.form = fb.group({
      username: [null, [Validators.required, LoginValidators.cannotContainSpace,
        LoginValidators.cannotContainUpperCaseLetter]],
      password: [null, [Validators.required]]
    });
  }


  ngOnInit() {
  }


}
