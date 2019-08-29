import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {NotificationService} from "../../core/services/notification/notification.service";
import {LanguageService} from "../../core/services/language/language.service";
import {Notification} from "../../core/models/notification";
import {not} from "rxjs/internal-compatibility";
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
        this.languageService.getText('edit-pers-data-2')+
        '</div><div>'+
        "ID:"+user.id+'</div><div>'+
        this.languageService.getText('counter')+': '+user.failedLoginAttempt+'</div><div>'+
        this.languageService.getText('firstName')+': '+user.firstName+'</div><div>'+
        this.languageService.getText('lastName')+': '+user.lastName+'</div><div>'+
        'Email: '+user.email+'</div><div>'+
        this.languageService.getText('phoneNumber')+': '+user.mobileNumber+'</div><div>'+
        this.languageService.getText('username')+': '+user.username+'</div><div>'+
        'Status: '+( user.status? 'active':'inactive')+'</div>'
      return res;
    }


    if(notification.type == "BUG_CLOSED")
    {
      let bug: Bug = JSON.parse(notification.message)
      let res:string ='<div>'+this.languageService.getText('closedBug')+'</div><div>'+
        this.languageService.getText('bug-details') + ': <div></div>'+
        this.languageService.getText('title') + ' : '+bug.title+'</div><div>'+
        this.languageService.getText('description') +' : '+bug.description.substr(0,20)+' ...</div><div>'+
        this.languageService.getText('version') +' : '+bug.version+'</div><div>'+
        this.languageService.getText('fixedVersion') +' : '+bug.fixedVersion+'</div><div>'+
        this.languageService.getText('severity') +' : '+bug.severity+'</div><div>'+
        'Status: ' +' : '+bug.status+'</div><div>'+
        this.languageService.getText('targetDate') +' : '+ formatDate(bug.targetDate,'dd.MM.yyyy','en')+'</div><div>'+
        this.languageService.getText('createdBy') +' : '+bug.created+'</div><div>'+
        this.languageService.getText('assignedTo') +' : '+bug.assigned+'</div><div>'+'</div>';
      return res;
    }
  }
}
