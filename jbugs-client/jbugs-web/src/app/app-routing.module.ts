import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './mainPage/login/login.component';
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {AddBugComponent} from './mainPage/bugs-management/add-bug/add-bug.component';


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
    path: 'dashboard',
    component: DashboardComponent,
    children: [{
      path: '',
      redirectTo: 'bugsAdd',
      pathMatch: 'full',
    },
      {
        path: 'bugsAdd',
        component: AddBugComponent,
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
