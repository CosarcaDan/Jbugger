import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './mainPage/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {HttpClientModule} from '@angular/common/http';
import {MenubarModule} from "primeng/menubar";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ChartModule} from "primeng/chart";
import {TabViewModule} from "primeng/primeng";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MenubarModule,
    ChartModule,
    TabViewModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
