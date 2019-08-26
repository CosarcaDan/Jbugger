import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './common/login/login.component';
import {DashboardComponent} from './common/dashboard/dashboard.component';
import {AddBugComponent} from './feature/bugs-management/add-bug/add-bug.component';
import {BugsListComponent} from './feature/bugs-management/bugs-list/bugs-list.component';
import {UserListComponent} from './feature/user-management/user-list/user-list.component';
import {AddPermissionComponent} from './feature/permission-management/add-permission/add-permission.component';
import {RemovePermissionComponent} from './feature/permission-management/remove-permission/remove-permission.component';
import {AuthGuardService} from './core/services/guards/auth-guard.service';


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
    canActivate: [AuthGuardService],
    children: [
      {
        path: 'bugs/add',
        component: AddBugComponent
      },
      {
        path: 'bugs',
        component: BugsListComponent
      },
      {
        path: 'bugsAdd',
        component: AddBugComponent
      },
      {
        path: 'users',
        component: UserListComponent
      },
      {
        path: 'permissions/add',
        component: AddPermissionComponent
      },
      {
        path: 'permissions/remove',
        component: RemovePermissionComponent
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
