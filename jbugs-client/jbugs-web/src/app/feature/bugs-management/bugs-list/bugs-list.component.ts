import {Component, OnInit, ViewChild} from '@angular/core';
import {SelectItem} from 'primeng/api';
import {Router} from '@angular/router';
import {BugServiceService} from '../../../core/services/bug/bug-service.service';
import {Bug} from '../../../core/models/bug';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddBugComponent} from "../add-bug/add-bug.component";
import {AuthService} from "../../../core/services/auth/auth.service";
import {Table} from "primeng/table";
import {DatePipe} from "@angular/common";
import {UserService} from "../../../core/services/user/user.service";

@Component({
  selector: 'app-get-bugs',
  templateUrl: './bugs-list.component.html',
  styleUrls: ['./bugs-list.component.css'],
  providers: [BugServiceService],

})

export class BugsListComponent implements OnInit {

  cols: any[];

  status: SelectItem[];

  statusNew: SelectItem[];

  statusInProgress: SelectItem[];

  statusInfoNeeded: SelectItem[];

  statusRejected: SelectItem[];

  statusFixed: SelectItem[];

  severity: SelectItem[];

  bugs: Array<Bug>;

  newBug: boolean;


  bugSearchCriteria: Bug = {
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


  @ViewChild('dt', undefined)
  dt: Table;

  filteredBugs: any[];

  constructor(private router: Router, private bugServices: BugServiceService, public modalService: NgbModal,
              private authService: AuthService, private  userService: UserService) {

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
    ];

    this.status = [
      {label: 'NEW', value: 'NEW'},
      {label: 'IN_PROGRESS', value: 'IN_PROGRESS'},
      {label: 'FIXED', value: 'FIXED'},
      {label: 'CLOSED', value: 'CLOSED'},
      {label: 'REJECTED', value: 'REJECTED'},
      {label: 'INFO_NEEDED', value: 'INFONEEDED'}
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
      {label: 'LOW', value: 'LOW'},
      {label: 'MEDIUM', value: 'MEDIUM'},
      {label: 'HIGH', value: 'HIGH'},
      {label: 'CRITICAL', value: 'CRITICAL'},
    ];

    this.dt.filterConstraints['dateFilter'] = function inCollection(value: any, filter: any): boolean {
      if (filter === undefined || filter === null || (filter.length === 0 || filter === "") && value === null) {
        return true;
      }
      if (value === undefined || value === null || value.length === 0) {
        return false;
      }
      if (new DatePipe('en').transform(value, 'dd.MM.yyyy') == new DatePipe('en').transform(filter, 'dd.MM.yyyy')) {
        return true;
      }
      return false;
    }
  }

  getBugs() {
    this.bugs = [];
    this.bugServices.getBugs().subscribe((data) => {
      console.log(data);
      // @ts-ignore
      this.bugs = data;
      console.log(this.bugs);


      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    });
  }

  public search() {
    console.log(this.bugSearchCriteria);
    this.bugServices.getBugsAfterSearchCriteria(this.bugSearchCriteria).subscribe((data: {}) => {
      // @ts-ignore
      this.bugs = data;

      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    })
  }

  add() {
    const modalRef = this.modalService.open(AddBugComponent, {windowClass: 'add-pop'});
    modalRef.result.then(() => {
      this.search();
    });
  }

  export() {
    this.bugServices.exportInPdf(this.bug).subscribe(s => {
      window.open(s.toString(), '_self');
    })
  }

  delete(id: number) {
    console.log('deleted' + id);
    this.bugServices.deleteBugAfterId(id).subscribe(
      (data) => {
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
      (data) => {
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

  filterBugs(event) {
    this.userService.getUsers().subscribe((users) => {
      console.log(users);
      this.filteredBugs = users.map((u) => u.username).filter((u) => u.toLowerCase().indexOf(event.query.toLowerCase()) == 0);
      console.log(this.filteredBugs);
    });
  }
}
