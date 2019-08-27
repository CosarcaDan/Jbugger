import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
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
  selectedLanguage= sessionStorage.getItem('language')=='en'?'English':'Romanian' ;

  constructor(private router: Router, private authService: AuthService, private labelService:LanguageService) {
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
  }

  public goto(link) {
    this.router.navigate([link]);
  }

  logout() {
    this.authService.logout();
  }

  setLanguage(language){
    console.log(language);
    if(language == 'Romanian')
      sessionStorage.setItem('language','ro');
    if(language == 'English')
      sessionStorage.setItem('language','en');

    this.items[0].label=this.labelService.getText('user-man');
    this.items[1].label=this.labelService.getText('bug-man');
    this.items[2].label=this.labelService.getText('perm-man');
  }

}
