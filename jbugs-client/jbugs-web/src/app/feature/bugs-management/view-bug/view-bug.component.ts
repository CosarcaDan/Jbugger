import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BugService} from '../../../core/services/bug/bug.service';
import {Bug} from '../../../core/models/bug';
import {LanguageService} from '../../../core/services/language/language.service';

@Component({
  selector: 'app-view-bug',
  templateUrl: './view-bug.component.html',
  styleUrls: ['./view-bug.component.css']
})
export class ViewBugComponent implements OnInit {
  private id: number;
  bug: Bug;

  constructor(private route: ActivatedRoute, private bugService: BugService,
              private languageService: LanguageService) {

  }

  ngOnInit() {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.bugService.getBug(this.id).subscribe((data) => {
      this.bug = data;
    });
  }

}
