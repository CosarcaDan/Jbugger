import {Component, OnInit} from '@angular/core';
import {User} from '../../../core/models/user';
import {UserService} from '../../../core/services/user/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddUserComponent} from '../add-user/add-user.component';
import {RoleService} from '../../../core/services/role/role.service';
import {Role} from '../../../core/models/role';
import {Checkedrole} from '../../../core/models/checkedrole';

@Component({
  selector: 'app-get-user',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  cols: any[];
  users: User[];
  selectedUser: User;
  newUser: boolean;
  displayDialog: boolean;
  displayAddDialog: boolean;
  selectedRoles: Role[] = [];
  role: Role;
  roles: Array<Checkedrole>;
  rolesOfCurrentUser: Array<Role>;

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


  constructor(private userService: UserService, private roleService: RoleService, private modalService: NgbModal) {
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
    console.log(event.data);
    this.user = JSON.parse(JSON.stringify(event.data));
    this.displayDialog = true;
    this.rolesOfCurrentUser = new Array<Role>();
    this.userService.getUserRoles(this.user.id).subscribe((data) => {
      // @ts-ignore
      for (let dataKey of data) {
        this.rolesOfCurrentUser.push(dataKey);
      }
      this.lengthCurrentUsersRoles = this.rolesOfCurrentUser.length;
    });
    // console.log('All roles: ', this.roles);
    // console.log('User\'s roles: ', this.rolesOfCurrentUser);
    // for(let role of this.roles){
    //   console.log('Role', role);
    //   console.log(this.rolesOfCurrentUser.length);
    //  if(this.rolesOfCurrentUser.includes(role)){
    //    this.role.checked = true;
    //  }
    // }
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

  add() {
    const modalRef = this.modalService.open(AddUserComponent, {windowClass: 'add-popup'});
  }

  onClicked(role, event) {
    for (let i = 0; i < this.roles.length; i++) {
      if (this.roles[i].id == event.target.value) {
        this.roles[i].checked = event.target.checked;
      }
    }
  }
}
