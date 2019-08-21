import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './mainPage/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AddUserComponent} from './user-management/add-user/add-user.component';
import {AuthInterceptor} from './mainPage/interceptors/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    AddUserComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
