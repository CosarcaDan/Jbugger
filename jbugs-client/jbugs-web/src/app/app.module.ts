import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './mainPage/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {PermissionManagementModule} from "./mainPage/permission-management/permission-management.module";
import {AuthInterceptor} from "./mainPage/interceptors/auth.interceptor";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MultiSelectModule} from 'primeng/multiselect';
import {MenubarModule} from "primeng/menubar";
import {ChartModule} from "primeng/chart";
import {CalendarModule, FileUploadModule, TabViewModule} from "primeng/primeng";
import { AddBugComponent } from './mainPage/bugs-management/add-bug/add-bug.component';
import { GetBugsComponent } from './mainPage/bugs-management/get-bugs/get-bugs.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    AddBugComponent,
    GetBugsComponent,

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    PermissionManagementModule,
    BrowserAnimationsModule,
    MultiSelectModule,
    ReactiveFormsModule,
    MenubarModule,
    ChartModule,
    TabViewModule,
    CalendarModule,
    FileUploadModule

  ],
  providers: [{
    provide : HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi   : true,
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
