import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddBugComponent} from './add-bug/add-bug.component';
import {ReactiveFormsModule} from '@angular/forms';
import {NgbModalModule} from '@ng-bootstrap/ng-bootstrap';
import {SummaryPipe} from './SummaryPipe';
import {ExcelService} from '../../core/services/excel/excel.service';


@NgModule({
  declarations: [
    AddBugComponent,
    SummaryPipe,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgbModalModule,
  ],
  entryComponents: [
    AddBugComponent,
  ],
  providers: [ExcelService],
  exports: [
    AddBugComponent,
    SummaryPipe,

  ]
})
export class BugManagementModule {
}
