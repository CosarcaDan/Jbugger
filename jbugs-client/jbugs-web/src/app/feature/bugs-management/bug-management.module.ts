import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddBugComponent} from "./add-bug/add-bug.component";
import {ReactiveFormsModule} from "@angular/forms";
import {NgbModalBackdrop} from "@ng-bootstrap/ng-bootstrap/modal/modal-backdrop";
import {MatDialog, MatDialogModule} from "@angular/material";
import {Overlay} from "@angular/cdk/overlay";
import {NgbModalModule} from "@ng-bootstrap/ng-bootstrap";


@NgModule({
  declarations: [
    AddBugComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgbModalModule
  ],
  entryComponents: [
    AddBugComponent,
  ],
  providers: [],
  exports:[
    AddBugComponent
  ]
})
export class BugManagementModule {
}
