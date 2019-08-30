import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {BackendError} from '../../../core/models/backendError';
import {AddUserValidators} from './add-user.validators';
import {Token} from '../../../core/models/token';
import {UserService} from '../../../core/services/user/user.service';
import {RoleService} from '../../../core/services/role/role.service';
import {User} from '../../../core/models/user';
import {Role} from '../../../core/models/role';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LanguageService} from '../../../core/services/language/language.service';
import {MessageComponent} from '../../../core/message/message.component';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  form: FormGroup;
  backendError: BackendError;
  userAdd: User;
  token: Token;

  firstname: string;
  lastname: string;
  mobileNumber: string;
  email: string;
  selectedRoles: Role[] = [];
  role: Role;

  roles: Array<Role>;

  constructor(private router: Router, private userService: UserService,
              private roleService: RoleService, private fb: FormBuilder,
              public activeModal: NgbActiveModal, private languageService: LanguageService,
              private modalService: NgbModal) {
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

    let userToBeAdded: User = {
      id: null,
      failedLoginAttempt: null,
      firstName: this.firstname,
      lastName: this.lastname,
      email: this.email,
      mobileNumber: this.mobileNumber,
      password: null,
      username: null,
      status: null
    };
    this.userService.add(userToBeAdded, this.selectedRoles).subscribe(() => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-add-successful');
      },
      (error2 => {
        console.log('Error', error2);
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('user-add-failed')+this.languageService.getText(error2.error.errorCode);
      }));
    this.activeModal.close();

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
