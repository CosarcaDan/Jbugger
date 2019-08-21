import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./mainPage/login/login.component";
import {DashboardComponent} from "./mainPage/dashboard/dashboard.component";
import {GetBugsComponent} from "./mainPage/bugs-management/get-bugs/get-bugs.component";


const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: "dashboard",
    component: DashboardComponent,
    children: []
  },
  {
    path: "bugs",
    component: GetBugsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
