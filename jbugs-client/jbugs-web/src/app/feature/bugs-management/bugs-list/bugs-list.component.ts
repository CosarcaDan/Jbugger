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

  @ViewChild('fileInput', {static: false}) fileInput: ElementRef;

  @ViewChild('dt', undefined) dt: Table;

  cols: any[];

  status: SelectItem[];

  statusForGlobal: SelectItem[];

  statusNew: SelectItem[];

  statusInProgress: SelectItem[];

  statusInfoNeeded: SelectItem[];

  statusRejected: SelectItem[];

  statusFixed: SelectItem[];

  severity: SelectItem[];

  severityForGlobal: SelectItem[];

  bugs: Array<Bug>;

  newBug: boolean;

  allUsers: Array<User>;

  mappedUsers: SelectItem[];

  fileSize: number;

  selectedBug: Bug;

  uploadedFileName: string;

  myAtt;

  attachments;

  currentAttachments: Array<Attachment>;

  temporatStatus: string;

  filteredBugs: any[];

  displayDialog: boolean;

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

  constructor(private router: Router, private bugServices: BugService, public modalService: NgbModal,
              private authService: AuthService, private fileService: FileService,
              private userService: UserService, private languageService: LanguageService, private excelService: ExcelService) {
  }

  ngOnInit() {

    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');
    this.languageService.getText('save');

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
    ];

    this.status = [
      {label: 'NEW', value: 'NEW'},
      {label: 'IN_PROGRESS', value: 'IN_PROGRESS'},
      {label: 'FIXED', value: 'FIXED'},
      {label: 'CLOSED', value: 'CLOSED'},
      {label: 'REJECTED', value: 'REJECTED'},
      {label: 'INFO_NEEDED', value: 'INFONEEDED'}
    ];

    this.statusForGlobal = [
      {label: this.languageService.getText('all-statuses'), value: ''},
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

    this.severityForGlobal = [
      {label: this.languageService.getText('all-severities'), value: ''},
      {label: 'LOW', value: 'LOW'},
      {label: 'MEDIUM', value: 'MEDIUM'},
      {label: 'HIGH', value: 'HIGH'},
      {label: 'CRITICAL', value: 'CRITICAL'},
    ];

    //function that filter after date in the calendar field.
    this.dt.filterConstraints['dateFilter'] = function inCollection(value: any, filter: any): boolean {
      if (filter === undefined || filter === null || (filter.length === 0 || filter === '') && value === null || filter === '') {
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


  /**
   * Gets all users for the bug fields (assigned to/ created by)
   *
   * */
  getUsers() {
    this.allUsers = new Array<User>();
    this.userService.getUsers().subscribe((data) => {
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

  /**
   * Searches all the bugs after a given criteria and loads the bugs list into the table.
   *
   * */
  public search() {
    this.bugServices.getBugsAfterSearchCriteria(this.bugSearchCriteria).subscribe((data) => {
      this.bugs = data;
      for (var bug of this.bugs) {
        var date = new Date(bug.targetDate);
        bug.targetDate = date;
      }
    })
  }

  /**
   * Opens a modal dialog for adding a new bug in he bug list and reload the data into the table.
   *
   * */
  add() {
    const modalRef = this.modalService.open(AddBugComponent, {windowClass: 'add-pop'});
    modalRef.result.then(() => {
      this.search();
    });
  }

  /**
   * Exports the selected bug from the bugs list in PDF.
   *
   * */
  export() {
    this.bugServices.exportInPdf(this.bug).subscribe(s => {
      window.open(s.toString(), '_self');
    })
  }

  /**
   * Delete the selected bug form the bugs list and reload the data into the table.
   *
   * */
  delete(id: number) {
    this.bugServices.deleteBugAfterId(id).subscribe(
      (data) => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-close-successful');
        modalRef.result.then(() => {
          this.search();
        });
      },
      (error1 => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-close-failed') + this.languageService.getText(error1.error.errorCode);
      }));
    this.displayDialog = false;
    this.search();
  }

  /**
   * Edit the selected bug form the bugs list and reload the data into the table.
   *
   * */
  save() {
    this.bug.status = this.temporatStatus;
    let attachmentToBeAdded: Attachment = {
      id: null,
      attContent: this.uploadedFileName,
    };
    if (this.attachments != null && this.checkFileSize()) {
      this.fileUpload();
    }
    if (!this.checkFileSize()) {
      attachmentToBeAdded.attContent = null;
    }
    this.bugServices.saveEditBug(this.bug, attachmentToBeAdded).subscribe(
      (data) => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-edit-successful');
        modalRef.result.then(() => {
          this.search();
        });
      },
      (error2 => {
        const modalRef = this.modalService.open(MessageComponent, {windowClass: 'add-pop'});
        modalRef.componentInstance.message = this.languageService.getText('bug-edit-failed') + this.languageService.getText(error2.error.errorCode);
      })
    );

    this.displayDialog = false;
    this.search();
  }

  /**
   * Upload the selected attachments of the current bug.
   *
   * */
  fileUpload() {
    const formModel = this.prepareSave();
    if (this.checkFileSize()) {
      this.fileService.uploadFile(formModel).subscribe(this.clearFile);
    }
  }

  prepareSave(): FormData {
    let input = new FormData();
    input.append('file', this.myAtt[0]);
    return input;
  }

  clearFile() {
    this.attachments = null;
    this.myAtt = null;
    this.uploadedFileName = '';
    if (this.fileInput.nativeElement != null)
      this.fileInput.nativeElement.value = '';
  }

  /**
   * On row select of the bugs table - shows a dialog with the bug details where you can delete/edit a bug.
   *
   * */
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

  /**
   * Validators for the bug.
   *
   * */
  checkTitle(): boolean {
    if (this.bug.title.length != 0) {
      return true;
    }
    return false;
  }

  checkDescription(): boolean {
    if (this.bug.description.length >= 250) {
      return true;
    }
    return false;
  }

  checkVersion(): boolean {
    let regexp = new RegExp('^(0|[1-9a-zA-Z][0-9a-zA-z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)$');
    if (regexp.test(this.bug.version)) {
      return true;
    }
    return false;
  }

  //check also null - not required
  checkFixedInVersion(): boolean {
    let regexp = new RegExp('^(0|[1-9a-zA-Z][0-9a-zA-z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)$');
    if (this.bug.fixedVersion == '') {
      return true;
    }
    if (regexp.test(this.bug.fixedVersion)) {
      return true;
    }
    return false;

  }

  //check not null
  checkFileExtension(): boolean {
    let regexp = new RegExp('([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.pdf|.doc|.odf|.xlsx|.xls|.png|.jpg)$');
    if (this.uploadedFileName == '') {
      return true;
    }
    if (regexp.test(this.uploadedFileName)) {
      return true;
    }
    return false;
    return true;

  }

  checkFileSize(): boolean {
    if (this.fileSize > 25) {
      return false;
    }
    return true;
  }

  cloneBug(b: Bug): Bug {
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

  /**
   * Edits the attachments of the selected bug form the bugs list.
   *
   * */
  onFileChange(event) {
    this.fileSize = event.target.files[0].size / 1024 / 1024; // in MB
    if (this.fileSize > 25) {
      this.clearFile();
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


  /**
   * Filter the bugs after the username of the assigned to/ created by user.
   *
   * */
  filterBugs(event) {
    this.userService.getUsers().subscribe((users) => {
      this.filteredBugs = users.map((u) => u.username).filter((u) => u.toLowerCase().indexOf(event.query.toLowerCase()) == 0);
    });
  }

  /**
   * Export the bugs list into excel.
   *
   * */
  exportAsXLSX() {
    if (this.dt.hasFilter()) {
      this.excelService.exportAsExcelFile(this.dt.filteredValue, 'bugs');
    } else {
      this.excelService.exportAsExcelFile(this.dt.value, 'bugs');
    }
  }

  /**
   * Deletes the attachments of the selected bug form the bugs list.
   *
   * */
  deleteAttachment(id: number) {
    this.bugServices.deleteAttachments(this.bug, id).subscribe();
    this.getAttachments(this.bug).subscribe(
      res => {
        this.currentAttachments = res;
      }
    )
  }
}
