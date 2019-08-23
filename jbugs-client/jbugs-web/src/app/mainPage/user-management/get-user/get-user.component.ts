import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user';
import {UserService} from '../../service/user/user.service';

@Component({
  selector: 'app-get-user',
  templateUrl: './get-user.component.html',
  styleUrls: ['./get-user.component.css']
})
export class GetUserComponent implements OnInit {
  cols: any[];
  users: User[];
  selectedUser: User;
  newUser: boolean;
  displayDialog: boolean;

  user: User = {
    id: 0,
    counter: 0,
    firstName: '',
    lastName: '',
    email: '',
    mobileNumber: '',
    password: '',
    username: '',
    status: '',
  };


  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.getBugs();

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

  onRowSelect(event) {
    this.newUser = false;
    this.user = this.cloneUser(event.data);
    this.displayDialog = true;
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
      status: '',
    };
    for (let prop in u) {
      user[prop] = u[prop];
    }
    return user;
  }

  showDialogToAdd() {
    this.newUser = true;
    //this.user = {};
    this.displayDialog = true;
  }

  delete() {

  }

  save() {

  }
}
