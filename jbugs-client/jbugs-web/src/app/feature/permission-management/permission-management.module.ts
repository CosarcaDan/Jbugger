import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddPermissionComponent} from './add-permission/add-permission.component';
import {RemovePermissionComponent} from './remove-permission/remove-permission.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MultiSelectModule} from 'primeng/primeng';
import {PermissionManagementComponent} from './permission-management/permission-management.component';
import {DragDropModule} from '@angular/cdk/drag-drop';


@NgModule({
  declarations: [AddPermissionComponent, RemovePermissionComponent, PermissionManagementComponent],
  exports: [
    AddPermissionComponent,
    RemovePermissionComponent,
    PermissionManagementComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MultiSelectModule,
    DragDropModule
  ]
})
export class PermissionManagementModule {
}
