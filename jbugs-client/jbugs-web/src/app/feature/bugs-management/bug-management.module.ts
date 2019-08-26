import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddBugComponent} from "./add-bug/add-bug.component";
import {ReactiveFormsModule} from "@angular/forms";
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
  exports: [
    AddBugComponent
  ]
})
export class BugManagementModule {
}
