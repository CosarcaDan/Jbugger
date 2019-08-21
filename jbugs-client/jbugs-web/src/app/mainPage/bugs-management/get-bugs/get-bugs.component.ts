import {Component, OnInit} from '@angular/core';
import {Interface} from "../../models/bug";
import {SelectItem} from "primeng/api";

@Component({
  selector: 'app-get-bugs',
  templateUrl: './get-bugs.component.html',
  styleUrls: ['./get-bugs.component.css']
})
export class GetBugsComponent implements OnInit {

  bugs: Interface[];

  cols: any[];

  status: SelectItem[];

  yearFilter: number;

  yearTimeout: any;

  constructor() {
  }

  ngOnInit() {

    this.bugs = [
      {
        id: 1,
        title: 'Bug1',
        description: 'I am a bug',
        version: '1.0',
        targetDate: new Date("2019-08-25"),
        status: 'in progress',
        fixedVersion: '1.1',
        severity: 'low',
        created: 'Mihai',
        assigned: 'Gabriela'
      },
      {
        id: 2,
        title: 'Bug2',
        description: 'I am a bug',
        version: '2.1',
        targetDate: new Date("2019-08-30"),
        status: 'finished',
        fixedVersion: '2.3',
        severity: 'high',
        created: 'Mihaela',
        assigned: 'Gabi'
      },
      {
        id: 3,
        title: 'Bug3',
        description: 'I am a bug',
        version: '1.5',
        targetDate: new Date("2019-09-25"),
        status: 'in progress',
        fixedVersion: '1.6',
        severity: 'low',
        created: 'Mihai',
        assigned: 'Gabriela'
      }
    ];

    this.cols = [
      {field: 'title', header: 'Title'},
      {field: 'description', header: 'Description'},
      {field: 'version', header: 'Version'},
      {field: 'targetDate', header: 'Target Date'},
      {field: 'status', header: 'Status'},
      {field: 'fixedVersion', header: 'Fixed Version'},
      {field: 'severity', header: 'Severity'},
      {field: 'created', header: 'Created by'},
      {field: 'assigned', header: 'Assigned to'}
    ];

    this.status = [
      {label: 'All Statuses', value: null},
      {label: 'New', value: 'new'},
      {label: 'In progress', value: 'in progress'},
      {label: 'Fixed', value: 'fixed'},
      {label: 'Closed', value: 'closed'},
      {label: 'Rejected', value: 'rejected'},
      {label: 'Info Needed', value: 'info needed'}
    ];
  }

  onYearChange(event, dt) {
    if (this.yearTimeout) {
      clearTimeout(this.yearTimeout);
    }

    this.yearTimeout = setTimeout(() => {
      dt.filter(event.value, 'year', 'gt');
    }, 250);
  }
}
