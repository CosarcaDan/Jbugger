import {Component, OnInit} from '@angular/core';
import {Role} from '../../../core/models/role';
import {Permission} from '../../../core/models/permission';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RoleService} from '../../../core/services/role/role.service';
import {Router} from '@angular/router';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';

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
  private permissionsInRole: Array<Permission>;

  constructor(private roleService: RoleService, private fb: FormBuilder, private router: Router) {
  }

  ngOnInit() {
    this.getRoles();
    this.createForm();
  }

  createForm() {
    console.log('roles:', this.roles);
    this.angForm = this.fb.group({
      roles: ['', Validators.required],
      permission: ['', Validators.required]
    });
  }

  getRoles() {
    this.roles = new Array<Role>();
    this.roleService.getRoles().subscribe((data) => {
      console.log('data:', data);
      for (let dataKey of data) {
        this.roles.push(dataKey);
      }
      console.log('roleset: ', this.roles);
    });
  }

  getNewPermissions(id) {
    let role = this.roles.find(r => r.id == id);
    console.log(role);
    this.permissionsNotInRole = new Array<Permission>();
    this.roleService.getPermissionsNotInRole(role).subscribe((data) => {
      console.log('data:', data);
      for (let dataKey of data) {
        this.permissionsNotInRole.push(dataKey);
      }
      console.log('permissionset: ', this.permissionsNotInRole);
    });

    this.permissionsInRole = new Array<Permission>();
    this.roleService.getPermissionsInRole(role).subscribe((data) => {
      console.log('data:', data);
      for (let dataKey of data) {
        this.permissionsInRole.push(dataKey);
      }
      console.log('permissionset: ', this.permissionsInRole);
    });
  }

  removePermission(permission) {
    let role = this.roles.find(r => r.id == this.selectedRole);
    //let permissions = this.selectedPermissions.map(p => this.permissionsInRole.find((pp) => pp.id == p));
    console.log('remove', role, permission);
    this.roleService.removePermissionToRole(role, permission);
  }

  addPermission(permission) {
    let role = this.roles.find(r => r.id == this.selectedRole);
    console.log('add', role, permission);
    this.roleService.addPermissionToRole(role, permission);
  }

  drop(event: CdkDragDrop<Array<Permission>, any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      console.log(event.previousContainer.element.nativeElement.className.match(' inPermissions') != null, event.previousContainer.data[event.previousIndex]);
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
