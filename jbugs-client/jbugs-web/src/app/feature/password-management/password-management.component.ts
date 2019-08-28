import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../core/models/user';
import {UserService} from '../../core/services/user/user.service';
import {AuthService} from '../../core/services/auth/auth.service';
import {AddUserValidators} from '../user-management/add-user/add-user.validators';

@Component({
  selector: 'app-password-management',
  templateUrl: './password-management.component.html',
  styleUrls: ['./password-management.component.css']
})
export class PasswordManagementComponent implements OnInit {

  form: FormGroup;
  loggedUser: User;
  newPassword: string;
  testPassword: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  mobileNumber: string;
  users: Array<User>;

  constructor(private userService: UserService, private fb: FormBuilder,
              private authService: AuthService) {
    this.form = fb.group({
      firstName: [null, [Validators.required, AddUserValidators.validateName]],
      lastName: [null, [Validators.required, AddUserValidators.validateName]],
      email: [null, [Validators.required, AddUserValidators.validateEmail, AddUserValidators.cannotContainSpace]],
      mobileNumber: [null, [Validators.required, AddUserValidators.validateNumber]],
      newPassword: [null, []],
      testPassword: [null, []]
    });
  }

  changePassword() {
    this.newPassword = this.form.get('newPassword').value;
    this.testPassword = this.form.get('testPassword').value;
    this.firstName = this.form.get('firstName').value;
    this.lastName = this.form.get('lastName').value;
    this.mobileNumber = this.form.get('mobileNumber').value;
    this.email = this.form.get('email').value;
    this.username = this.authService.getUsername();
    if (this.newPassword != null && this.testPassword != null) {
      if (this.newPassword.match(this.testPassword)) {
        this.loggedUser = {
          id: null,
          username: this.username,
          password: this.newPassword,
          counter: this.loggedUser.counter,
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
          mobileNumber: this.mobileNumber,
          status: this.loggedUser.status,
        };
        this.userService.changePassword(this.loggedUser).subscribe(
          (data: {}) => {
            alert(data);
          },
          (error2 => {
            console.log('Error', error2);
            alert('Editing data failed:' + error2.error.detailMessage);
          })
        );
      } else {
        alert('Passwords are not equal. Please retry!');
      }
    } else {
      this.loggedUser = {
        id: null,
        username: this.username,
        password: this.loggedUser.password,
        counter: this.loggedUser.counter,
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        mobileNumber: this.mobileNumber,
        status: this.loggedUser.status,
      };
      this.userService.changePassword(this.loggedUser).subscribe(
        (data: {}) => {
          alert(data);
        },
        (error2 => {
          console.log('Error', error2);
          alert('Editing data failed :' + error2.error.detailMessage);
        })
      );
      // console.log('Pass: ', this.loggedUser.password);
    }
  }

  ngOnInit() {
    this.username = this.authService.getUsername();
    this.getUsers();
    console.log('Current users: ', this.users);
    this.userService.getUserByUsername(this.username).subscribe((data) => {
      this.loggedUser = data;
    });
  }

  getUsers() {
    this.users = new Array<User>();
    this.userService.getUsers().subscribe((data) => {
      console.log('data:', data);
      for (let dataKey of data) {
        this.users.push(dataKey);
      }
    });
  }

}
