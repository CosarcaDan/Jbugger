import {Component, OnInit} from '@angular/core';
import {Role} from '../../../core/models/role';
import {Permission} from '../../../core/models/permission';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RoleService} from '../../../core/services/role/role.service';
import {Router} from '@angular/router';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {LanguageService} from '../../../core/services/language/language.service';

@Component({
  selector: 'app-permission-management',
  templateUrl: './permission-management.component.html',
  styleUrls: ['./permission-management.component.css']
})
export class PermissionManagementComponent implements OnInit {


  roles: Array<Role>;
  permissionsNotInRole: Array<Permission>;
  angForm: FormGroup;
  selectedRole: number;
  permissionsInRole: Array<Permission>;

  constructor(private roleService: RoleService, private fb: FormBuilder, private router: Router, private languageService: LanguageService) {
  }

  ngOnInit() {
    this.getRoles();
    this.createForm();
  }

  createForm() {
    this.angForm = this.fb.group({
      roles: ['', Validators.required],
      permission: ['', Validators.required]
    });
  }

  /**
   * Gets the role list.
   *
   * */
  getRoles() {
    this.roles = new Array<Role>();
    this.roleService.getRoles().subscribe((data) => {
      for (let dataKey of data) {
        this.roles.push(dataKey);
      }
    });
  }

  /**
   * Gets the permission list for a selected role (the ones that are not in role and the ones that already exists).
   * @param id - number; the id of the role.
   *
   * */
  getNewPermissions(id) {
    let role = this.roles.find(r => r.id == id);
    this.permissionsNotInRole = new Array<Permission>();
    this.roleService.getPermissionsNotInRole(role).subscribe((data) => {
      for (let dataKey of data) {
        this.permissionsNotInRole.push(dataKey);
      }
    });

    this.permissionsInRole = new Array<Permission>();
    this.roleService.getPermissionsInRole(role).subscribe((data) => {
      for (let dataKey of data) {
        this.permissionsInRole.push(dataKey);
      }
    });
  }

  /**
   * Deletes a permission from the permission list of the selected role.
   *
   * */
  removePermission(permission) {
    let role = this.roles.find(r => r.id == this.selectedRole);
    //let permissions = this.selectedPermissions.map(p => this.permissionsInRole.find((pp) => pp.id == p));
    this.roleService.removePermissionToRole(role, permission);
  }

  /**
   * Adds a permission into the permission list of the selected role.
   *
   * */
  addPermission(permission) {
    let role = this.roles.find(r => r.id == this.selectedRole);
    this.roleService.addPermissionToRole(role, permission);
  }

  /**
   * Moves elements from one side of the drag and drop to the another side.
   *
   * */
  drop(event: CdkDragDrop<Array<Permission>, any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      if (event.previousContainer.element.nativeElement.className.match(' inPermissions') == null) {
        this.addPermission(event.previousContainer.data[event.previousIndex]);
      } else {
        this.removePermission(event.previousContainer.data[event.previousIndex]);
      }
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }
}
