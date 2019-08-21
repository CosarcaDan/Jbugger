import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {BackendError} from '../../mainPage/models/backendError';
import {AddUserValidators} from './add-user.validators';
import {UserAdd} from '../models/userAdd';
import {Token} from '../../mainPage/models/token';
import {UserService} from '../../mainPage/service/user/user.service';
import {RoleService} from '../../mainPage/service/role.service';
import {Role} from '../models/role';


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

  roles: Array<Role>;

  constructor(private router: Router, private userService: UserService,
              private roleService: RoleService, private fb: FormBuilder) {
    this.form = fb.group({
      firstname: [null, [Validators.required, AddUserValidators.validateName]],
      lastname: [null, [Validators.required, AddUserValidators.validateName]],
      mobilenumber: [null, [Validators.required, AddUserValidators.validateNumber]],
      email: [null, [Validators.required, AddUserValidators.validateEmail,
        AddUserValidators.cannotContainSpace]],
    });
  }

  ngOnInit() {
    this.getRoles();
  }

  getRoles() {
    this.roles = new Array<Role>();
    this.roleService.getRoles().subscribe((data) => {
      console.log('data:', data);
      // @ts-ignore
      for (let dataKey of data) {
        this.roles.push(dataKey);
      }
      for (let role of this.roles) {
        role.checked = false;
      }
      console.log('roleset: ', this.roles);
    });
  }


  addUser() {
    this.firstname = this.form.get('firstname').value.toString();
    this.lastname = this.form.get('lastname').value.toString();
    this.email = this.form.get('email').value.toString();
    this.mobileNumber = this.form.get('mobilenumber').value.toString();

    this.getSelectedRoles();
    //console.log(this.selectedRoles);

    let userToBeAdded: UserAdd = {
      id: null,
      counter: null,
      firstName: this.firstname,
      lastName: this.lastname,
      email: this.email,
      mobileNumber: this.mobileNumber,
      password: null,
      username: null,
      status: null
    };
    this.userService.add(userToBeAdded, this.selectedRoles);
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
