import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserServiceService} from "../service/user-service.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers:[ UserServiceService ]
})
export class LoginComponent implements OnInit {

  username:string;
  password:string;
  login(form){
     this.username = form.value.username;
     this.password = form.value.password;
    console.log("Login",this.password);
    //todo rest request if result ok redirect to main
    this.router.navigate(['/dashboard']);
  }

  constructor(private router: Router,private userService: UserServiceService) { }

  ngOnInit() {
  }

}
