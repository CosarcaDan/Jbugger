import {Component, OnInit} from '@angular/core';
import {User} from '../../../core/models/user';
import {UserService} from '../../../core/services/user/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddUserComponent} from '../add-user/add-user.component';
import {RoleService} from '../../../core/services/role/role.service';
import {Role} from '../../../core/models/role';

@Component({
  selector: 'app-get-user',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  cols: any[];
  users: Array<User>;
  selectedUser: User;
  newUser: boolean;
  displayDialog: boolean;
  displayAddDialog: boolean;
  selectedRoles: Role[] = [];
  role: Role;
  roles: Array<Role>;
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
    this.getUsers();
    //this.getRoles();

    this.cols = [
      {field: 'firstName', header: 'First Name'},
      {field: 'lastName', header: 'Last Name'},
      {field: 'email', header: 'Email'},
      {field: 'mobileNumber', header: 'Mobile Number'},
      {field: 'username', header: 'Username'},
      {field: 'status', header: 'Status'}
    ];
  }

  public getUsers() {
    this.userService.getUsers().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.users = data;

    });
  }

  lengthCurrentUsersRoles;
  onRowSelect(event) {
    this.newUser = false;
    this.selectedRoles=[];
    console.log(event.data);
    this.user = JSON.parse(JSON.stringify(event.data));
    this.rolesOfCurrentUser = new Array<Role>();
    this.userService.getUserRoles(this.user.id).subscribe((data) => {
      for (let dataKey of data) {
        this.rolesOfCurrentUser.push(dataKey);
      }
      this.roles= new  Array<Role>();
      this.roleService.getRoles().subscribe((data) => {
        for (let dataKey of data) {
          this.roles.push(dataKey);
        }
        this.roles.forEach(r =>r.checked=this.rolesOfCurrentUser.find(rr=> rr.type==r.type)!=null)
      });

      console.log(this.roles);
      this.lengthCurrentUsersRoles = this.rolesOfCurrentUser.length;
    });
    this.displayDialog = true;
  }

  activateUser() {
    this.user.status = true;
    this.userService.activate(this.user).subscribe(
      (data: {}) => {
        alert(data);
        this.getUsers();
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
        this.getUsers();
      },
      (error2 => {
        alert('User Deactivate failed :' + error2.error.detailMessage);
      }))
    ;
  }

  edit() {
    this.getSelectedRoles();
    console.log(this.selectedRoles)
    this.userService.edit(this.user, this.selectedRoles).subscribe(
      (data: {}) => {
        alert(data);
        this.getUsers();
        this.getUsers();
      },
      (error2 => {
        console.log('Error', error2);
        alert('Edit User failed :' + error2.error.detailMessage);
      }))
    ;
    //location.reload();
  }

  getRoles() {
    this.roles = new Array<Role>();
    this.roleService.getRoles().subscribe((data) => {
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
    modalRef.result.then(()=>{this.getUsers();});
  }

  onClicked(role, event) {
    for (let i = 0; i < this.roles.length; i++) {
      if (this.roles[i].id == event.target.value) {
        this.roles[i].checked = event.target.checked;
      }
    }
  }
}
