import {Component, OnInit} from '@angular/core';
import {SelectItem} from 'primeng/api';
import {Router} from '@angular/router';
import {BugServiceService} from '../../service/bug/bug-service.service';
import {Bug} from '../../models/bug';

@Component({
  selector: 'app-get-bugs',
  templateUrl: './get-bugs.component.html',
  styleUrls: ['./get-bugs.component.css'],
  providers: [BugServiceService],

})
export class GetBugsComponent implements OnInit {

  cols: any[];

  status: SelectItem[];

  statusNew: SelectItem[];

  statusInProgress: SelectItem[];

  statusInfoNeeded: SelectItem[];

  statusRejected: SelectItem[];

  statusFixed: SelectItem[];

  severity: SelectItem[];

  bugs: Bug[];

  newBug: boolean;

  loadComponent: Boolean;



  bugSearchCrit: Bug = {
    id: 0,
    title: '',
    description: '',
    version: '',
    targetDate: new Date(),
    status: '',
    fixedVersion: '',
    severity: '',
    created: '',
    assigned: ''
  };

  displayDialog: boolean;

  bug: Bug = {
    id: 0,
    title: '',
    description: '',
    version: '',
    targetDate: new Date(),
    status: '',
    fixedVersion: '',
    severity: '',
    created: '',
    assigned: ''
  };
  selectedBug: Bug;

  constructor(private router: Router, private bugServices: BugServiceService) {

  }

  ngOnInit() {
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
      // {field: 'button', header: ''}
    ];

    this.status = [
      {label: 'All Statuses', value: ''},
      {label: 'New', value: 'NEW'},
      {label: 'In progress', value: 'IN_PROGRESS'},
      {label: 'Fixed', value: 'FIXED'},
      {label: 'Closed', value: 'CLOSED'},
      {label: 'Rejected', value: 'REJECTED'},
      {label: 'Info Needed', value: 'INFONEEDED'}
    ];

    this.statusNew = [
      {label: 'New', value: 'NEW'},
      {label: 'In progress', value: 'IN_PROGRESS'},
      {label: 'Rejected', value: 'REJECTED'}
    ];

    this.statusInProgress = [
      {label: 'In progress', value: 'IN_PROGRESS'},
      {label: 'Fixed', value: 'FIXED'},
      {label: 'Rejected', value: 'REJECTED'},
      {label: 'Info Needed', value: 'INFONEEDED'}
    ];

    this.statusInfoNeeded = [
      {label: 'In progress', value: 'IN_PROGRESS'},
      {label: 'Info Needed', value: 'INFONEEDED'}
    ];

    this.statusRejected = [
      {label: 'Rejected', value: 'REJECTED'},
    ];

    this.statusFixed = [
      {label: 'Fixed', value: 'FIXED'},
    ];



    this.severity = [
      {label: 'All Severities', value: ''},
      {label: 'LOW', value: 'LOW'},
      {label: 'MEDIUM', value: 'MEDIUM'},
      {label: 'HIGH', value: 'HIGH'},
      {label: 'CRITICAL', value: 'CRITICAL'},
    ]
  }

  getBugs() {
    this.bugs = [];
    this.bugServices.getBugs().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.bugs = data;

      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    });
  }


  public search() {
    console.log(this.bugSearchCrit);
    this.bugServices.getBugsAfterSearchCriteria(this.bugSearchCrit).subscribe((data: {}) => {
      // @ts-ignore
      this.bugs = data;

      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    })
  }

  add() {
    this.loadComponent = true;
  }

  export() {
    this.bugServices.exportInPdf(this.bug).subscribe(s => {
      window.open(s.toString(), '_self');
    })
  }

  delete(id: number) {
    console.log('deleted' + id);
    this.bugServices.deleteBugAfterId(id).subscribe(
      (data: {}) => {
        alert('Bug closed Complete');
      },
      (error1 => {
        console.log('Error', error1);
        alert('update failed :' + error1.error.detailMessage);
      }));
    this.displayDialog = false;
    this.search();
  }

  save() {
    console.log('saved');
    this.bugServices.saveEditBug(this.bug).subscribe(
      (data: {}) => {
        alert('Edit Bugs Complete');
      },
      (error2 => {
        console.log('Error', error2);
        alert('update failed :' + error2.error.detailMessage);
      })
    );

    this.displayDialog = false;
    this.search();
  }

  onRowSelect(event) {
    this.newBug = false;
    this.bug = this.cloneBug(event.data);
    this.displayDialog = true;
  }

  private cloneBug(b: Bug): Bug {
    let bug = {
      id: 0,
      title: '',
      description: '',
      version: '',
      targetDate: new Date(),
      status: '',
      fixedVersion: '',
      severity: '',
      created: '',
      assigned: ''
    };
    for (let props in b) {
      bug[props] = b[props];
    }
    return bug;
  }
}
