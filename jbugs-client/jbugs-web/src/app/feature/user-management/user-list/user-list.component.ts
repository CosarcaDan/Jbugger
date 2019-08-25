import {Component, OnInit} from '@angular/core';
import {User} from '../../../core/models/user';
import {UserService} from '../../../core/services/user/user.service';
import {AddBugComponent} from "../../bugs-management/add-bug/add-bug.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddUserComponent} from "../add-user/add-user.component";

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


  constructor(private userService: UserService, private modalService: NgbModal) {
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
    this.user = this.cloneUser(JSON.parse(event.data.toString()));
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

  add(){
      const modalRef = this.modalService.open(AddUserComponent,{windowClass : "add-popup"});
  }

  save() {

  }
}
