import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SelectItem} from 'primeng/api';
import {Router} from '@angular/router';
import {BugService} from '../../../core/services/bug/bug.service';
import {Bug} from '../../../core/models/bug';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddBugComponent} from '../add-bug/add-bug.component';
import {AuthService} from '../../../core/services/auth/auth.service';
import {Attachment} from '../../../core/models/attachment';
import {FileService} from '../../../core/services/file/file.service';
import {User} from '../../../core/models/user';
import {UserService} from '../../../core/services/user/user.service';
import {Table} from 'primeng/table';
import {DatePipe} from '@angular/common';
import {ExcelService} from '../../../core/services/excel/excel.service';
import {LanguageService} from '../../../core/services/language/language.service';
import {MessageComponent} from '../../../core/message/message.component';


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

  mappedUsers: SelectItem[];


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

  private temporatStatus: string;

  @ViewChild('dt', undefined)
  dt: Table;

  filteredBugs: any[];

  constructor(private router: Router, private bugServices: BugService, public modalService: NgbModal,
              private authService: AuthService, private fileService: FileService,
              private userService: UserService, private languageService: LanguageService, private excelService: ExcelService) {

  }

  ngOnInit() {
//setInterval(getNotifications,1000);    this.getUsers();
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');

    //setInterval(getNotifications,1000);

    this.getUsers();
    this.cols = [
      {field: 'title', header: this.languageService.getText('title')},
      {field: 'description', header: this.languageService.getText('description')},
      {field: 'version', header: this.languageService.getText('version')},
      {field: 'targetDate', header: this.languageService.getText('targetDate')},
      {field: 'status', header: 'Status'},
      {field: 'fixedVersion', header: this.languageService.getText('fixedVersion')},
      {field: 'severity', header: this.languageService.getText('severity')},
      {field: 'created', header: this.languageService.getText('createdBy')},
      {field: 'assigned', header: this.languageService.getText('assignedTo')},
      // {field: 'button', header: ''}
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
      if (filter === undefined || filter === null || (filter.length === 0 || filter === '') && value === null) {
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
      this.mappedUsers = this.allUsers.map(user => {
        return {label: user.firstName + ' ' + user.lastName + ' (' + user.username + ')', value: user.username};
        }
      );
    });
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
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-close-successful');
      },
      (error1 => {
        console.log('Error', error1);
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-close-failed') + this.languageService.getText(error1.error.errorCode);
      }));
    this.displayDialog = false;
    this.search();
  }

  save() {
    console.log(this.temporatStatus);
    this.bug.status = this.temporatStatus;
    let attachmentToBeAdded: Attachment = {
      id: null,
      attContent: this.uploadedFileName,
    };
    if (this.attachments != null)
      this.fileUpload();
    console.log('BUG TO BE SAVED', this.bug);
    this.bugServices.saveEditBug(this.bug, attachmentToBeAdded).subscribe(
      (data) => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-edit-successful');
        this.search();
      },
      (error2 => {
        console.log('Error', error2);
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-edit-failed') + this.languageService.getText(error2.error.errorCode);
      })
    );

    this.displayDialog = false;
    this.search();
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
    this.attachments = null;
    this.myAtt = null;
    this.uploadedFileName = '';
    if (this.fileInput.nativeElement != null)
      this.fileInput.nativeElement.value = '';
  }

  onRowSelect(event) {
    this.getUsers();
    this.getAttachments(event.data).toPromise().then(
      res => {
        this.currentAttachments = res;
        this.clearFile();
        this.newBug = false;
        this.bug = this.cloneBug(event.data);
        this.temporatStatus = this.bug.status;
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
    let fileSize = event.target.files[0].size / 1024 / 1024; // in MB
    if (fileSize > 25) {
      const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
      modalRef.componentInstance.message = this.languageService.getText('file-size');
    }
    if (event.target.files.length > 0) {
      let files = event.target.files;
      this.uploadedFileName = files[0].name;
      this.myAtt = files;
    }
  }

  getAttachments(bug: Bug) {
    return this.bugServices.getAttachments(bug);
  }


  filterBugs(event) {
    this.userService.getUsers().subscribe((users) => {
      console.log(users);
      this.filteredBugs = users.map((u) => u.username).filter((u) => u.toLowerCase().indexOf(event.query.toLowerCase()) == 0);
      console.log(this.filteredBugs);
    });
  }


  exportAsXLSX() {
    if (this.dt.hasFilter()) {
      this.excelService.exportAsExcelFile(this.dt.filteredValue, 'bugs');
    } else {
      this.excelService.exportAsExcelFile(this.dt.value, 'bugs');
    }
  }

  deleteAttachment(id: number) {
    this.bugServices.deleteAttachments(this.bug,id).subscribe();
    this.getAttachments(this.bug).subscribe(
      res => {
        this.currentAttachments = res;
      }
    )
  }
}
