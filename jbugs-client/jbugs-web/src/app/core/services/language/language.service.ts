import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LanguageService implements OnInit {

  private labels: { label, value }[];
  private languageLoaded: string = null;

  constructor(private http: HttpClient) {
    this.ngOnInit();
  }

  ngOnInit(): void {
    this.http.get('./assets/labels-' + localStorage.getItem('language') + '.json').subscribe(
      (res: { labels: { label, value }[] }) => {
        this.labels = res.labels;
        this.languageLoaded = localStorage.getItem('language');
      }
    );
  }

  getText(label: string) {
    if (this.languageLoaded == null || this.languageLoaded != localStorage.getItem('language')) {
      this.ngOnInit();
    }
    return this.labels.find(l => l.label == label).value;
    // if(language=='en') {
    //   console.log('label: ', reslabel.en);
    //   return reslabel.en;
    // }
    // if(language == 'ro') {
    //   console.log('label: ', reslabel.ro);
    //   return reslabel.ro;
    // }
  }

}
