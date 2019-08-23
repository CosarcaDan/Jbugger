import {Component, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  items: MenuItem[];
  display;
  countries: any[];
  selectedCountry: any;

  constructor() {
  }

  ngOnInit() {
    this.items = [
      {
        label: 'User management',
        icon: 'pi pi-fw pi-user',
        items: [{
          label: 'Add',
          icon: 'pi pi-fw pi-user-plus',
        },
          {
            label: 'View all',
            icon: 'pi pi-fw pi-users'
          },
        ]
      },
      {
        label: 'Bug management',
        icon: 'pi pi-fw pi pi-th-large',
      },
      {
        label: 'Permission management',
        icon: 'pi pi-fw pi-star',
        items: [
          {
            label: 'Add permission',
            icon: 'pi pi-fw pi-plus'
          },
          {
            label: 'Delete permission',
            icon: 'pi pi-fw pi-minus'
          }
        ]
      }
    ];

    this.countries = [
      {label: 'Romanian', value: 'Romanian', icon: 'fa fa-fw '},
      {label: 'English', value: 'English', icon: 'fa fa-fw'},
    ];
  }
}
