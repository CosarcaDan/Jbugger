import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {UserServiceService} from '../service/user-service.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LoginValidators} from './login.validators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers:[ UserServiceService ]
})
export class LoginComponent implements OnInit {

  //username:string;
  //password:string;
  form: FormGroup;

  login() {
    console.log('Login: ', this.username.value, ' ', this.password.value);
    //todo rest request if result ok redirect to main
    this.router.navigate(['/dashboard']);
  }

  constructor(private router: Router, private userService: UserServiceService, private fb: FormBuilder) {
    this.form = fb.group({
      username: [null, [Validators.required, LoginValidators.cannotContainSpace,
        LoginValidators.cannotContainUpperCaseLetter]],
      password: [null, [Validators.required]]
    });
  }

  get username() {
    return this.form.get('username');
  }

  get password() {
    return this.form.get('password');
  }

  ngOnInit() {
  }

}
