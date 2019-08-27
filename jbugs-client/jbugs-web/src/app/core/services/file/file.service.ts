import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FileService {
  base_url: string = 'http://localhost:8080/jbugs/services/files';

  constructor(private http: HttpClient) {
  }

  public uploadFile(file):Observable<any> {
    console.log('file',file.get('file'));
    return this.http.post(this.base_url + '/upload', file);
  }

}
