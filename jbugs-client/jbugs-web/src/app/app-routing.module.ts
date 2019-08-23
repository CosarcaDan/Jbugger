import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './mainPage/login/login.component';
import {DashboardComponent} from './mainPage/dashboard/dashboard.component';
import {AddBugComponent} from './mainPage/bugs-management/add-bug/add-bug.component';
import {GetBugsComponent} from './mainPage/bugs-management/get-bugs/get-bugs.component';
import {GetUserComponent} from './mainPage/user-management/get-user/get-user.component';
import {AddPermissionComponent} from './mainPage/permission-management/add-permission/add-permission.component';


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
        component: AddBugComponent
      },
      {
        path: 'bugs',
        component: GetBugsComponent
      },
      {
        path: 'users',
        component: GetUserComponent
      },
      {
        path: 'permissions/add',
        component: AddPermissionComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

