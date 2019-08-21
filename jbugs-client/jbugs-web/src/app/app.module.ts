import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './mainPage/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {HttpClientModule} from '@angular/common/http';
import {MenubarModule} from "primeng/menubar";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ChartModule} from "primeng/chart";
import {
  ButtonModule,
  CalendarModule,
  FileUploadModule,
  InputTextModule,
  MultiSelectModule,
  PaginatorModule,
  SelectButtonModule,
  SidebarModule,
  SliderModule,
  TabViewModule
} from "primeng/primeng";
import {AddBugComponent} from './mainPage/bugs-management/add-bug/add-bug.component';
import {GetBugsComponent} from './mainPage/bugs-management/get-bugs/get-bugs.component';
import {TableModule} from "primeng/table";
import {EditBugComponent} from './mainPage/bugs-management/edit-bug/edit-bug.component';
import {ToastModule} from "primeng/toast";
import {GetBugsModule} from "./mainPage/bugs-management/get-bugs/get-bugs.module";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    AddBugComponent,
    GetBugsComponent,
    EditBugComponent,

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
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
    GetBugsModule,
    SelectButtonModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
