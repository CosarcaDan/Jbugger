import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddPermissionComponent } from './add-permission/add-permission.component';
import { RemovePermissionComponent } from './remove-permission/remove-permission.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MultiSelectModule} from "primeng/primeng";



@NgModule({
  declarations: [AddPermissionComponent, RemovePermissionComponent],
  exports: [
    AddPermissionComponent,
    RemovePermissionComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MultiSelectModule
  ]
})
export class PermissionManagementModule { }
