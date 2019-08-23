import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BugValidators} from '../bug.validator';
import {UserAdd} from '../../../user-management/models/userAdd';
import {UserService} from '../../service/user/user.service';
import {BugServiceService} from '../../service/bug/bug-service.service';
import {Bug} from '../../models/bug';

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
  allUsers: Array<UserAdd>;
  loggedUsername = sessionStorage.getItem('username');
  loading: boolean = false;
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

  constructor(private fb: FormBuilder, private userService: UserService, private bugService: BugServiceService) {
    this.form = fb.group({
      title: [null, [Validators.required,]],
      description: [null, [Validators.required, Validators.minLength(2)]],
      version: [null, [BugValidators.validateVersion]],
      fixedVersion: [null, [BugValidators.validateVersion]],
      targetDate: [null, []],
      severity: [null, []],
      createdBy: [null, []],
      status: [null, []],
      assignedTo: [null, []],
      attachment: null,
    });
  }

  ngOnInit() {
    this.currentLoggedUser = {name: this.loggedUsername};
    this.currentStatus = {name: 'NEW'};
    this.getUsers();
  }

  getUsers() {
    this.allUsers = new Array<UserAdd>();
    this.userService.getUsers().subscribe((data) => {
      console.log('data:', data);
      // @ts-ignore
      for (let dataKey of data) {
        this.allUsers.push(dataKey);
      }
      console.log('roleset: ', this.allUsers);
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
    this.created = this.loggedUsername;
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
    this.uploadFile();
    this.bugService.add(bugToBeAdded);
  }

  onFileChange(event) {
    let reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      let file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.form.get('attachment').setValue({
          filename: file.name,
          filetype: file.type,
          value: (<string> reader.result).split(',')[1]
        });
      };
    }
  }

  uploadFile() {
    const formModel = this.form.value;
    this.loading = true;
    // In a real-world app you'd have a http request / service call here like
    // this.http.post('apiUrl', formModel)
    setTimeout(() => {
      console.log(formModel);
      alert('done!');
      this.loading = false;
    }, 1000);
  }

  clearFile() {
    this.form.get('attachment').setValue(null);
    this.fileInput.nativeElement.value = '';
  }
}
