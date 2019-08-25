import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {BackendError} from '../../models/backendError';
import {AddUserValidators} from './add-user.validators';
import {Token} from '../../models/token';
import {UserService} from '../../service/user/user.service';
import {RoleService} from '../../service/role.service';
import {Role} from '../models/role';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  form: FormGroup;
  backendError: BackendError;
  token: Token;

  firstName: string;
  lastName: string;
  mobileNumber: string;
  email: string;
  selectedRoles: Role[] = [];
  role: Role;
  roles: Array<Role>;

  constructor(private router: Router, private userService: UserService,
              private roleService: RoleService, private fb: FormBuilder) {
    this.form = fb.group({
      firstname: [null, [Validators.required, AddUserValidators.validateName,
        AddUserValidators.beginWithCapitalLetter]],
      lastname: [null, [Validators.required, AddUserValidators.validateName,
        AddUserValidators.beginWithCapitalLetter]],
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
    });
  }


  addUser() {
    this.firstName = this.form.get('firstname').value.toString();
    this.lastName = this.form.get('lastname').value.toString();
    this.email = this.form.get('email').value.toString();
    this.mobileNumber = this.form.get('mobilenumber').value.toString();

    this.getSelectedRoles();

    let userToBeAdded = {
      id: null,
      counter: null,
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      mobileNumber: this.mobileNumber,
      password: null,
      status: null
    };

    this.userService.add(userToBeAdded, this.selectedRoles).subscribe(
      (data: {}) => {
        // @ts-ignore
        alert(data);
      }, (error1: {}) => {
        // @ts-ignore
        this.backendError = error1.error;
        console.log('Error', this.backendError);
        alert(this.backendError.detailMessage);
      }
    );
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
