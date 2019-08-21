import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-add-bug',
  templateUrl: './add-bug.component.html',
  styleUrls: ['./add-bug.component.css']
})
export class AddBugComponent implements OnInit {
  form: FormGroup;
  date: Date;

  severitys: string[]; // take from rest on nginit
  defaultSeverity:string;

  constructor(fb:FormBuilder) {
    this.form = fb.group({title:[]});
  }

  ngOnInit() {
  }

  onBasicUpload($event: any) {

  }
}
