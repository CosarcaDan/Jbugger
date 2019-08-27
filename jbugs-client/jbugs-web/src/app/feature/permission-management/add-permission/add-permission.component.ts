import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {RoleService} from '../../../core/services/role/role.service';
import {Role} from '../../../core/models/role';
import {Permission} from '../../../core/models/permission';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-permission',
  templateUrl: './add-permission.component.html',
  styleUrls: ['./add-permission.component.css']
})
export class AddPermissionComponent implements OnInit {


  roles: Array<Role>;
  permissions: Array<Permission>;
  angForm: FormGroup;
  selectedRole: number;
  selectedPermissions: number[];

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
      // @ts-ignore
      for (let dataKey of data) {
        this.roles.push(dataKey);
      }
      console.log('roleset: ', this.roles);
    });
  }


  getNewPermissions(id) {
    let role = this.roles.find(r => r.id == id);
    console.log(role);
    this.permissions = new Array<Permission>();
    this.roleService.getPermissionsNotInRole(role).subscribe((data) => {
      console.log('data:', data);
      // @ts-ignore
      for (let dataKey of data) {
        this.permissions.push(dataKey);
      }
      console.log('permissionset: ', this.permissions);
    });
  }

  addPermission() {
    let role = this.roles.find(r => r.id == this.selectedRole);
    let permissions = this.selectedPermissions.map(p => this.permissions.find((pp) => pp.id == p));
    console.log(role, permissions);
    this.roleService.addPermissionToRole(role, permissions);
    this.getNewPermissions(role.id);
    // location.reload();
  }
}
