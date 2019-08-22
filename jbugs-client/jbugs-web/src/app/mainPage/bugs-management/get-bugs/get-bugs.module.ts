import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EditBugComponent} from '../edit-bug/edit-bug.component';
import {DynamicDialogComponent, DynamicDialogModule} from 'primeng/dynamicdialog';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    DynamicDialogModule,
  ],
  entryComponents: [
    EditBugComponent,
    DynamicDialogComponent
  ],
  providers: [],
})
export class GetBugsModule {
}
