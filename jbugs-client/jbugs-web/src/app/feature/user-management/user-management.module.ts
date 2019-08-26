import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from "@angular/forms";
import {NgbModalModule} from "@ng-bootstrap/ng-bootstrap";
import {AddUserComponent} from "./add-user/add-user.component";
import {UserListComponent} from "./user-list/user-list.component";
import {TableModule} from "primeng/table";
import {DialogModule} from "primeng/dialog";
import {ButtonModule, InputTextModule} from "primeng/primeng";


@NgModule({
  declarations: [
    AddUserComponent,
    UserListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgbModalModule,
    TableModule,
    DialogModule,
    InputTextModule,
    ButtonModule
  ],
  entryComponents: [
    AddUserComponent,
  ],
  providers: [],
  exports: [
    AddUserComponent
  ]
})
export class UserManagementModule {
}
