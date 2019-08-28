import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class LanguageService implements OnInit{

  private labels: {label,ro,en}[];

  constructor(private http: HttpClient) {
    this.ngOnInit();
  }

  ngOnInit(): void {
      this.http.get('./assets/labels.json').subscribe(
        (res:{labels:{label,ro,en}[]})=> {
          console.log('res',res)
          this.labels=res.labels
        }
      )
  }

  getText(label:string){
    let language = sessionStorage.getItem('language');
    // console.log(this.labels);
    let reslabel = this.labels.find(l=>l.label==label);
    if(language=='en') {
      console.log('label: ', reslabel.en);
      return reslabel.en;
    }
    if(language == 'ro') {
      console.log('label: ', reslabel.ro);
      return reslabel.ro;
    }
  }

}
