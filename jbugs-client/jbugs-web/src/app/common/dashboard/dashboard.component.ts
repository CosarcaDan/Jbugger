import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {NotificationService} from "../../core/services/notification/notification.service";
import {LanguageService} from "../../core/services/language/language.service";
import {Notification} from "../../core/models/notification";
import {User} from "../../core/models/user";
import {Bug} from "../../core/models/bug";
import {formatDate} from "@angular/common";


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

  showNot(notification: Notification) {
    this.notifications.forEach(not => {
      not.show = not.id == notification.id;
    });
    notification.isSeen = true;
  }

  hide() {
    this.notifications.forEach(not => {
      not.show = false;
    })
  }


  notificationFormatter(notification: Notification): string {
    if (notification.type == "WELCOME_NEW_USER") {
      let user: User = JSON.parse(notification.message);
      return '<div>' + this.languageService.getText('welcome') + ' ' + user.firstName + ' ' + user.lastName + '! ' +
        this.languageService.getText('edit-pers-data') + `<a href="dashboard/profile" ">` + this.languageService.getText('here') + `</a>` +
        this.languageService.getText('edit-pers-data-2') +
        '</div><div>' +
        this.languageService.getText('firstName') + ': ' + user.firstName + '</div><div>' +
        this.languageService.getText('lastName') + ': ' + user.lastName + '</div><div>' +
        'Email: ' + user.email + '</div><div>' +
        this.languageService.getText('phoneNumber') + ': ' + user.mobileNumber + '</div><div>' +
        this.languageService.getText('username') + ': ' + user.username + '</div><div>' +
        'Status: ' + (localStorage.getItem('language') == 'en' ? user.status ? 'active' : 'inactive' : user.status ? 'activ' : 'inactiv') + '</div>';
    }
    if (notification.type == 'USER_DELETED' || notification.type == 'USER_DEACTIVATED') {
      let user: User = JSON.parse(notification.message);
      return '<div>' + this.languageService.getText('user_deleted') +
        '</div><div>' +
        this.languageService.getText('firstName') + ': ' + user.firstName + '</div><div>' +
        this.languageService.getText('lastName') + ': ' + user.lastName + '</div><div>' +
        'Email: ' + user.email + '</div><div>' +
        this.languageService.getText('phoneNumber') + ': ' + user.mobileNumber + '</div><div>' +
        this.languageService.getText('username') + ': ' + user.username + '</div><div>' +
        'Status: ' + (localStorage.getItem('language') == 'en' ? user.status ? 'active' : 'inactive' : user.status ? 'activ' : 'inactiv') + '</div>';
    }
    if (notification.type == 'USER_UPDATED') {
      let user: User[] = JSON.parse(notification.message);
      return '<div>' + this.languageService.getText('user_updated') + '<p><br></p></div>' +
        '<div>' + this.languageService.getText('user_updated_new') + '</div><div>' +
        this.languageService.getText('firstName') + ': ' + user[0].firstName + '</div><div>' +
        this.languageService.getText('lastName') + ': ' + user[0].lastName + '</div><div>' +
        'Email: ' + user[0].email + '</div><div>' +
        this.languageService.getText('phoneNumber') + ': ' + user[0].mobileNumber + '</div><div>' +
        this.languageService.getText('username') + ': ' + user[0].username + '</div><div>' +
        'Status: ' + (user[0].status ? 'active' : 'inactive') + '<p><br></p></div></div>' +

        '<div>' + this.languageService.getText('user_updated_old') + '</div><div>' +
        this.languageService.getText('firstName') + ': ' + user[1].firstName + '</div><div>' +
        this.languageService.getText('lastName') + ': ' + user[1].lastName + '</div><div>' +
        'Email: ' + user[0].email + '</div><div>' +
        this.languageService.getText('phoneNumber') + ': ' + user[1].mobileNumber + '</div><div>' +
        this.languageService.getText('username') + ': ' + user[1].username + '</div><div>' +
        'Status: ' + (user[0].status ? 'active' : 'inactive') + '<br></div>';
    }
    if (notification.type == "BUG_CLOSED") {
      let bug: Bug = JSON.parse(notification.message);
      let res: string = '<div>' + this.languageService.getText('closedBug') + '</div><div>' +
        this.languageService.getText('bug-details') + ': <div></div>' +
        this.languageService.getText('title') + ' : ' + bug.title + '</div><div>' +
        this.languageService.getText('description') + ' : ' + bug.description.substr(0, 20) + ' ...</div><div>' +
        this.languageService.getText('version') + ' : ' + bug.version + '</div><div>' +
        this.languageService.getText('fixedVersion') + ' : ' + bug.fixedVersion + '</div><div>' +
        this.languageService.getText('severity') + ' : ' + bug.severity + '</div><div>' +
        'Status: ' + ' : ' + bug.status + '</div><div>' +
        this.languageService.getText('targetDate') + ' : ' + formatDate(bug.targetDate, 'dd.MM.yyyy', 'en') + '</div><div>' +
        this.languageService.getText('createdBy') + ' : ' + bug.created + '</div><div>' +
        this.languageService.getText('assignedTo') + ' : ' + bug.assigned + '</div><div>' + '</div>';
      return res;
    }
    if (notification.type == "BUG_UPDATED") {
      let bugs: Bug[] = JSON.parse(notification.message);
      let res: string = '<div>' + this.languageService.getText('bugUpdated') + '</div><div>' +
        this.languageService.getText('oldBugData') + ': <div></div>' +
        this.languageService.getText('title') + ' : ' + bugs[0].title + '</div><div>' +
        this.languageService.getText('description') + ' : ' + bugs[0].description.substr(0, 20) + ' ...</div><div>' +
        this.languageService.getText('version') + ' : ' + bugs[0].version + '</div><div>' +
        this.languageService.getText('fixedVersion') + ' : ' + bugs[0].fixedVersion + '</div><div>' +
        this.languageService.getText('severity') + ' : ' + bugs[0].severity + '</div><div>' +
        'Status: ' + ' : ' + bugs[0].status + '</div><div>' +
        this.languageService.getText('targetDate') + ' : ' + formatDate(bugs[0].targetDate, 'dd.MM.yyyy', 'en') + '</div><div>' +
        this.languageService.getText('createdBy') + ' : ' + bugs[0].created + '</div><div>' +
        this.languageService.getText('assignedTo') + ' : ' + bugs[0].assigned + '</div><div>' + '<div></div><br>' +

        this.languageService.getText('newBugData') + ': <div></div>' +
        this.languageService.getText('title') + ' : ' + bugs[1].title + '</div><div>' +
        this.languageService.getText('description') + ' : ' + bugs[1].description.substr(0, 20) + ' ...</div><div>' +
        this.languageService.getText('version') + ' : ' + bugs[1].version + '</div><div>' +
        this.languageService.getText('fixedVersion') + ' : ' + bugs[1].fixedVersion + '</div><div>' +
        this.languageService.getText('severity') + ' : ' + bugs[1].severity + '</div><div>' +
        'Status: ' + ' : ' + bugs[1].status + '</div><div>' +
        this.languageService.getText('targetDate') + ' : ' + formatDate(bugs[1].targetDate, 'dd.MM.yyyy', 'en') + '</div><div>' +
        this.languageService.getText('createdBy') + ' : ' + bugs[1].created + '</div><div>' +
        this.languageService.getText('assignedTo') + ' : ' + bugs[1].assigned + '</div><div>' + '</div>';
      return res;

    }
    if (notification.type == "BUG_STATUS_UPDATED") {
      let bugs: Bug[] = JSON.parse(notification.message);
      let res: string = '<div>' + this.languageService.getText('bugStatusUpdate') + '</div><div>' +
        this.languageService.getText('oldBugStatus') + ': <div></div>' +
        this.languageService.getText('title') + ' : ' + bugs[0].title + '</div><div>' +
        'Status: ' + ' : ' + bugs[0].status + '</div><div><br>' +
        this.languageService.getText('newBugStatus') + ': <div></div>' +
        this.languageService.getText('title') + ' : ' + bugs[1].title + '</div><div>' +

        'Status: ' + ' : ' + bugs[1].status + '</div><div>' + '</div>';
      return res;

    }
  }
}
