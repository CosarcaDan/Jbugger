import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SelectItem} from 'primeng/api';
import {Router} from '@angular/router';
import {BugService} from '../../../core/services/bug/bug.service';
import {Bug} from '../../../core/models/bug';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddBugComponent} from '../add-bug/add-bug.component';
import {AuthService} from '../../../core/services/auth/auth.service';
import {Attachment} from "../../../core/models/attachment";
import {FileService} from "../../../core/services/file/file.service";
import {User} from "../../../core/models/user";
import {UserService} from "../../../core/services/user/user.service";
import {Table} from "primeng/table";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-get-bugs',
  templateUrl: './bugs-list.component.html',
  styleUrls: ['./bugs-list.component.css'],
  providers: [BugService],

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

  @ViewChild('fileInput', {static: false}) fileInput: ElementRef;

  allUsers: Array<User>;


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
  private uploadedFileName: string;
  private myAtt;
  attachments;
  private currentAttachments: Array<Attachment>;

  @ViewChild('dt', undefined)
    dt: Table;
    filteredBugAssignedToSuggestion: any[];
    filteredBugs: Bug[];

    constructor(private router: Router, private bugServices: BugService, public modalService: NgbModal, private authService: AuthService, private fileService: FileService, private userService: UserService) {

    }

    ngOnInit() {
      this.getUsers();
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

  getUsers() {
    this.allUsers = new Array<User>();
    this.userService.getUsers().subscribe((data) => {
      console.log('data:', data);
      // @ts-ignore
      for (let dataKey of data) {
        this.allUsers.push(dataKey);
      }
    });
  }

  filterBugsAssignedTo(event) {
      this.filteredBugs = [];
      let bugs = this.getBugs();
      for (let i = 0; i < bugs.length; i++) {
        let bug = this.bugs[i];
        if (bug.assigned === event.query.toString()) {
          this.filteredBugs.push(bug);
        }
      }
    }

    getBugs(): Bug[] {
      this.bugs = [];
      this.bugServices.getBugs().subscribe((data) => {
        console.log(data);
      // @ts-ignore
        this.bugs = data;

        for (var bug of this.bugs) {
          var date = new Date(bug.targetDate);
          bug.targetDate = date;
        }
      });
      return this.bugs;
    }


  public search() {
    console.log(this.bugSearchCriteria);
    this.bugServices.getBugsAfterSearchCriteria(this.bugSearchCriteria).subscribe((data) => {

      this.bugs = data;

      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    })
  }

  add() {
    const modalRef = this.modalService.open(AddBugComponent, {windowClass: 'add-pop'});
    modalRef.result.then(()=>{this.search();});
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

  save(){
    console.log('saved');
    let attachmentToBeAdded: Attachment = {
      id: null,
      attContent: this.uploadedFileName,
    };
    if(this.attachments!=null)
      this.fileUpload();
    this.bugServices.saveEditBug(this.bug,attachmentToBeAdded).subscribe(
      (data) => {
        alert('Edit Bugs Complete');
        this.search();
      },
      (error2 => {
        console.log('Error', error2);
        alert('update failed :' + error2.error.detailMessage);
      })
    );

    this.displayDialog = false;
  }

  private prepareSave(): FormData {
    let input = new FormData();
    input.append('file', this.myAtt[0]);
    return input;
  }

  fileUpload() {
    const formModel = this.prepareSave();
    console.log(formModel.get('file'));
    this.fileService.uploadFile(formModel).subscribe(this.clearFile);
  }

  clearFile() {
    this.attachments=null;
    this.myAtt=null;
    this.uploadedFileName='';
    if(this.fileInput.nativeElement !=null )
      this.fileInput.nativeElement.value = '';
  }

  onRowSelect(event) {
    this.getAttachments(event.data).toPromise().then(
      res=>{
        this.currentAttachments=res;
      this.clearFile();
      this.newBug = false;
      this.bug = this.cloneBug(event.data);
      this.displayDialog = true;
    }
    )

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

  onFileChange(event) {
    if (event.target.files.length > 0) {
      let files = event.target.files;
      this.uploadedFileName = files[0].name;
      this.myAtt = files;
    }
  }

  getAttachments(bug:Bug){
    return this.bugServices.getAttachments(bug);
  }

}
