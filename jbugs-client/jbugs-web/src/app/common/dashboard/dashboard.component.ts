import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {NotificationService} from "../../core/services/notification/notification.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  items: MenuItem[];
  display;
  languages: any[];
  selectedLanguage: any;
  user=this.authService.getUsername();

  interval;


  notifications: Notification[];

  constructor(private router: Router, private authService: AuthService, private notificationService: NotificationService) {
  }

  ngOnInit() {
    this.items = [
      {
        label: 'User management',
        icon: 'pi pi-fw pi-users',
        command: () => this.goto('dashboard/users'),
        visible: this.authService.hasPermission('USER_MANAGEMENT')
      },
      {
        label: 'Bug management',
        icon: 'pi pi-fw pi pi-th-large',
        command: () => this.goto('dashboard/bugs'),
        visible: this.authService.hasPermission('BUG_MANAGEMENT')
      },
      {
        label: 'Permission management',
        icon: 'pi pi-fw pi-key',
        visible: this.authService.hasPermission('PERMISSION_MANAGEMENT'),
        command: () => this.goto('dashboard/permissions')
      }
    ];

    this.languages = [
      {label: 'Romanian', value: 'Romanian', icon: 'flag-icon flag-icon-ro'},
      {label: 'English', value: 'English', icon: 'flag-icon flag-icon-gb'},
    ];


    this.interval = setInterval(this.getMyNotification.bind(this),10000);

  }

  public goto(link) {
    this.router.navigate([link]);
  }

  logout() {
    clearInterval(this.interval);
    this.authService.logout();
  }

  getMyNotification(){
    console.log('UsernameFrorNotification',this.user);
    this.notificationService.getMyNotification(this.user).subscribe((data)=>{
      this.notifications = data;
      console.log(this.notifications)
    })
  }
}
