import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {NotificationService} from "../../core/services/notification/notification.service";
import {LanguageService} from "../../core/services/language/language.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  items: MenuItem[];
  display;
  languages: any[];
  selectedLanguage = localStorage.getItem('language') == 'en' ? 'English' : 'Romanian';

  user = this.authService.getUsername();
  displayNotification: boolean;

  interval;

  notifications: Notification[];

  intervalRun: boolean;

  constructor(private router: Router, private authService: AuthService, private notificationService: NotificationService, private languageService: LanguageService) {
  }

  ngOnInit() {
    this.items = [
      {
        label: this.selectedLanguage == 'English' ? 'User management' : 'Administrarea utilizatorilor',
        icon: 'pi pi-fw pi-users',
        command: () => this.goto('dashboard/users'),
        visible: this.authService.hasPermission('USER_MANAGEMENT')
      },
      {
        label: this.selectedLanguage == 'English' ? 'Bug management' : 'Administrarea bugurilor',
        icon: 'pi pi-fw pi pi-th-large',
        command: () => this.goto('dashboard/bugs'),
        visible: this.authService.hasPermission('BUG_MANAGEMENT')
      },
      {
        label: this.selectedLanguage == 'English' ? 'Permission management' : 'Administrarea permisiunilor  ',
        icon: 'pi pi-fw pi-key',
        visible: this.authService.hasPermission('PERMISSION_MANAGEMENT'),
        command: () => this.goto('dashboard/permissions')
      }
    ];

    this.languages = [
      {label: 'Română', value: 'Romanian', icon: 'flag-icon flag-icon-ro'},
      {label: 'English', value: 'English', icon: 'flag-icon flag-icon-gb'},
    ];


    this.interval = setInterval(this.getMyNotification.bind(this), 3000);

    this.intervalRun = true;

  }

  public goto(link) {
    this.router.navigate([link]);
  }

  logout() {
    clearInterval(this.interval);
    this.authService.logout();
  }

  getMyNotification() {
    console.log('UsernameFromNotification', this.user);
    this.notificationService.getMyNotification(this.user).subscribe((data) => {
      this.notifications = data;
      console.log(this.notifications)
    })
  }

  setLanguage(language) {
    console.log(language);
    if (language == 'Romanian')
      localStorage.setItem('language', 'ro');
    if (language == 'English')
      localStorage.setItem('language', 'en');

    this.items[0].label = this.languageService.getText('user-man');
    this.items[1].label = this.languageService.getText('bug-man');
    this.items[2].label = this.languageService.getText('perm-man');

    location.reload();
  }

  notificationDialogClearIntervalWhenVisible() {
    clearInterval(this.interval);
    this.intervalRun = false;
  }

  notificationDialogStartIntervalWhenVisible() {
    if (!this.intervalRun) {
      this.interval = setInterval(this.getMyNotification.bind(this), 3000);
      this.intervalRun = true;
    }
  }

  notificationClicked(not) {
    not.isSeen = true;
    this.displayNotification = true;
  }
}
