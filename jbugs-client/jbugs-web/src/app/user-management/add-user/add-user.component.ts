import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserServiceService} from '../../mainPage/service/user-service.service';
import {RoleServiceService} from '../../mainPage/service/role-service.service';
import {Role} from '../models/role';
import {BackendError} from '../../mainPage/models/backendError';
import {AddUserValidators} from './add-user.validators';
import {HttpHeaders} from '@angular/common/http';
import {UserAdd} from '../models/userAdd';
import {Token} from '../../mainPage/models/token';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  form: FormGroup;
  backendError: BackendError;
  userAdd: UserAdd;
  token: Token;

  firstname: string;
  lastname: string;
  mobileNumber: string;
  email: string;
  selectedRoles: Role[] = [];
  role: Role;

  //todo ne hard-codat
  roles = [
    {id: 1, type: 'Admin', checked: false},
    {id: 2, type: 'Developer', checked: false},
    {id: 3, type: 'Tester', checked: false}
  ];

  constructor(private router: Router, private userService: UserServiceService,
              private roleService: RoleServiceService, private fb: FormBuilder) {
    this.form = fb.group({
      firstname: [null, [Validators.required, AddUserValidators.validateName]],
      lastname: [null, [Validators.required, AddUserValidators.validateName]],
      mobilenumber: [null, [Validators.required, AddUserValidators.validateNumber]],
      email: [null, [Validators.required, AddUserValidators.validateEmail,
        AddUserValidators.cannotContainSpace]],
    });
  }

  ngOnInit() {
  }

  addUser() {
    this.firstname = this.form.get('firstname').value.toString();
    this.lastname = this.form.get('lastname').value.toString();
    this.email = this.form.get('email').value.toString();
    this.mobileNumber = this.form.get('mobilenumber').value.toString();

    this.getSelectedRoles();
    console.log(this.selectedRoles);

    this.userService.add(this.userAdd, this.selectedRoles).subscribe((data: {}) => {
      // @ts-ignore
      this.token = data;
      console.log(this.token.value);
      this.userService.httpOptionsWithAuth = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': this.token.value,
        })
      };
      sessionStorage.setItem('token', this.token.value);
    }, (error1: {}) => {
      // @ts-ignore
      this.backendError = error1.error;
      console.log('Error', this.backendError);
      alert(this.backendError.detailMessage);
    });
  }

  getSelectedRoles() {
    for (let i = 0; i < this.roles.length; i++) {
      if (this.roles[i].checked == true) {
        let role = {} as Role;
        role.id = this.roles[i].id;
        role.type = this.roles[i].type;
        this.selectedRoles.push(role);
      }
    }
  }

  onClicked(role, event) {
    for (let i = 0; i < this.roles.length; i++) {
      if (this.roles[i].id == event.target.value) {
        this.roles[i].checked = event.target.checked;
      }
    }
  }
}
