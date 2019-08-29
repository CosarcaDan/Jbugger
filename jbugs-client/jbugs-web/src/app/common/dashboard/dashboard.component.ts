import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {NotificationService} from '../../core/services/notification/notification.service';
import {LanguageService} from '../../core/services/language/language.service';
import {Notification} from '../../core/models/notification';
import {User} from '../../core/models/user';

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
  notificationFormatter(notification:Notification):string{
    if(notification.type == "WELCOME_NEW_USER")
    {
      //"{"id":8,"failedLoginAttempt":0,"firstName":"Test","lastName":"Felh","email":"testfh@msggroup.com","mobileNumber":"0740541012","password":"f9b8e085ea3877ca8e8074aaa0c21ac9a9808275d835d680a6a9b530e2f7f27c","username":"felht","status":true}"
      let user: User = JSON.parse(notification.message)
      let res:string ='<div>'+this.languageService.getText('welcome')+' '+user.firstName+' '+user.lastName+'! '+
        this.languageService.getText('edit-pers-data')+`<a href="dashboard/profile" ">`+this.languageService.getText('here')+`</a>`+
        this.languageService.getText('firstName')+': '+user.firstName+'</div><div>'+
        this.languageService.getText('lastName')+': '+user.lastName+'</div><div>'+
        'Email: '+user.email+'</div><div>'+
        this.languageService.getText('phoneNumber')+': '+user.mobileNumber+'</div><div>'+
        this.languageService.getText('username')+': '+user.username+'</div><div>'+
        'Status: '+( user.status? 'active':'inactive')+'</div>'
      console.log(res);
      return res;
    }
    if (notification.type == 'USER_DELETED' || notification.type == 'USER_DEACTIVATED') {
      let user: User = JSON.parse(notification.message);
      let res: string = '<div>' + this.languageService.getText('user_deleted') +
        this.languageService.getText('firstName') + ': ' + user.firstName + '</div><div>' +
        this.languageService.getText('lastName') + ': ' + user.lastName + '</div><div>' +
        'Email: ' + user.email + '</div><div>' +
        this.languageService.getText('phoneNumber') + ': ' + user.mobileNumber + '</div><div>' +
        this.languageService.getText('username') + ': ' + user.username + '</div><div>' +
        'Status: ' + (user.status ? 'active' : 'inactive') + '</div>';
      console.log(res);
      return res;
    }
    if (notification.type == 'USER_UPDATED') {
      let user: User[] = JSON.parse(notification.message);
      let res: string = '<div>' + this.languageService.getText('user_updated') + '<p><br></p></div>' +
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
        'Status: ' + (user[0].status ? 'active' : 'inactive') + '<br></div>'
      ;
      return res;
    }
  }
}
