import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user';
import {UserService} from '../../service/user/user.service';
import {Role} from '../models/role';
import {RoleService} from '../../service/role.service';
import {FormGroup,} from '@angular/forms';
import {UserRole} from '../models/userRole';


@Component({
  selector: 'app-get-user',
  templateUrl: './get-user.component.html',
  styleUrls: ['./get-user.component.css']
})
export class GetUserComponent implements OnInit {
  form: FormGroup;
  cols: any[];
  users: User[];
  selectedUser: User;
  newUser: boolean;
  displayDialog: boolean;
  displayAddDialog: boolean;
  selectedRoles: Role[] = [];
  role: Role;
  roles: Array<Role>;
  rolesOfCurrentUser: Array<UserRole>;

  user: User = {
    id: 0,
    counter: 0,
    firstName: '',
    lastName: '',
    email: '',
    mobileNumber: '',
    password: '',
    username: '',
    status: null,
  };


  constructor(private userService: UserService, private roleService: RoleService) {
  }

  ngOnInit() {
    this.getBugs();
    this.getRoles();

    this.cols = [
      {field: 'firstName', header: 'First Name'},
      {field: 'lastName', header: 'Last Name'},
      {field: 'email', header: 'Email'},
      {field: 'mobileNumber', header: 'Mobile Number'},
      {field: 'username', header: 'Username'},
      {field: 'status', header: 'Status'}
    ];
  }

  private getBugs() {
    this.users = [];
    this.userService.getUsers().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.users = data;
    });
  }

  lengthCurrentUsersRoles;
  onRowSelect(event) {
    this.newUser = false;
    this.user = this.cloneUser(event.data);
    this.displayDialog = true;
    this.rolesOfCurrentUser = new Array<Role>();
    this.userService.getUserRoles(this.user.id).subscribe((data) => {
      // @ts-ignore
      for (let dataKey of data) {
        this.rolesOfCurrentUser.push(dataKey);
      }
      this.lengthCurrentUsersRoles = this.rolesOfCurrentUser.length;
    });
  }

  private cloneUser(u: User): User {
    let user = {
      id: 0,
      counter: 0,
      firstName: '',
      lastName: '',
      email: '',
      mobileNumber: '',
      password: '',
      username: '',
      status: null,
      roleList: null,
    };
    for (let prop in u) {
      user[prop] = u[prop];
    }
    return user;
  }

  showDialogToAdd() {
    this.newUser = true;
    //this.user = {};
    this.displayAddDialog = true;
  }

  activateUser() {
    this.user.status = true;
    this.userService.activate(this.user).subscribe(
      (data: {}) => {
        alert(data);
      },
      (error2 => {
        console.log('Error', error2);
        alert('User Activate failed :' + error2.error.detailMessage);
      }))
    ;
  }

  deactivateUser() {
    this.user.status = false;
    this.userService.deactivate(this.user).subscribe(
      (data: {}) => {
        alert(data);
      },
      (error2 => {
        alert('User Deactivate failed :' + error2.error.detailMessage);
      }))
    ;
  }

  edit() {
    this.getSelectedRoles();
    this.userService.edit(this.user, this.selectedRoles).subscribe(
      (data: {}) => {
        alert(data);
      },
      (error2 => {
        console.log('Error', error2);
        alert('Edit User failed :' + error2.error.detailMessage);
      }))
    ;
  }

  getRoles() {
    this.roles = new Array<Role>();
    this.roleService.getRoles().subscribe((data) => {
      // @ts-ignore
      for (let dataKey of data) {
        this.roles.push(dataKey);
      }
      for (let role of this.roles) {
        role.checked = false;
      }
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
