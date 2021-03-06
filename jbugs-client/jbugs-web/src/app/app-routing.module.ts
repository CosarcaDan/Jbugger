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
import {PermissionManagementComponent} from './feature/permission-management/permission-management/permission-management.component';
import {ProfileComponent} from './feature/profile/profile.component';
import {ViewBugComponent} from './feature/bugs-management/view-bug/view-bug.component';


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
        path: 'bugs',
        component: BugsListComponent
      },
      {
        path: 'users',
        component: UserListComponent
      },
      {
        path: 'permissions',
        component: PermissionManagementComponent
      },
      {
        path: 'profile',
        component: ProfileComponent
      },
      {
        path: 'bugs/:id',
        component: ViewBugComponent
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
