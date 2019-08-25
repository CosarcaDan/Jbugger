import {Component, OnInit} from '@angular/core';
import {MenuItem} from "primeng/api";
import {Router, RouterModule} from "@angular/router";
import {AuthService} from "../../core/services/auth/auth.service";

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

  constructor(private router:Router,private authService:AuthService) {
  }

  ngOnInit() {
    this.items = [
      {
        label: 'User management',
        icon: 'pi pi-fw pi-users',
        command: () => this.goto('dashboard/users'),
        visible:this.authService.hasPermission("USER_MANAGEMENT")
      },
      {
        label: 'Bug management',
        icon: 'pi pi-fw pi pi-th-large',
        command: () => this.goto('dashboard/bugs'),
        visible:this.authService.hasPermission("BUG_MANAGEMENT")
      },
      {
        label: 'Permission management',
        icon: 'pi pi-fw pi-star',
        visible:this.authService.hasPermission("PERMISSION_MANAGEMENT"),
        items: [
          {
            label: 'Add permission',
            icon: 'pi pi-fw pi-plus',
            command: () => this.goto('dashboard/permissions/add')
          },
          {
            label: 'Delete permission',
            icon: 'pi pi-fw pi-minus',
            command: () => this.goto('dashboard/permissions/remove')
          }
        ]
      }
    ];

    this.countries = [
      {label: 'Romanian', value: 'Romanian', icon: 'fa fa-fw '},
      {label: 'English', value: 'English', icon: 'fa fa-fw'},
    ];
  }
  public goto(link)
  {
    this.router.navigate([link]);
  }

  logout() {
    this.authService.logout();
  }
}