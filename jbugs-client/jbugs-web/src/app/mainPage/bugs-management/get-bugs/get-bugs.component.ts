import {Component, OnInit} from '@angular/core';
import {DialogService, SelectItem} from "primeng/api";
import {Router} from "@angular/router";
import {BugServiceService} from "../../service/bug/bug-service.service";
import {Bug} from "../../models/bug";
import {EditBugComponent} from "../edit-bug/edit-bug.component";

@Component({
  selector: 'app-get-bugs',
  templateUrl: './get-bugs.component.html',
  styleUrls: ['./get-bugs.component.css'],
  providers: [BugServiceService, DialogService],

})
export class GetBugsComponent implements OnInit {

  cols: any[];

  status: SelectItem[];

  severity: SelectItem[];

  yearTimeout: any;

  bugs: Bug[];

  constructor(private router: Router, private bugServices: BugServiceService, public dialogService: DialogService) {
  }

  ngOnInit() {
    this.getBugs()
  }

  getBugs() {
    this.bugs = [];
    this.bugServices.getBugs().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.bugs = data;

      for (var bug of this.bugs){
        var date = new Date(bug.targetDate);
        bug.targetDate = date
      }
    });

    this.cols = [
      {field: 'title', header: 'Title'},
      {field: 'description', header: 'Description'},
      {field: 'version', header: 'Version'},
      {field: 'targetDate', header: 'Target Date'},
      {field: 'status', header: 'Status'},
      {field: 'fixedVersion', header: 'Fixed Version'},
      {field: 'severity', header: 'Severity'},
      {field: 'created', header: 'Created by'},
      {field: 'assigned', header: 'Assigned to'},
      {field: 'button', header: ''}
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

    this.severity = [
      {label: 'All Severities', value: null},
      {label: 'LOW', value: 'LOW'},
      {label: 'MEDIUM', value: 'MEDIUM'},
      {label: 'HIGH', value: 'HIGH'},
      {label: 'CRITICAL', value: 'CRITICAL'},
    ]
  }

  onYearChange(event, dt) {
    if (this.yearTimeout) {
      clearTimeout(this.yearTimeout);
    }

    this.yearTimeout = setTimeout(() => {
      dt.filter(event.value, 'year', 'gt');
    }, 250);
  }

  show() {
    this.dialogService.open(EditBugComponent, {
      header: 'Edit a bug',
      width: '70%'
    });
  }
}
