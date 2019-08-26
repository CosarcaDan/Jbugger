import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BugValidators} from '../bug.validator';
import {UserService} from '../../../core/services/user/user.service';
import {BugServiceService} from '../../../core/services/bug/bug-service.service';
import {Bug} from '../../../core/models/bug';
import {FileService} from '../../../core/services/file/file.service';
import {Attachment} from '../../../core/models/attachment';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../../core/services/auth/auth.service';
import {User} from '../../../core/models/user';

@Component({
  selector: 'app-add-bug',
  templateUrl: './add-bug.component.html',
  styleUrls: ['./add-bug.component.css'],
})
export class AddBugComponent implements OnInit {

  form: FormGroup;
  todayDate = new Date();
  currentLoggedUser;
  currentStatus;
  allUsers: Array<User>;
  uploadedFileName: string;
  myAtt;
  @ViewChild('fileInput', {static: false}) fileInput: ElementRef;

  //bug attributes
  title: string;
  description: string;
  version: string;
  targetDate: Date;
  status: string;
  fixedVersion: string;
  severity: string;
  created: string;
  assigned: string;

  severitys = [
    {type: 'LOW'},
    {type: 'MEDIUM'},
    {type: 'HIGH'},
    {type: 'CRITICAL'},
  ];

  constructor(private fb: FormBuilder, private userService: UserService, private bugService: BugServiceService,
              private fileService: FileService, public activeModal: NgbActiveModal, private authService: AuthService) {
    this.form = fb.group({
      title: [null, [Validators.required,]],
      description: [null, [Validators.required, Validators.minLength(250)]],
      version: [null, [BugValidators.validateVersion]],
      fixedVersion: [null, [BugValidators.validateVersion]],
      targetDate: [null, []],
      severity: [null, []],
      createdBy: [null, []],
      status: [null, []],
      assignedTo: [null, []],
      attachments: ['',[Validators.required]],
    });
  }

  ngOnInit() {
    this.currentLoggedUser = {name: this.authService.getUsername()};
    this.currentStatus = {name: 'NEW'};
    this.getUsers();
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

  addBug() {
    this.title = this.form.get('title').value.toString();
    this.description = this.form.get('description').value.toString();
    this.version = this.form.get('version').value.toString();
    this.targetDate = this.form.get('targetDate').value.toString();
    this.status = this.currentStatus.name;
    this.fixedVersion = this.form.get('fixedVersion').value.toString();
    this.severity = this.form.get('severity').value.toString();
    this.created = this.authService.getUsername();
    this.assigned = this.form.get('assignedTo').value.toString();

    let bugToBeAdded: Bug = {
      id: null,
      title: this.title,
      description: this.description,
      version: this.version,
      targetDate: this.targetDate,
      status: this.status,
      fixedVersion: this.fixedVersion,
      severity: this.severity,
      created: this.created,
      assigned: this.assigned
    };

    let attachmentToBeAdded: Attachment = {
      id: null,
      attContent: this.uploadedFileName,
    };
    this.fileUpload();
    this.bugService.add(bugToBeAdded, attachmentToBeAdded).subscribe(
      (data: {}) => {
        alert(data);
      },
      (error2 => {
        console.log('Error', error2);
        alert('Bug Add failed :' + error2.error.detailMessage);
      })
    );
    this.clearFile();
    this.activeModal.close();
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      let files = event.target.files;
      this.uploadedFileName = files[0].name;
      this.myAtt = files;
    }
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
    this.form.get('attachments').setValue(null);
    this.fileInput.nativeElement.value = '';
  }

}
