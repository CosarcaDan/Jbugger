import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './common/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DashboardComponent} from './common/dashboard/dashboard.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {PermissionManagementModule} from './feature/permission-management/permission-management.module';
import {AuthInterceptor} from './core/interceptors/auth.interceptor';
import {MenubarModule} from 'primeng/menubar';
import {ChartModule} from 'primeng/chart';
import {
  AutoCompleteModule,
  ButtonModule,
  CalendarModule,
  DialogModule,
  FileUploadModule,
  InputTextModule,
  ListboxModule,
  MultiSelectModule,
  PaginatorModule,
  SelectButtonModule,
  SidebarModule,
  SliderModule,
  TabViewModule
} from 'primeng/primeng';
import {AddBugComponent} from './feature/bugs-management/add-bug/add-bug.component';
import {BugsListComponent} from './feature/bugs-management/bugs-list/bugs-list.component';
import {TableModule} from 'primeng/table';
import {ToastModule} from 'primeng/toast';
import {BugManagementModule} from './feature/bugs-management/bug-management.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AddUserComponent} from './feature/user-management/add-user/add-user.component';
import {UserManagementModule} from './feature/user-management/user-management.module';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {NgxFlagIconCssModule} from 'ngx-flag-icon-css';
import {NgbdWelcomeModalContent} from './core/services/auth/auth.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    BugsListComponent,
    NgbdWelcomeModalContent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    PermissionManagementModule,
    BrowserAnimationsModule,
    MultiSelectModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    ChartModule,
    MenubarModule,
    TabViewModule,
    SidebarModule,
    TableModule,
    SliderModule,
    PaginatorModule,
    MultiSelectModule,
    CalendarModule,
    FileUploadModule,
    ToastModule,
    BugManagementModule,
    SelectButtonModule,
    DialogModule,
    UserManagementModule,
    AngularFontAwesomeModule,
    NgxFlagIconCssModule,
    AutoCompleteModule,
    ListboxModule,
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  }],
  bootstrap: [AppComponent],
  entryComponents: [
    AddBugComponent,
    AddUserComponent,
    NgbdWelcomeModalContent
  ]
})
export class AppModule {
}
