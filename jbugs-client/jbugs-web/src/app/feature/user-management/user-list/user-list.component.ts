import {Component, OnInit} from '@angular/core';
import {User} from '../../../core/models/user';
import {UserService} from '../../../core/services/user/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddUserComponent} from '../add-user/add-user.component';
import {RoleService} from '../../../core/services/role/role.service';
import {Role} from '../../../core/models/role';
import {LanguageService} from '../../../core/services/language/language.service';
import {MessageComponent} from '../../../core/message/message.component';

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

  lengthCurrentUsersRoles;

  language: string;

  user: User = {
    id: 0,
    failedLoginAttempt: 0,
    firstName: '',
    lastName: '',
    email: '',
    mobileNumber: '',
    password: '',
    username: '',
    status: null,
  };

  constructor(private userService: UserService, private roleService: RoleService, private modalService: NgbModal, private  languageService: LanguageService) {
  }

  ngOnInit() {
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.getUsers();
    this.language = localStorage.getItem('language');

    this.cols = [
      {field: 'firstName', header: this.languageService.getText('firstName')},
      {field: 'lastName', header: this.languageService.getText('lastName')},
      {field: 'email', header: 'Email'},
      {field: 'mobileNumber', header: this.languageService.getText('phoneNumber')},
      {field: 'username', header: this.languageService.getText('username')},
      {field: 'status', header: 'Status'}
    ];
  }

  /**
   * Gets all users.
   *
   * */
  public getUsers() {
    this.userService.getUsers().subscribe((data) => {
      this.users = data;
    });
  }

  /**
   * On row select it opens a new dialog for editing or activating/deactivating a user from the users list.
   *
   * */
  onRowSelect(event) {
    this.newUser = false;
    this.selectedRoles = [];
    this.user = JSON.parse(JSON.stringify(event.data));
    this.rolesOfCurrentUser = new Array<Role>();
    this.userService.getUserRoles(this.user.id).subscribe((data) => {
      for (let dataKey of data) {
        this.rolesOfCurrentUser.push(dataKey);
      }
      this.roles = new Array<Role>();
      this.roleService.getRoles().subscribe((data) => {
        for (let dataKey of data) {
          this.roles.push(dataKey);
        }
        this.roles.forEach(r => r.checked = this.rolesOfCurrentUser.find(rr => rr.type == r.type) != null);
      });

      this.lengthCurrentUsersRoles = this.rolesOfCurrentUser.length;
    });
    this.displayDialog = true;
  }

  /**
   * Saves the changes after activating a user.
   *
   * */
  activateUser() {
    this.user.status = true;
    this.userService.activate(this.user).subscribe(
      () => {

        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-activate-successful');
        modalRef.result.then(() => {
          this.getUsers();
        });
      },
      (error2 => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-activate-failed') + this.languageService.getText(error2.error.errorCode);
      }))
    ;
    this.displayDialog = false;
  }

  /**
   * Saves the changes after deactivating a user.
   *
   * */
  deactivateUser() {
    this.user.status = false;
    this.userService.deactivate(this.user).subscribe(
      () => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-deactivate-successful');
        modalRef.result.then(() => {
          this.getUsers();
        });
      },
      (error2 => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-deactivate-failed') + this.languageService.getText(error2.error.errorCode);
      }))
    ;
    this.displayDialog = false;
  }

  /**
   * Saves the changes after editing a user.
   *
   * */
  edit() {
    this.getSelectedRoles();
    this.userService.edit(this.user, this.selectedRoles).subscribe(
      (data: {}) => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-edit-successful');
        modalRef.result.then(() => {
          this.getUsers();
        });
      },
      (error2 => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-edit-failed') + this.languageService.getText(error2.error.errorCode);
      }))
    ;
    this.displayDialog = false;
  }

  /**
   * When one role is selected, add it to the roles list of the user.
   *
   * */
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

  /**
   * On the add button click a new modal dialog opens for adding a new user to the users list.
   *
   * */
  add() {
    const modalRef = this.modalService.open(AddUserComponent, {windowClass: 'add-popup'});
    modalRef.result.then(() => {
      this.getUsers();
    });
  }

  /**
   * On one role click it gets checked.
   *
   * */
  onClicked(role, event) {
    for (let i = 0; i < this.roles.length; i++) {
      if (this.roles[i].id == event.target.value) {
        this.roles[i].checked = event.target.checked;
      }
    }
  }

  /**
   * Validators for the user fields.
   *
   * */
  checkFirstName(): boolean {
    let regex = new RegExp('^[A-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ][a-zA-Zșțăîâäöüßáéóőúű]{0,30}[- ]?[a-zșțăîâäöüáéóőúűßA-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ]{0,30}[a-zșțăîâäöüßáéóőúű]$');
    if (regex.test(this.user.firstName)) {
      return true;
    }
    return false;
  }

  checkLastName(): boolean {
    let regex = new RegExp('^[A-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ][a-zA-Zșțăîâäöüßáéóőúű]{0,30}[- ]?[a-zșțăîâäöüáéóőúűßA-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ]{0,30}[a-zșțăîâäöüßáéóőúű]$');
    if (regex.test(this.user.lastName)) {
      return true;
    }
    return false;
  }

  checkPhone(): boolean {
    let regex = new RegExp('^(\\+49)?1(5[12579]|6[023]|7[0-9])[0-9]{7}$|^(004|\\+4)?07[2-8][0-9]{7}$');
    if (regex.test(this.user.mobileNumber)) {
      return true;
    }
    return false;

  }

  checkEmail(): boolean {
    let regex = new RegExp('^[a-zA-Z0-9-_.]*@msggroup\\.com$');
    if (regex.test(this.user.email)) {
      return true;
    }
    return false;
  }
}
