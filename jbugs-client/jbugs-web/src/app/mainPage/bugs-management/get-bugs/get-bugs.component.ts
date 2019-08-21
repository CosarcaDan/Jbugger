import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {BugServiceService} from '../../service/bug/bug-service.service';
import {Bug} from '../../models/bug';

@Component({
  selector: 'app-get-bugs',
  templateUrl: './get-bugs.component.html',
  styleUrls: ['./get-bugs.component.css'],
  providers: [BugServiceService]

})
export class GetBugsComponent implements OnInit {

  bugs: Bug[];

  constructor(private router: Router, private bugServices: BugServiceService) {
  }

  ngOnInit() {
    this.getBugs();
  }

  getBugs() {
    this.bugs = [];
    this.bugServices.getBugs().subscribe((data: {}) => {
      console.log(data);
      // @ts-ignore
      this.bugs = data;
    });
  }

}
